package com.gallery.core.delegate.impl

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.gallery.core.GalleryConfigs.Companion.configs
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.adapter.GalleryAdapter
import com.gallery.core.delegate.args.ScanArgs
import com.gallery.core.delegate.args.ScanArgs.Companion.scanArgs
import com.gallery.core.delegate.args.ScanArgs.Companion.toBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraResultContract
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.CameraUri
import com.gallery.core.extensions.PermissionCode
import com.gallery.core.extensions.checkCameraPermission
import com.gallery.core.extensions.checkWritePermission
import com.gallery.core.extensions.delete
import com.gallery.core.extensions.drawable
import com.gallery.core.extensions.fileExists
import com.gallery.core.extensions.findIdByUri
import com.gallery.core.extensions.hide
import com.gallery.core.extensions.openVideo
import com.gallery.core.extensions.orEmpty
import com.gallery.core.extensions.scanFile
import com.gallery.core.extensions.show
import com.gallery.core.extensions.square
import com.gallery.core.extensions.takePictureUri
import com.gallery.core.extensions.toScanEntity
import com.gallery.scan.Types
import com.gallery.scan.args.MediaResult
import com.gallery.scan.extensions.fileScan
import com.gallery.scan.extensions.isScanAllMedia
import com.gallery.scan.impl.MediaScanImpl
import com.gallery.scan.impl.file.FileScanEntity

/**
 * 图库代理
 */
