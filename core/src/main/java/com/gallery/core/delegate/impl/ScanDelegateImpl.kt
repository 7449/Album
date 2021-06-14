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
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryBundle.Companion.galleryBundleOrDefault
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.adapter.GalleryAdapter
import com.gallery.core.delegate.args.ScanArgs
import com.gallery.core.delegate.args.ScanArgs.Companion.putScanArgs
import com.gallery.core.delegate.args.ScanArgs.Companion.scanArgs
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.*
import com.gallery.core.widget.GalleryDivider
import com.gallery.scan.Types
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.scan.extensions.multipleScanExpand
import com.gallery.scan.extensions.scanCore
import com.gallery.scan.extensions.singleScanExpand
import com.gallery.scan.impl.ScanImpl
import com.gallery.scan.impl.file.FileScanArgs
import com.gallery.scan.impl.file.FileScanEntity
import com.gallery.scan.impl.file.fileExpand
import com.gallery.scan.result.Result

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
    private var parentId: Long = Types.Scan.SCAN_ALL

    /** 相机启动器 */
    private val openCameraLauncher: ActivityResultLauncher<CameraUri> =
        fragment.registerForActivityResult(CameraResultContract()) {
            when (it) {
                Activity.RESULT_CANCELED -> cameraCanceled()
                Activity.RESULT_OK -> cameraSuccess()
            }
        }

    /** 裁剪启动器 */
    private val cropLauncher: ActivityResultLauncher<Intent> =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            galleryICrop?.onCropResult(this, galleryBundle, it)
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
    private val galleryBundle: GalleryBundle =
        fragment.arguments.orEmptyExpand().galleryBundleOrDefault
    private val galleryAdapter: GalleryAdapter = GalleryAdapter(
        requireActivity.squareExpand(galleryBundle.spanCount),
        galleryBundle,
        galleryCallback,
        galleryImageLoader,
        this
    )
    private val recyclerView: RecyclerView =
        fragment.requireView().findViewById(R.id.gallery_recyclerview) as RecyclerView
    private val emptyView: ImageView =
        fragment.requireView().findViewById(R.id.gallery_empty_view) as ImageView

    private val scan: ScanImpl<FileScanEntity> =
        ScanImpl(
            fragment.scanCore(
                factory = ScanEntityFactory.fileExpand(),
                args = FileScanArgs(
                    galleryBundle.scanType.map { it.toString() }.toTypedArray(),
                    galleryBundle.scanSortField,
                    galleryBundle.scanSort
                )
            )
        ) {
            when (this) {
                is Result.Multiple -> onScanMultipleSuccess(multipleValue)
                is Result.Single -> onScanSingleSuccess(singleValue)
            }
        }

    override val rootView: View get() = fragment.requireView()
    override val activity: FragmentActivity? get() = fragment.activity
    override val requireActivity: FragmentActivity get() = fragment.requireActivity()
    override val selectItem: ArrayList<ScanEntity> get() = galleryAdapter.currentSelectList
    override val currentParentId: Long get() = parentId
    override val allItem: ArrayList<ScanEntity>
        get() = galleryAdapter.currentList.filterTo(
            ArrayList()
        ) { it.parent != GalleryAdapter.CAMERA }

    override fun onUpdateParentId(parentId: Long) {
        this.parentId = parentId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        ScanArgs.newSaveInstance(parentId, fileUri, selectItem).putScanArgs(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val selectList = savedInstanceState?.scanArgs?.let {
            parentId = it.parentId
            fileUri = it.fileUri
            it.selectList
        }
        recyclerView.layoutManager = GridLayoutManager(
            recyclerView.context,
            galleryBundle.spanCount,
            galleryBundle.orientation,
            false
        )
        recyclerView.addItemDecoration(GalleryDivider(galleryBundle.dividerWidth))
        if (recyclerView.itemAnimator is SimpleItemAnimator) {
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        emptyView.setImageDrawable(requireActivity.drawableExpand(galleryBundle.photoEmptyDrawable))
        emptyView.setOnClickListener { v ->
            if (galleryInterceptor.onEmptyPhotoClick(v) && emptyView.drawable != null) {
                cameraOpen()
            }
        }
        galleryAdapter.addSelectAll(selectList ?: galleryBundle.selectEntities)
        recyclerView.adapter = galleryAdapter
        onScanGallery(parentId)
        galleryCallback.onGalleryCreated(this, galleryBundle, savedInstanceState)
    }

    override fun onDestroy() {
        openCameraLauncher.unregister()
        cropLauncher.unregister()
        cameraPermissionLauncher.unregister()
        writePermissionLauncher.unregister()
    }

    override fun onScanMultipleSuccess(scanEntities: ArrayList<FileScanEntity>) {
        if (scanEntities.isEmpty() && parentId.isScanAllExpand) {
            emptyView.showExpand()
            recyclerView.hideExpand()
            galleryCallback.onScanSuccessEmpty(activity)
            return
        }
        emptyView.hideExpand()
        recyclerView.showExpand()
        if (parentId.isScanAllExpand && !galleryBundle.hideCamera) {
            scanEntities.add(0, FileScanEntity(parent = GalleryAdapter.CAMERA))
        }
        val toScanEntity = scanEntities.toScanEntity()
        galleryAdapter.addAll(toScanEntity)
        galleryAdapter.updateEntity()
        galleryCallback.onScanSuccess(allItem)
        scrollToPosition(0)
    }

    override fun onScanSingleSuccess(scanEntity: FileScanEntity?) {
        scanEntity ?: return galleryCallback.onResultError(activity, galleryBundle)
        val toScanEntity = scanEntity.toScanEntity()
        //拍照或裁剪成功扫描到数据之后根据扫描方式更新数据
        if (parentId.isScanAllExpand || parentId == scanEntity.parent) {
            if (galleryBundle.scanSort == Types.Sort.DESC) {
                galleryAdapter.addEntity(if (galleryBundle.hideCamera) 0 else 1, toScanEntity)
            } else {
                galleryAdapter.addEntity(toScanEntity)
                scrollToPosition(galleryAdapter.currentList.size - 1)
            }
        }
        notifyDataSetChanged()
        galleryCallback.onResultSuccess(activity, toScanEntity)
    }

    override fun onCameraItemClick(view: View, position: Int, scanEntity: ScanEntity) {
        cameraOpen()
    }

    override fun onPhotoItemClick(view: View, position: Int, scanEntity: ScanEntity) {
        if (!scanEntity.uri.isFileExistsExpand(requireActivity)) {
            galleryCallback.onClickItemFileNotExist(requireActivity, scanEntity)
            return
        }
        if (galleryBundle.isVideoScanExpand) {
            requireActivity.openVideoExpand(scanEntity.uri) {
                galleryCallback.onOpenVideoPlayError(requireActivity, scanEntity)
            }
            return
        }
        if (galleryBundle.radio) {
            if (galleryBundle.crop) {
                cropLauncher.launch(
                    galleryICrop?.openCrop(
                        requireActivity,
                        galleryBundle,
                        scanEntity.uri
                    )
                )
            } else {
                galleryCallback.onGalleryResource(requireActivity, scanEntity)
            }
            return
        }
        galleryCallback.onPhotoItemClick(
            requireActivity,
            galleryBundle,
            scanEntity,
            position,
            parentId
        )
    }

    override fun cameraOpen() {

        fun Fragment.checkPermissionAndRequestCameraExpand(launcher: ActivityResultLauncher<String>): Boolean {
            return if (!checkCameraPermissionExpand()) {
                launcher.launch(Manifest.permission.CAMERA)
                false
            } else {
                true
            }
        }

        if (!fragment.checkPermissionAndRequestCameraExpand(cameraPermissionLauncher)) {
            galleryCallback.onCameraOpenStatus(activity, CameraStatus.PERMISSION)
            return
        }
        val cameraUri: Uri? = requireActivity.cameraUriExpand(galleryBundle)
        cameraUri?.let {
            fileUri = it
        } ?: galleryCallback.onCameraOpenStatus(activity, CameraStatus.ERROR)
        val onCustomCamera: Boolean = galleryInterceptor.onCustomCamera(fileUri)
        if (onCustomCamera || cameraUri == null) {
            return
        }

        fun Fragment.checkCameraStatusExpand(
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

        val uri = CameraUri(
            galleryBundle.scanType,
            fileUri
        )

        galleryCallback.onCameraOpenStatus(
            activity,
            fragment.checkCameraStatusExpand(uri) { openCameraLauncher.launch(it) }
        )
    }

    override fun cameraSuccess() {
        requireActivity.scanFileExpand(fileUri) { onScanGallery(parentId, true) }
        if (galleryBundle.cameraCrop) {
            cropLauncher.launch(galleryICrop?.openCrop(requireActivity, galleryBundle, fileUri))
        }
    }

    override fun cameraCanceled() {
        fileUri.deleteExpand(requireActivity)
        fileUri = Uri.EMPTY
        galleryCallback.onCameraCanceled(activity, galleryBundle)
    }

    override fun permissionsGranted(type: PermissionCode) {
        when (type) {
            PermissionCode.WRITE -> onScanGallery(parentId)
            PermissionCode.READ -> cameraOpen()
        }
    }

    override fun permissionsDenied(type: PermissionCode) {
        galleryCallback.onPermissionsDenied(activity, type)
    }

    override fun onScanGallery(parent: Long, isCamera: Boolean) {

        fun Fragment.checkPermissionAndRequestWriteExpand(launcher: ActivityResultLauncher<String>): Boolean {
            return if (!checkWritePermissionExpand()) {
                launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                false
            } else {
                true
            }
        }

        if (!fragment.checkPermissionAndRequestWriteExpand(writePermissionLauncher)) {
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
            scan.scanSingle(requireActivity.findIdByUriExpand(fileUri).singleScanExpand())
        } else {
            scan.scanMultiple(parent.multipleScanExpand())
        }
    }

    override fun onScanResult(uri: Uri) {
        requireActivity.scanFileExpand(uri) {
            scan.scanSingle(requireActivity.findIdByUriExpand(it).singleScanExpand())
        }
    }

    override fun onUpdateResult(scanArgs: ScanArgs?) {
        scanArgs ?: return
        if (!scanArgs.isRefresh || selectItem == scanArgs.selectList) {
            return
        }
        galleryAdapter.addSelectAll(scanArgs.selectList)
        galleryAdapter.updateEntity()
        galleryCallback.onChangedResultCount(selectCount)
    }

    override fun notifyItemChanged(position: Int) {
        galleryAdapter.notifyItemChanged(position)
    }

    override fun notifyDataSetChanged() {
        galleryAdapter.notifyDataSetChanged()
    }

    override fun addOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        recyclerView.addOnScrollListener(onScrollListener)
    }

    override fun scrollToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
    }

}