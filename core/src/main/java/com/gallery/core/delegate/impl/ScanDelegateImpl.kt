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
import com.gallery.core.R
import com.gallery.core.args.GalleryConfigs
import com.gallery.core.args.GalleryConfigs.Companion.configs
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
import com.gallery.core.extensions.checkReadPermission
import com.gallery.core.extensions.delete
import com.gallery.core.extensions.drawable
import com.gallery.core.extensions.fileExists
import com.gallery.core.extensions.findIdByUri
import com.gallery.core.extensions.hide
import com.gallery.core.extensions.openVideo
import com.gallery.core.extensions.orEmpty
import com.gallery.core.extensions.scanFile
import com.gallery.core.extensions.show
import com.gallery.core.extensions.takePictureUri
import com.gallery.core.extensions.toScanEntity
import com.gallery.scan.Types
import com.gallery.scan.args.MediaResult
import com.gallery.scan.extensions.fileScan
import com.gallery.scan.extensions.isScanAllMedia
import com.gallery.scan.impl.MediaScanImpl
import com.gallery.scan.impl.file.FileScanEntity

class ScanDelegateImpl(
    /**
     * [Fragment]
     * 承载容器
     * [Fragment]中必须存在 [R.id.gallery_recyclerview] [R.id.gallery_empty_view] 两个id的View
     */
    private val fragment: Fragment,
    private val iCrop: ICrop?,
    private val callback: IGalleryCallback,
    private val interceptor: IGalleryInterceptor,
    private val loader: IGalleryImageLoader,
) : IScanDelegate, GalleryAdapter.OnGalleryItemClickListener {

    private var fileUri: Uri = Uri.EMPTY
    private var parentId: Long = Types.Id.ALL

    private val openCameraLauncher: ActivityResultLauncher<CameraUri> =
        fragment.registerForActivityResult(CameraResultContract()) {
            when (it) {
                Activity.RESULT_CANCELED -> takePictureCanceled()
                Activity.RESULT_OK -> takePictureSuccess()
            }
        }

    private val cropLauncher: ActivityResultLauncher<Intent> =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            iCrop?.onCropResult(this, it)
        }

    private val cameraPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                permissionsGranted(PermissionCode.CAMERA)
            } else {
                permissionsDenied(PermissionCode.CAMERA)
            }
        }

    private val readPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                permissionsGranted(PermissionCode.READ)
            } else {
                permissionsDenied(PermissionCode.READ)
            }
        }

    /** 涉及到View的获取，获取[ScanDelegateImpl]实例时必须在[Fragment.onViewCreated]之后 */
    private val configs: GalleryConfigs = fragment.arguments.orEmpty().configs
    private val listView: RecyclerView = rootView.findViewById(R.id.gallery_recyclerview)
    private val emptyView: ImageView = rootView.findViewById(R.id.gallery_empty_view)
    private val galleryAdapter = GalleryAdapter(configs, callback, loader, this)

    private val fileScanArgs = fragment.fileScan(configs.fileScanArgs)
    private val mediaScan: MediaScanImpl<FileScanEntity> = MediaScanImpl(fileScanArgs) {
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
        listView.layoutManager = GridLayoutManager(
            listView.context,
            if (configs.gridConfig.orientation == RecyclerView.HORIZONTAL) 1 else configs.gridConfig.spanCount,
            configs.gridConfig.orientation,
            false
        )
        if (listView.itemAnimator is SimpleItemAnimator) {
            (listView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
        emptyView.setImageDrawable(requireActivity.drawable(configs.cameraConfig.emptyIcon))
        emptyView.setOnClickListener { v ->
            if (interceptor.onEmptyPhotoClick(v) && emptyView.drawable != null) {
                openSystemCamera()
            }
        }
        galleryAdapter.addSelectAll(selectList ?: configs.selects)
        listView.adapter = galleryAdapter
        onScanGallery(parentId)
        callback.onGalleryCreated(this, savedInstanceState)
    }

    override fun onDestroy() {
        openCameraLauncher.unregister()
        cropLauncher.unregister()
        cameraPermissionLauncher.unregister()
        readPermissionLauncher.unregister()
    }

    override fun onScanMultipleSuccess(scanEntities: ArrayList<FileScanEntity>) {
        if (scanEntities.isEmpty() && parentId.isScanAllMedia) {
            emptyView.show()
            listView.hide()
            callback.onScanMultipleEmpty()
            return
        }
        emptyView.hide()
        listView.show()
        if (parentId.isScanAllMedia && !configs.hideCamera) {
            scanEntities.add(0, FileScanEntity(parent = GalleryAdapter.CAMERA))
        }
        galleryAdapter.addAll(scanEntities.toScanEntity())
        galleryAdapter.updateEntity()
        callback.onScanMultipleSuccess()
        scrollToPosition(0)
    }

    override fun onScanSingleSuccess(scanEntity: FileScanEntity?) {
        activity ?: return callback.onScanSingleFailure()
        scanEntity ?: return callback.onScanSingleFailure()
        val toScanEntity = scanEntity.toScanEntity()
        if (parentId.isScanAllMedia || parentId == scanEntity.parent) {
            if (configs.sort.first == Types.Sort.DESC) {
                val item = galleryAdapter.currentList.find { it.parent == GalleryAdapter.CAMERA }
                galleryAdapter.addEntity(if (item == null) 0 else 1, toScanEntity)
            } else {
                galleryAdapter.addEntity(toScanEntity)
                scrollToPosition(galleryAdapter.currentList.size - 1)
            }
        }
        notifyDataSetChanged()
        callback.onScanSingleSuccess(toScanEntity)
    }

    override fun onCameraItemClick() {
        openSystemCamera()
    }

    override fun onPhotoItemClick(position: Int, scanEntity: ScanEntity) {
        if (!scanEntity.uri.fileExists(requireActivity)) {
            callback.onClickItemFileNotExist(scanEntity)
            return
        }
        if (configs.isScanVideoMedia) {
            if (!interceptor.onOpenVideo(scanEntity)) {
                requireActivity.openVideo(scanEntity.uri) {
                    callback.onOpenVideoError(scanEntity)
                }
            }
            return
        }
        if (configs.radio) {
            if (configs.crop) {
                cropLauncher.launch(iCrop?.openCrop(requireActivity, configs, scanEntity.uri))
            } else {
                callback.onGalleryResource(scanEntity)
            }
            return
        }
        callback.onPhotoItemClick(scanEntity, position, parentId)
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
            callback.onCameraOpenStatus(CameraStatus.PERMISSION)
            return
        }
        val cameraUri = requireActivity.takePictureUri(configs)
        cameraUri?.let {
            fileUri = it
        } ?: return callback.onCameraOpenStatus(CameraStatus.ERROR)

        if (interceptor.onCustomCamera(fileUri)) return

        fun Fragment.checkCameraStatus(
            uri: CameraUri,
            action: (uri: CameraUri) -> Unit
        ): CameraStatus {
            val intent =
                if (uri.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()))
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                else
                    Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            return intent.resolveActivity(requireActivity().packageManager)?.let {
                action.invoke(uri)
                CameraStatus.SUCCESS
            } ?: CameraStatus.ERROR
        }

        callback.onCameraOpenStatus(fragment.checkCameraStatus(CameraUri(configs.type, fileUri)) {
            openCameraLauncher.launch(it)
        })
    }

    override fun takePictureSuccess() {
        requireActivity.scanFile(fileUri) { onScanGallery(parentId, true) }
        if (configs.takePictureCrop) {
            cropLauncher.launch(iCrop?.openCrop(requireActivity, configs, fileUri))
        }
    }

    override fun takePictureCanceled() {
        fileUri.delete(requireActivity)
        fileUri = Uri.EMPTY
        callback.onCameraCanceled()
    }

    override fun permissionsGranted(type: PermissionCode) {
        when (type) {
            PermissionCode.READ -> onScanGallery(parentId)
            PermissionCode.CAMERA -> openSystemCamera()
        }
    }

    override fun permissionsDenied(type: PermissionCode) {
        callback.onPermissionsDenied(type)
    }

    override fun onScanGallery(parent: Long, isCamera: Boolean) {

        fun Fragment.checkPermissionAndRequestWrite(launcher: ActivityResultLauncher<String>): Boolean {
            return if (!checkReadPermission()) {
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                false
            } else {
                true
            }
        }

        if (!fragment.checkPermissionAndRequestWrite(readPermissionLauncher)) {
            return
        }
        this.parentId = parent
        if (isCamera && galleryAdapter.isNotEmpty) {
            mediaScan.single(requireActivity.findIdByUri(fileUri))
        } else {
            mediaScan.multiple(parent)
        }
    }

    override fun onScanResult(uri: Uri) {
        requireActivity.scanFile(uri) {
            mediaScan.single(requireActivity.findIdByUri(it))
        }
    }

    override fun onUpdateResult(args: ScanArgs) {
        if (!args.isRefresh || selectItem == args.selectList) {
            return
        }
        galleryAdapter.addSelectAll(args.selectList)
        galleryAdapter.updateEntity()
        callback.onRefreshResultChanged()
    }

    override fun notifyItemChanged(position: Int) {
        galleryAdapter.notifyItemChanged(position)
    }

    override fun notifyDataSetChanged() {
        galleryAdapter.notifyDataSetChanged()
    }

    override fun addOnScrollListener(listener: RecyclerView.OnScrollListener) {
        listView.addOnScrollListener(listener)
    }

    override fun removeOnScrollListener(listener: RecyclerView.OnScrollListener) {
        listView.removeOnScrollListener(listener)
    }

    override fun scrollToPosition(position: Int) {
        listView.scrollToPosition(position)
    }

}