class ScanDelegateImpl(
    /**
     * [Fragment]
     * 承载容器
     * [Fragment]中必须存在 [R.id.gallery_recyclerview] [R.id.gallery_empty_view] 两个id的View
     */
    private val fragment: Fragment,
    /**
     * [ICrop]
     * 裁剪
     */
    private val galleryICrop: ICrop?,
    /**
     * [IGalleryCallback]
     * 各种回调
     */
    private val galleryCallback: IGalleryCallback,
    /**
     * [IGalleryInterceptor]
     * 拦截功能回调，目前支持自定义相机和占位符点击事件
     */
    private val galleryInterceptor: IGalleryInterceptor,
    /**
     * [IGalleryImageLoader]
     * 图片加载框架
     */
    private val galleryImageLoader: IGalleryImageLoader,
) : IScanDelegate, GalleryAdapter.OnGalleryItemClickListener {

    private var fileUri: Uri = Uri.EMPTY
    private var parentId: Long = Types.Id.ALL

    /** 相机启动器 */
    private val openCameraLauncher: ActivityResultLauncher<CameraUri> =
        fragment.registerForActivityResult(CameraResultContract()) {
            when (it) {
                Activity.RESULT_CANCELED -> takePictureCanceled()
                Activity.RESULT_OK -> takePictureSuccess()
            }
        }

    /** 裁剪启动器 */
    private val cropLauncher: ActivityResultLauncher<Intent> =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            galleryICrop?.onCropResult(this, configs, it)
        }

    /** 相机权限启动器 */
    private val cameraPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                permissionsGranted(PermissionCode.READ)
            } else {
                permissionsDenied(PermissionCode.READ)
            }
        }

    /** 读写权限启动器 */
    private val writePermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                permissionsGranted(PermissionCode.WRITE)
            } else {
                permissionsDenied(PermissionCode.WRITE)
            }
        }

    /** 涉及到View的获取，获取[ScanDelegateImpl]实例时必须在[Fragment.onViewCreated]之后 */
    private val configs = fragment.arguments.orEmpty().configs
    private val galleryAdapter = GalleryAdapter(
        requireActivity.square(configs.gridConfig.spanCount),
        configs,
        galleryCallback,
        galleryImageLoader,
        this
    )
    private val recyclerView: RecyclerView = rootView.findViewById(R.id.gallery_recyclerview)
    private val emptyView: ImageView = rootView.findViewById(R.id.gallery_empty_view)

    private val scan: MediaScanImpl<FileScanEntity> =
        MediaScanImpl(fragment.fileScan(configs.fileScanArgs)) {
            when (this) {
                is MediaResult.Multiple -> onScanMultipleSuccess(multipleValue)
                is MediaResult.Single -> onScanSingleSuccess(singleValue)
            }
        }

    override val rootView: View get() = fragment.requireView()
    override val activity: FragmentActivity? get() = fragment.activity
    override val requireActivity: FragmentActivity get() = fragment.requireActivity()
    override val selectItem: ArrayList<ScanEntity> get() = galleryAdapter.currentSelectList
    override val currentParentId: Long get() = parentId
    override val allItem: ArrayList<ScanEntity>
        get() = galleryAdapter.currentList.filterTo(ArrayList()) { it.parent != GalleryAdapter.CAMERA }

    override fun updateParentId(parentId: Long) {
        this.parentId = parentId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        ScanArgs.onSaveInstanceState(parentId, fileUri, selectItem).toBundle(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val selectList = savedInstanceState?.scanArgs?.let {
            parentId = it.parentId
            fileUri = it.fileUri
            it.selectList
        }
        recyclerView.layoutManager = GridLayoutManager(
            recyclerView.context,
            if (configs.gridConfig.orientation == RecyclerView.HORIZONTAL) 1 else configs.gridConfig.spanCount,
            configs.gridConfig.orientation,
            false
        )
        if (recyclerView.itemAnimator is SimpleItemAnimator) {
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        emptyView.setImageDrawable(requireActivity.drawable(configs.cameraConfig.emptyIcon))
        emptyView.setOnClickListener { v ->
            if (galleryInterceptor.onEmptyPhotoClick(v) && emptyView.drawable != null) {
                openSystemCamera()
            }
        }
        galleryAdapter.addSelectAll(selectList ?: configs.selects)
        recyclerView.adapter = galleryAdapter
        onScanGallery(parentId)
        galleryCallback.onGalleryCreated(this, configs, savedInstanceState)
    }

    override fun onDestroy() {
        openCameraLauncher.unregister()
        cropLauncher.unregister()
        cameraPermissionLauncher.unregister()
        writePermissionLauncher.unregister()
    }

    override fun onScanMultipleSuccess(scanEntities: ArrayList<FileScanEntity>) {
        if (scanEntities.isEmpty() && parentId.isScanAllMedia) {
            emptyView.show()
            recyclerView.hide()
            galleryCallback.onScanSuccessEmpty(requireActivity)
            return
        }
        emptyView.hide()
        recyclerView.show()
        if (parentId.isScanAllMedia && !configs.hideCamera) {
            scanEntities.add(0, FileScanEntity(parent = GalleryAdapter.CAMERA))
        }
        val toScanEntity = scanEntities.toScanEntity()
        galleryAdapter.addAll(toScanEntity)
        galleryAdapter.updateEntity()
        galleryCallback.onScanSuccess(allItem)
        scrollToPosition(0)
    }

    override fun onScanSingleSuccess(scanEntity: FileScanEntity?) {
        activity ?: return
        scanEntity ?: return galleryCallback.onResultError(requireActivity, configs)
        val toScanEntity = scanEntity.toScanEntity()
        //拍照或裁剪成功扫描到数据之后根据扫描方式更新数据
        if (parentId.isScanAllMedia || parentId == scanEntity.parent) {
            if (configs.sort.first == Types.Sort.DESC) {
                galleryAdapter.addEntity(if (configs.hideCamera) 0 else 1, toScanEntity)
            } else {
                galleryAdapter.addEntity(toScanEntity)
                scrollToPosition(galleryAdapter.currentList.size - 1)
            }
        }
        notifyDataSetChanged()
        galleryCallback.onResultSuccess(requireActivity, toScanEntity)
    }

    override fun onCameraItemClick(view: View, position: Int, scanEntity: ScanEntity) {
        openSystemCamera()
    }

    override fun onPhotoItemClick(view: View, position: Int, scanEntity: ScanEntity) {
        if (!scanEntity.uri.fileExists(requireActivity)) {
            galleryCallback.onClickItemFileNotExist(requireActivity, scanEntity)
            return
        }
        if (configs.isScanVideoMedia) {
            requireActivity.openVideo(scanEntity.uri) {
                galleryCallback.onOpenVideoPlayError(requireActivity, scanEntity)
            }
            return
        }
        if (configs.radio) {
            if (configs.crop) {
                cropLauncher.launch(
                    galleryICrop?.openCrop(requireActivity, configs, scanEntity.uri)
                )
            } else {
                galleryCallback.onGalleryResource(requireActivity, scanEntity)
            }
            return
        }
        galleryCallback.onPhotoItemClick(
            requireActivity,
            configs,
            scanEntity,
            position,
            parentId
        )
    }

    override fun openSystemCamera() {

        fun Fragment.checkPermissionAndRequestCamera(launcher: ActivityResultLauncher<String>): Boolean {
            return if (!checkCameraPermission()) {
                launcher.launch(Manifest.permission.CAMERA)
                false
            } else {
                true
            }
        }

        if (!fragment.checkPermissionAndRequestCamera(cameraPermissionLauncher)) {
            galleryCallback.onCameraOpenStatus(requireActivity, CameraStatus.PERMISSION)
            return
        }
        val cameraUri: Uri? = requireActivity.takePictureUri(configs)
        cameraUri?.let {
            fileUri = it
        } ?: return galleryCallback.onCameraOpenStatus(requireActivity, CameraStatus.ERROR)
        val onCustomCamera: Boolean = galleryInterceptor.onCustomCamera(fileUri)
        if (onCustomCamera) {
            return
        }

        fun Fragment.checkCameraStatus(
            uri: CameraUri,
            action: (uri: CameraUri) -> Unit
        ): CameraStatus {
            val intent =
                if (uri.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                else
                    Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            return intent.resolveActivity(requireActivity().packageManager)?.let {
                action.invoke(uri)
                CameraStatus.SUCCESS
            } ?: CameraStatus.ERROR
        }

        val uri = CameraUri(configs.type, fileUri)

        galleryCallback.onCameraOpenStatus(
            requireActivity,
            fragment.checkCameraStatus(uri) { openCameraLauncher.launch(it) }
        )
    }

    override fun takePictureSuccess() {
        requireActivity.scanFile(fileUri) { onScanGallery(parentId, true) }
        if (configs.takePictureCrop) {
            cropLauncher.launch(
                galleryICrop?.openCrop(requireActivity, configs, fileUri)
            )
        }
    }

    override fun takePictureCanceled() {
        fileUri.delete(requireActivity)
        fileUri = Uri.EMPTY
        galleryCallback.onCameraCanceled(requireActivity, configs)
    }

    override fun permissionsGranted(type: PermissionCode) {
        when (type) {
            PermissionCode.WRITE -> onScanGallery(parentId)
            PermissionCode.READ -> openSystemCamera()
        }
    }

    override fun permissionsDenied(type: PermissionCode) {
        galleryCallback.onPermissionsDenied(activity, type)
    }

    override fun onScanGallery(parent: Long, isCamera: Boolean) {

        fun Fragment.checkPermissionAndRequestWrite(launcher: ActivityResultLauncher<String>): Boolean {
            return if (!checkWritePermission()) {
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                false
            } else {
                true
            }
        }

        if (!fragment.checkPermissionAndRequestWrite(writePermissionLauncher)) {
            return
        }
        this.parentId = parent
        // 如果本机没有图片,进来拍照直接走扫描全部的方法可以兼容到hideCamera
        // resultSuccess只有在拍照成功并且之前数据不为空or裁剪成功才会回调
        // 拍照成功之后不需要特殊处理,因为肯定是SCAN_ALL的情况下,直接更新数据即可
        // 裁剪成功分为两种情况
        // 第一种:SCAN_ALL情况下直接更新数据
        // 第二种:parentId为文件夹ID的时候处理数据,如果 parentId == scan.parent
        // 可以直接插入到当前数据,如果不等于,不能插入,因为裁剪之后的图片属于另一个文件夹的数据
        // 文件夹数据更新的时候也需要处理这种情况
        if (isCamera && galleryAdapter.isNotEmpty) {
            scan.single(requireActivity.findIdByUri(fileUri))
        } else {
            scan.multiple(parent)
        }
    }

    override fun onScanResult(uri: Uri) {
        requireActivity.scanFile(uri) {
            scan.single(requireActivity.findIdByUri(it))
        }
    }

    override fun onUpdateResult(args: ScanArgs) {
        if (!args.isRefresh || selectItem == args.selectList) {
            return
        }
        galleryAdapter.addSelectAll(args.selectList)
        galleryAdapter.updateEntity()
        galleryCallback.onRefreshResultChanged(selectCount)
    }

    override fun notifyItemChanged(position: Int) {
        galleryAdapter.notifyItemChanged(position)
    }

    override fun notifyDataSetChanged() {
        galleryAdapter.notifyDataSetChanged()
    }

    override fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(listener)
    }

    override fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

}