package com.gallery.core.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.gallery.core.Gallery
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.action.GalleryAction
import com.gallery.core.action.ScanInterface
import com.gallery.core.constant.GalleryCameraConst
import com.gallery.core.constant.GalleryConst
import com.gallery.core.constant.GalleryInternalConst
import com.gallery.core.constant.GalleryPermissionConst
import com.gallery.core.ext.*
import com.gallery.core.ui.adapter.GalleryAdapter
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.core.ui.widget.SimpleGridDivider
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanImpl
import com.gallery.scan.ScanView
import com.gallery.scan.SingleMediaScanner
import com.gallery.scan.args.ScanConst
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.gallery_fragment_gallery.*

class ScanFragment : GalleryBaseFragment(), ScanView, ScanInterface, GalleryAdapter.OnGalleryItemClickListener, SingleMediaScanner.SingleScannerListener {

    companion object {
        fun newInstance(galleryBundle: GalleryBundle) = ScanFragment().apply { arguments = Bundle().apply { putParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS, galleryBundle) } }
    }

    private var galleryAction: GalleryAction? = null
    private var singleMediaScanner: SingleMediaScanner? = null
    private lateinit var galleryBundle: GalleryBundle
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var scan: ScanImpl
    private var fileUri: Uri = Uri.EMPTY
    var parent: Long = ScanConst.ALL
    var finderName: String = ""
    var finderList: ArrayList<ScanEntity> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is GalleryAction) {
            galleryAction = parentFragment as GalleryAction
        } else if (context is GalleryAction) {
            galleryAction = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        galleryBundle = bundle.getParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS) ?: GalleryBundle()
        savedInstanceState?.let {
            parent = it.getLong(GalleryInternalConst.TYPE_STATE_PARENT, ScanConst.ALL)
            finderName = it.getString(GalleryInternalConst.TYPE_STATE_FINDER_NAME, "")
            fileUri = it.getParcelable(GalleryInternalConst.TYPE_STATE_IMAGE_URI) ?: Uri.EMPTY
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(GalleryInternalConst.TYPE_STATE_SELECT, selectEntity)
        outState.putLong(GalleryInternalConst.TYPE_STATE_PARENT, parent)
        outState.putString(GalleryInternalConst.TYPE_STATE_FINDER_NAME, finderName)
        outState.putParcelable(GalleryInternalConst.TYPE_STATE_IMAGE_URI, fileUri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scan = ScanImpl(this)
        galleryRootView.setBackgroundColor(ContextCompat.getColor(mActivity, galleryBundle.rootViewBackground))
        val drawable = ContextCompat.getDrawable(mActivity, galleryBundle.photoEmptyDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, galleryBundle.photoEmptyDrawableColor), PorterDuff.Mode.SRC_ATOP)
        galleryEmpty.setImageDrawable(drawable)
        galleryEmpty.setOnClickListener { v ->
            if (Gallery.instance.emptyClickListener == null) {
                startCamera()
                return@setOnClickListener
            }
            Gallery.instance.emptyClickListener?.invoke(v)
        }
        galleryRecyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mActivity, galleryBundle.spanCount)
        galleryRecyclerView.layoutManager = gridLayoutManager
        galleryRecyclerView.addItemDecoration(SimpleGridDivider(galleryBundle.dividerWidth))
        galleryAdapter = GalleryAdapter(mActivity.square(galleryBundle.spanCount), galleryBundle, galleryAction, this)

        val selectList = savedInstanceState?.getParcelableArrayList<ScanEntity>(GalleryInternalConst.TYPE_STATE_SELECT)

        if (!selectList.isNullOrEmpty()) {
            galleryAdapter.multipleList = selectList
        } else if (!galleryBundle.radio && !Gallery.instance.selectList.isNullOrEmpty()) {
            galleryAdapter.multipleList = Gallery.instance.selectList ?: ArrayList()
        }

        galleryRecyclerView.adapter = galleryAdapter
        savedInstanceState?.let { galleryAction?.onGalleryScreenChanged(galleryAdapter.multipleList.size) }
        onScanGallery(parent, isFinder = false, result = false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED ->
                when (requestCode) {
                    GalleryConst.TYPE_PRE_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
                    UCrop.REQUEST_CROP -> Gallery.instance.galleryListener?.onGalleryCropCanceled()
                    GalleryCameraConst.CAMERA_REQUEST_CODE -> Gallery.instance.galleryListener?.onGalleryCameraCanceled()
                }
            UCrop.RESULT_ERROR -> {
                Gallery.instance.galleryListener?.onGalleryUCropError(UCrop.getError(data.orEmpty()))
                if (galleryBundle.cropErrorFinish) {
                    mActivity.finish()
                }
            }
            Activity.RESULT_OK ->
                when (requestCode) {
                    GalleryCameraConst.CAMERA_REQUEST_CODE -> {
                        mActivity.uriToFilePath(fileUri)?.let {
                            scanFile(GalleryConst.TYPE_RESULT_CAMERA, it)
                            if (galleryBundle.cameraCrop) {
                                openCrop(fileUri)
                            }
                        }
                    }
                    UCrop.REQUEST_CROP -> {
                        if (data == null) {
                            Gallery.instance.galleryListener?.onGalleryUCropError(null)
                            return
                        }
                        val parcelable = data.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)
                        if (parcelable == null) {
                            Gallery.instance.galleryListener?.onGalleryUCropError(null)
                            return
                        }
                        Gallery.instance.galleryListener?.onGalleryUCropResources(parcelable)
                        parcelable.path?.let { scanFile(GalleryConst.TYPE_RESULT_CROP, it) }
                        if (galleryBundle.cropFinish) {
                            mActivity.finish()
                        }
                    }
                    GalleryConst.TYPE_PRE_REQUEST_CODE -> onResultPreview(data?.extras.orEmpty())
                }
        }
    }

    override fun scanSuccess(arrayList: ArrayList<ScanEntity>, finderList: ArrayList<ScanEntity>) {
        if (arrayList.isEmpty()) {
            galleryEmpty.show()
            Gallery.instance.galleryListener?.onGalleryEmpty()
            return
        }
        galleryEmpty.hide()
        if (parent == ScanConst.ALL && !galleryBundle.hideCamera) {
            arrayList.add(0, ScanEntity(id = GalleryInternalConst.CAMERA_ID))
        }
        galleryAdapter.addAll(arrayList)
        finderList.find { it.bucketDisplayName == "0" }?.bucketDisplayName = getString(galleryBundle.sdName)
        finderList.find { it.parent == ScanConst.ALL }?.bucketDisplayName = getString(galleryBundle.allName)
        this.finderList.clear()
        this.finderList.addAll(finderList)
    }

    override fun resultSuccess(scanEntity: ScanEntity?) {
        if (scanEntity == null) {
            Gallery.instance.galleryListener?.onGalleryResultCameraError()
        } else {
            galleryAdapter.galleryList.add(1, scanEntity)
            galleryAdapter.notifyDataSetChanged()
            scan.refreshResultFinder(finderList, scanEntity)
        }
    }

    override fun onCameraItemClick(view: View, position: Int, galleryEntity: ScanEntity) {
        if (permissionCamera()) {
            startCamera()
        }
    }

    override fun onPhotoItemClick(view: View, position: Int, galleryEntity: ScanEntity) {
        if (!mActivity.fileExists(galleryEntity.externalUri())) {
            Gallery.instance.galleryListener?.onGalleryFileNotExist()
            return
        }
        if (galleryBundle.scanType == ScanConst.VIDEO) {
            try {
                val openVideo = Intent(Intent.ACTION_VIEW)
                openVideo.setDataAndType(galleryEntity.externalUri(), "video/*")
                startActivity(openVideo)
            } catch (e: Exception) {
                Gallery.instance.galleryListener?.onGalleryVideoPlayError()
            }
            return
        }
        if (galleryBundle.radio) {
            if (galleryBundle.crop) {
                openCrop(galleryEntity.externalUri())
            } else {
                val list = ArrayList<ScanEntity>()
                list.add(galleryEntity)
                Gallery.instance.galleryListener?.onGalleryResources(list)
                if (galleryBundle.selectImageFinish) {
                    mActivity.finish()
                }
            }
            return
        }
        if (galleryBundle.noPreview) {
            return
        }
        galleryAction?.onGalleryItemClick(selectEntity, position, parent)
    }

    override fun onScanGallery(parent: Long, isFinder: Boolean, result: Boolean) {
        if (isFinder) {
            galleryAdapter.removeAll()
        }
        this.parent = parent
        if (!permissionStorage()) {
            return
        }
        if (result && galleryAdapter.galleryList.isNotEmpty()) {
            scan.scanResult(mActivity.getUriId(fileUri))
            return
        }
        scan.scanAll(parent)
    }

    override fun onScanStart() {}

    override fun onScanCompleted(type: Int, path: String?, uri: Uri?) {
        uri?.let {
            mActivity.runOnUiThread {
                if (type == GalleryConst.TYPE_RESULT_CROP) {
                    scan.scanResult(mActivity.getUriId(it))
                } else {
                    fileUri = it
                    onScanGallery(parent, isFinder = false, result = true)
                }
            }
        }
    }

    override fun scanFile(type: Int, path: String) {
        disconnect()
        singleMediaScanner = SingleMediaScanner(mActivity, path, type, this@ScanFragment)
    }

    override fun disconnect() {
        singleMediaScanner?.disconnect()
    }

    override fun startCamera() {
        fileUri = mActivity.getFileUri(mActivity.galleryPathFile(galleryBundle.cameraPath, galleryBundle.cameraName, galleryBundle.scanType))
        val i = openCamera(fileUri, galleryBundle.scanType == ScanConst.VIDEO)
        if (i == GalleryCameraConst.CAMERA_ERROR) {
            Gallery.instance.galleryListener?.onGalleryOpenCameraError()
        }
    }

    override fun openCrop(uri: Uri) {
        val onGalleryCustomCrop = galleryAction?.onGalleryCustomCrop(uri) ?: false
        if (onGalleryCustomCrop) {
            return
        }
        UCrop.of(uri, Uri.fromFile(mActivity.cropPathFile(galleryBundle.uCropPath, galleryBundle.cameraName, galleryBundle.scanType)))
                .withOptions(Gallery.instance.options ?: UCrop.Options())
                .start(mActivity, this)
    }

    override fun onResultPreview(bundle: Bundle) {
        val previewGalleryEntity = bundle.getParcelableArrayList<ScanEntity>(GalleryConst.TYPE_PRE_SELECT)
        val isRefreshUI = bundle.getBoolean(GalleryInternalConst.TYPE_PRE_REFRESH_UI, true)
        val isFinish = bundle.getBoolean(GalleryInternalConst.TYPE_PRE_DONE_FINISH, false)
        if (isFinish) {
            mActivity.finish()
            return
        }
        if (!isRefreshUI || previewGalleryEntity == null || selectEntity == previewGalleryEntity) {
            return
        }
        galleryAdapter.galleryList.mergeEntity(previewGalleryEntity)
        galleryAdapter.multipleList = previewGalleryEntity
        galleryAction?.onPrevChangedCount(selectEntity.size)
    }

    override fun permissionsGranted(type: Int) {
        when (type) {
            GalleryPermissionConst.GALLERY -> onScanGallery(parent, isFinder = false, result = false)
            GalleryPermissionConst.CAMERA -> startCamera()
        }
    }

    override fun permissionsDenied(type: Int) {
        Gallery.instance.galleryListener?.onGalleryPermissionsDenied(type)
        if (galleryBundle.permissionsDeniedFinish) {
            mActivity.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disconnect()
    }

    override fun refreshUI() {
        galleryAdapter.notifyDataSetChanged()
    }

    override val currentScanType: Int
        get() = galleryBundle.scanType

    override val scanContext: FragmentActivity
        get() = mActivity

    override val allGalleryList: ArrayList<ScanEntity>
        get() = galleryAdapter.galleryList

    override val selectEntity: ArrayList<ScanEntity>
        get() = galleryAdapter.multipleList

    override val layoutId: Int = R.layout.gallery_fragment_gallery
}