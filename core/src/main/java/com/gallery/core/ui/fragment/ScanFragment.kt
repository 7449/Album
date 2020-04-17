package com.gallery.core.ui.fragment

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.gallery.core.GalleryBundle
import com.gallery.core.PermissionCode
import com.gallery.core.R
import com.gallery.core.ResultType
import com.gallery.core.callback.IGallery
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.ext.*
import com.gallery.core.ui.adapter.GalleryAdapter
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.core.ui.widget.SimpleGridDivider
import com.gallery.scan.*
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.gallery_fragment_gallery.*

class ScanFragment : GalleryBaseFragment(R.layout.gallery_fragment_gallery), ScanView, GalleryAdapter.OnGalleryItemClickListener, IGallery {

    companion object {
        fun newInstance(galleryBundle: GalleryBundle = GalleryBundle()): ScanFragment {
            val scanFragment = ScanFragment()
            val bundle = Bundle()
            bundle.putParcelable(IGallery.GALLERY_START_CONFIG, galleryBundle)
            scanFragment.arguments = bundle
            return scanFragment
        }
    }

    private val galleryCallback by lazy {
        when {
            parentFragment is IGalleryCallback -> parentFragment as IGalleryCallback
            activity is IGalleryCallback -> activity as IGalleryCallback
            else -> throw IllegalArgumentException(context.toString() + " must implement IGalleryCallback")
        }
    }
    private val galleryBundle by lazy {
        bundle.getParcelable(IGallery.GALLERY_START_CONFIG) ?: GalleryBundle()
    }
    private val galleryAdapter by lazy {
        GalleryAdapter(requireActivity().square(galleryBundle.spanCount), galleryBundle, galleryCallback, this)
    }
    private val scan by lazy {
        ScanImpl(this)
    }
    private var fileUri: Uri = Uri.EMPTY
    var parentId: Long = SCAN_ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            parentId = it.getLong(IGallery.GALLERY_START_PARENT_ID, SCAN_ALL)
            fileUri = it.getParcelable(IGallery.GALLERY_START_IMAGE_URL) ?: Uri.EMPTY
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(IGallery.GALLERY_START_SELECT, selectEntities)
        outState.putLong(IGallery.GALLERY_START_PARENT_ID, parentId)
        outState.putParcelable(IGallery.GALLERY_START_IMAGE_URL, fileUri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        galleryRootView.setBackgroundColor(color(galleryBundle.rootViewBackground))
        galleryEmpty.setImageDrawable(drawable(galleryBundle.photoEmptyDrawable))
        galleryEmpty.setOnClickListener { v ->
            if (galleryCallback.onEmptyPhotoClick(v)) {
                startCamera()
            }
        }
        galleryAdapter.addSelectAll(savedInstanceState?.getParcelableArrayList(IGallery.GALLERY_START_SELECT)
                ?: galleryBundle.selectEntities)
        galleryRecyclerView.setHasFixedSize(true)
        galleryRecyclerView.layoutManager = GridLayoutManager(requireActivity(), galleryBundle.spanCount)
        galleryRecyclerView.addItemDecoration(SimpleGridDivider(galleryBundle.dividerWidth))
        galleryRecyclerView.adapter = galleryAdapter
        savedInstanceState?.let { galleryCallback.onScreenChanged(selectEntities.size) }
        onScanGallery(parentId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (galleryCallback.onGalleryFragmentResult(requestCode, resultCode, data)) {
            return
        }
        when (resultCode) {
            Activity.RESULT_CANCELED ->
                when (requestCode) {
                    IGalleryPrev.PREV_START_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
                    UCrop.REQUEST_CROP -> galleryCallback.onUCropCanceled()
                    IGallery.CAMERA_REQUEST_CODE -> galleryCallback.onCameraCanceled()
                }
            UCrop.RESULT_ERROR -> galleryCallback.onUCropError(UCrop.getError(data.orEmpty()))
            Activity.RESULT_OK ->
                when (requestCode) {
                    IGallery.CAMERA_REQUEST_CODE -> {
                        requireActivity().uriToFilePath(fileUri)?.let {
                            scanFile(ResultType.CAMERA, it)
                            if (galleryBundle.cameraCrop) {
                                openCrop(fileUri)
                            }
                        }
                    }
                    UCrop.REQUEST_CROP -> {
                        if (data?.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI) == null) {
                            galleryCallback.onUCropError(null)
                            return
                        }
                        galleryCallback.onUCropResources(data.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI).orEmpty())
                        data.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path?.let { scanFile(ResultType.CROP, it) }
                    }
                    IGalleryPrev.PREV_START_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
                }
        }
    }

    override fun scanSuccess(arrayList: ArrayList<ScanEntity>) {
        if (arrayList.isEmpty()) {
            galleryEmpty.show()
            galleryCallback.onScanSuccessEmpty()
            return
        }
        galleryEmpty.hide()
        if (parentId.isScanAll() && !galleryBundle.hideCamera) {
            arrayList.add(0, ScanEntity(id = IGallery.CAMERA_PARENT_ID))
        }
        galleryAdapter.addAll(arrayList)
        galleryAdapter.updateEntity()
    }

    override fun resultSuccess(scanEntity: ScanEntity?) {
        if (scanEntity == null) {
            galleryCallback.onCameraResultError()
        } else {
            galleryAdapter.addEntity(1, scanEntity)
            galleryAdapter.notifyDataSetChanged()
            galleryCallback.onPhotoResult(scanEntity)
        }
    }

    override fun onCameraItemClick(view: View, position: Int, galleryEntity: ScanEntity) {
        if (permissionCamera()) {
            startCamera()
        }
    }

    override fun onPhotoItemClick(view: View, position: Int, galleryEntity: ScanEntity) {
        if (!requireActivity().uriExists(galleryEntity.externalUri())) {
            galleryCallback.onClickItemFileNotExist()
            return
        }
        if (galleryBundle.scanType == ScanType.VIDEO) {
            try {
                val openVideo = Intent(Intent.ACTION_VIEW)
                openVideo.setDataAndType(galleryEntity.externalUri(), "video/*")
                startActivity(openVideo)
            } catch (e: Exception) {
                galleryCallback.onOpenVideoPlayError()
            }
            return
        }
        if (galleryBundle.radio) {
            if (galleryBundle.crop) {
                openCrop(galleryEntity.externalUri())
            } else {
                galleryCallback.onGalleryResource(ArrayList<ScanEntity>().apply { this.add(galleryEntity) })
            }
            return
        }
        galleryCallback.onPhotoItemClick(selectEntities, position, parentId)
    }

    override fun onScanGallery(parent: Long, isFinder: Boolean, result: Boolean) {
        this.parentId = parent
        if (!permissionStorage()) {
            return
        }
        if (result && galleryAdapter.isNotEmpty) {
            scan.scanResult(requireActivity().findIdByUri(fileUri))
            return
        }
        scan.scanParent(parent)
    }

    override fun scanFile(type: ResultType, path: String) {
        MediaScannerConnection.scanFile(requireContext(), arrayOf(path), null) { _: String?, uri: Uri? ->
            runOnUiThread {
                uri?.let {
                    if (type == ResultType.CROP) {
                        scan.scanResult(requireActivity().findIdByUri(it))
                    } else {
                        fileUri = it
                        onScanGallery(parentId, isFinder = false, result = true)
                    }
                }
            }
        }
    }

    override fun startCamera() {
        fileUri = requireActivity().findUriByFile(requireActivity().galleryPathFile(galleryBundle.cameraPath, galleryBundle.cameraName, galleryBundle.scanType))
        galleryCallback.onOpenCameraStatus(openCamera(fileUri, galleryBundle.scanType == ScanType.VIDEO))
    }

    override fun openCrop(uri: Uri) {
        val onGalleryCustomCrop = galleryCallback.onCustomPhotoCrop(uri)
        if (onGalleryCustomCrop) {
            return
        }
        UCrop.of(uri, Uri.fromFile(requireActivity().cropPathFile(galleryBundle.uCropPath, galleryBundle.cameraName, galleryBundle.scanType)))
                .withOptions(galleryCallback.onUCropOptions())
                .start(requireActivity(), this)
    }

    override fun onResultPreview(bundle: Bundle) {
        val previewGalleryEntity = bundle.getParcelableArrayList<ScanEntity>(IGalleryPrev.PREV_RESULT_SELECT)
        val isRefreshUI = bundle.getBoolean(IGalleryPrev.PREV_RESULT_REFRESH, true)
        if (!isRefreshUI || previewGalleryEntity == null || selectEntities == previewGalleryEntity) {
            return
        }
        galleryAdapter.addSelectAll(previewGalleryEntity)
        galleryAdapter.updateEntity()
        galleryCallback.onChangedPrevCount(selectCount)
    }

    override fun permissionsGranted(type: PermissionCode) {
        when (type) {
            PermissionCode.WRITE -> onScanGallery(parentId, isFinder = false, result = false)
            PermissionCode.READ -> startCamera()
        }
    }

    override fun permissionsDenied(type: PermissionCode) {
        galleryCallback.onPermissionsDenied(type)
    }

    override val currentScanType: ScanType
        get() = galleryBundle.scanType

    override val scanContext: FragmentActivity
        get() = requireActivity()

    override val currentEntities: ArrayList<ScanEntity>
        get() = galleryAdapter.currentList.filter { it.id != IGallery.CAMERA_PARENT_ID } as ArrayList<ScanEntity>

    override val selectEntities: ArrayList<ScanEntity>
        get() = galleryAdapter.currentSelectList

    override val selectEmpty: Boolean
        get() = selectEntities.isEmpty()

    override val selectCount: Int
        get() = selectEntities.size

    override val itemCount: Int
        get() = galleryAdapter.itemCount
}