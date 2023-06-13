package develop.file.gallery.delegate.impl

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.R
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.args.GalleryConfigs.Companion.configs
import develop.file.gallery.args.PermissionType
import develop.file.gallery.callback.IGalleryCallback
import develop.file.gallery.callback.IGalleryImageLoader
import develop.file.gallery.callback.IGalleryInterceptor
import develop.file.gallery.crop.ICrop
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.delegate.adapter.GalleryAdapter
import develop.file.gallery.delegate.args.ScanArgs
import develop.file.gallery.delegate.args.ScanArgs.Companion.scanArgs
import develop.file.gallery.delegate.args.ScanArgs.Companion.toBundle
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.CameraResultContract
import develop.file.gallery.extensions.CameraStatus
import develop.file.gallery.extensions.CameraUri
import develop.file.gallery.extensions.ContextCompat.drawable
import develop.file.gallery.extensions.ContextCompat.openVideo
import develop.file.gallery.extensions.ContextCompat.takePictureUri
import develop.file.gallery.extensions.FileCompat.scanFile
import develop.file.gallery.extensions.PermissionCompat.checkPermissionAndRequestCamera
import develop.file.gallery.extensions.PermissionCompat.checkPermissionAndRequestRead
import develop.file.gallery.extensions.ResultCompat.orEmpty
import develop.file.gallery.extensions.ResultCompat.toScanEntity
import develop.file.gallery.extensions.UriCompat.delete
import develop.file.gallery.extensions.UriCompat.exists
import develop.file.gallery.extensions.UriCompat.id
import develop.file.gallery.extensions.ViewCompat.hide
import develop.file.gallery.extensions.ViewCompat.show
import develop.file.gallery.extensions.ViewCompat.supportsChangeAnimations
import develop.file.gallery.extensions.checkCameraStatus
import develop.file.media.Types
import develop.file.media.args.MediaResult
import develop.file.media.extensions.isAllMediaParent
import develop.file.media.extensions.media
import develop.file.media.impl.MediaImpl
import develop.file.media.impl.file.FileMediaEntity

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
                permissionsGranted(PermissionType.CAMERA)
            } else {
                permissionsDenied(PermissionType.CAMERA)
            }
        }

    private val readPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                permissionsGranted(PermissionType.READ)
            } else {
                permissionsDenied(PermissionType.READ)
            }
        }

    private val configs: GalleryConfigs = fragment.arguments.orEmpty().configs
    private val listView: RecyclerView = rootView.findViewById(R.id.gallery_recyclerview)
    private val emptyView: ImageView = rootView.findViewById(R.id.gallery_empty_view)
    private val galleryAdapter = GalleryAdapter(configs, callback, loader, this)

    private val fileMediaArgs = fragment.media(configs.fileMediaArgs)
    private val mediaScan: MediaImpl<FileMediaEntity> = MediaImpl(fileMediaArgs) {
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
        listView.layoutManager = configs.layoutManager(listView.context)
        listView.supportsChangeAnimations(false)
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

    override fun onScanMultipleSuccess(scanEntities: ArrayList<FileMediaEntity>) {
        if (scanEntities.isEmpty() && parentId.isAllMediaParent) {
            emptyView.show()
            listView.hide()
            callback.onScanMultipleEmpty()
            return
        }
        emptyView.hide()
        listView.show()
        if (parentId.isAllMediaParent && !configs.hideCamera) {
            scanEntities.add(0, FileMediaEntity(parent = GalleryAdapter.CAMERA))
        }
        galleryAdapter.addAll(scanEntities.toScanEntity())
        galleryAdapter.updateEntity()
        callback.onScanMultipleSuccess()
        scrollToPosition(0)
    }

    override fun onScanSingleSuccess(scanEntity: FileMediaEntity?) {
        activity ?: return callback.onScanSingleFailure()
        scanEntity ?: return callback.onScanSingleFailure()
        val toScanEntity = scanEntity.toScanEntity()
        if (parentId.isAllMediaParent || parentId == scanEntity.parent) {
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
        if (!scanEntity.uri.exists(requireActivity)) {
            callback.onClickItemFileNotExist(scanEntity)
            return
        }
        if (configs.isScanVideoMedia) {
            if (!interceptor.onOpenVideo(scanEntity)) {
                requireActivity.openVideo(scanEntity.uri) { callback.onOpenVideoError(scanEntity) }
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
        if (!fragment.checkPermissionAndRequestCamera(cameraPermissionLauncher)) {
            callback.onCameraOpenStatus(CameraStatus.PERMISSION)
            return
        }
        val cameraUri = requireActivity.takePictureUri(configs)
        cameraUri?.let {
            fileUri = it
        } ?: return callback.onCameraOpenStatus(CameraStatus.ERROR)
        if (interceptor.onCustomCamera(fileUri)) return
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

    override fun permissionsGranted(type: PermissionType) {
        when (type) {
            PermissionType.READ -> onScanGallery(parentId)
            PermissionType.CAMERA -> openSystemCamera()
        }
    }

    override fun permissionsDenied(type: PermissionType) {
        callback.onPermissionsDenied(type)
    }

    override fun onScanGallery(parent: Long, isCamera: Boolean) {
        if (!fragment.checkPermissionAndRequestRead(readPermissionLauncher)) {
            return
        }
        this.parentId = parent
        if (isCamera && galleryAdapter.isNotEmpty) {
            mediaScan.single(fileUri.id(requireActivity))
        } else {
            mediaScan.multiple(parent)
        }
    }

    override fun onScanResult(uri: Uri) {
        requireActivity.scanFile(uri) { mediaScan.single(fileUri.id(requireActivity)) }
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