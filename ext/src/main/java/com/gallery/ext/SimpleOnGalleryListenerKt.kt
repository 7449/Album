package com.gallery.ext

import android.net.Uri
import com.gallery.scan.ScanEntity

class SimpleOnGalleryListenerKt {

    private var onGalleryCameraSuccessCanceled: (() -> Unit)? = null
    private var onGalleryContainerFinish: (() -> Unit)? = null
    private var onGalleryContainerBackPressed: (() -> Unit)? = null
    private var onGalleryContainerFinderEmpty: (() -> Unit)? = null
    private var onGalleryContainerPreviewSelectEmpty: (() -> Unit)? = null
    private var onGalleryResources: ((list: List<ScanEntity>) -> Unit)? = null
    private var onGalleryResultCameraError: (() -> Unit)? = null
    private var onGalleryPreviewEmpty: (() -> Unit)? = null
    private var onGalleryPermissionsDenied: ((type: Int) -> Unit)? = null
    private var onGalleryPreviewFileNotExist: (() -> Unit)? = null
    private var onGallerySelectEmpty: (() -> Unit)? = null
    private var onGalleryFileNotExist: (() -> Unit)? = null
    private var onGalleryCheckFileNotExist: (() -> Unit)? = null
    private var onGalleryCropCanceled: (() -> Unit)? = null
    private var onGalleryCameraCanceled: (() -> Unit)? = null
    private var onGalleryUCropError: ((data: Throwable?) -> Unit)? = null
    private var onGalleryUCropResources: ((uri: Uri) -> Unit)? = null
    private var onGalleryMaxCount: (() -> Unit)? = null
    private var onGalleryOpenCameraError: (() -> Unit)? = null
    private var onGalleryEmpty: (() -> Unit)? = null
    private var onGalleryVideoPlayError: (() -> Unit)? = null
    private var onGalleryCheckBox: ((count: Int, maxCount: Int) -> Unit)? = null

    fun onGalleryCameraSuccessCanceled(onGalleryCameraSuccessCanceled: () -> Unit) {
        this.onGalleryCameraSuccessCanceled = onGalleryCameraSuccessCanceled
    }

    fun onGalleryContainerFinish(onGalleryContainerFinish: () -> Unit) {
        this.onGalleryContainerFinish = onGalleryContainerFinish
    }

    fun onGalleryContainerBackPressed(onGalleryContainerBackPressed: () -> Unit) {
        this.onGalleryContainerBackPressed = onGalleryContainerBackPressed
    }

    fun onGalleryContainerFinderEmpty(onGalleryContainerFinderEmpty: () -> Unit) {
        this.onGalleryContainerFinderEmpty = onGalleryContainerFinderEmpty
    }

    fun onGalleryContainerPreviewSelectEmpty(onGalleryContainerPreviewSelectEmpty: () -> Unit) {
        this.onGalleryContainerPreviewSelectEmpty = onGalleryContainerPreviewSelectEmpty
    }

    fun onGalleryResources(onGalleryResources: (list: List<ScanEntity>) -> Unit) {
        this.onGalleryResources = onGalleryResources
    }

    fun onGalleryResultCameraError(onGalleryResultCameraError: () -> Unit) {
        this.onGalleryResultCameraError = onGalleryResultCameraError
    }

    fun onGalleryPreviewEmpty(onGalleryPreviewEmpty: () -> Unit) {
        this.onGalleryPreviewEmpty = onGalleryPreviewEmpty
    }

    fun onGalleryPermissionsDenied(onGalleryPermissionsDenied: (type: Int) -> Unit) {
        this.onGalleryPermissionsDenied = onGalleryPermissionsDenied
    }

    fun onGalleryPreviewFileNotExist(onGalleryPreviewFileNotExist: () -> Unit) {
        this.onGalleryPreviewFileNotExist = onGalleryPreviewFileNotExist
    }

    fun onGallerySelectEmpty(onGallerySelectEmpty: () -> Unit) {
        this.onGallerySelectEmpty = onGallerySelectEmpty
    }

    fun onGalleryFileNotExist(onGalleryFileNotExist: () -> Unit) {
        this.onGalleryFileNotExist = onGalleryFileNotExist
    }

    fun onGalleryCheckFileNotExist(onGalleryCheckFileNotExist: () -> Unit) {
        this.onGalleryCheckFileNotExist = onGalleryCheckFileNotExist
    }

    fun onGalleryCropCanceled(onGalleryCropCanceled: () -> Unit) {
        this.onGalleryCropCanceled = onGalleryCropCanceled
    }

    fun onGalleryCameraCanceled(onGalleryCameraCanceled: () -> Unit) {
        this.onGalleryCameraCanceled = onGalleryCameraCanceled
    }

    fun onGalleryUCropError(onGalleryUCropError: (data: Throwable?) -> Unit) {
        this.onGalleryUCropError = onGalleryUCropError
    }

    fun onGalleryUCropResources(onGalleryUCropResources: (uri: Uri) -> Unit) {
        this.onGalleryUCropResources = onGalleryUCropResources
    }

    fun onGalleryMaxCount(onGalleryMaxCount: () -> Unit) {
        this.onGalleryMaxCount = onGalleryMaxCount
    }

    fun onGalleryOpenCameraError(onGalleryOpenCameraError: () -> Unit) {
        this.onGalleryOpenCameraError = onGalleryOpenCameraError
    }

    fun onGalleryEmpty(onGalleryEmpty: () -> Unit) {
        this.onGalleryEmpty = onGalleryEmpty
    }

    fun onGalleryVideoPlayError(onGalleryVideoPlayError: () -> Unit) {
        this.onGalleryVideoPlayError = onGalleryVideoPlayError
    }

    fun onGalleryCheckBox(onGalleryCheckBox: (count: Int, maxCount: Int) -> Unit) {
        this.onGalleryCheckBox = onGalleryCheckBox
    }

    internal fun build(): com.gallery.ui.OnGalleryListener {
        return object : com.gallery.ui.OnGalleryListener {
            override fun onGalleryContainerFinish() {
                onGalleryContainerFinish?.invoke()
            }

            override fun onGalleryContainerBackPressed() {
                onGalleryContainerBackPressed?.invoke()
            }

            override fun onGalleryContainerFinderEmpty() {
                onGalleryContainerFinderEmpty?.invoke()
            }

            override fun onGalleryContainerPreSelectEmpty() {
                onGalleryContainerPreviewSelectEmpty?.invoke()
            }

            override fun onGalleryResources(list: List<ScanEntity>) {
                onGalleryResources?.invoke(list)
            }

            override fun onGalleryPreEmpty() {
                onGalleryPreviewEmpty?.invoke()
            }

            override fun onGalleryPermissionsDenied(type: Int) {
                onGalleryPermissionsDenied?.invoke(type)
            }

            override fun onGalleryPreFileNotExist() {
                onGalleryPreviewFileNotExist?.invoke()
            }

            override fun onGallerySelectEmpty() {
                onGallerySelectEmpty?.invoke()
            }

            override fun onGalleryFileNotExist() {
                onGalleryFileNotExist?.invoke()
            }

            override fun onGalleryCheckFileNotExist() {
                onGalleryCheckFileNotExist?.invoke()
            }

            override fun onGalleryCropCanceled() {
                onGalleryCropCanceled?.invoke()
            }

            override fun onGalleryCameraCanceled() {
                onGalleryCameraCanceled?.invoke()
            }

            override fun onGalleryUCropError(data: Throwable?) {
                onGalleryUCropError?.invoke(data)
            }

            override fun onGalleryUCropResources(uri: Uri) {
                onGalleryUCropResources?.invoke(uri)
            }

            override fun onGalleryMaxCount() {
                onGalleryMaxCount?.invoke()
            }

            override fun onGalleryOpenCameraError() {
                onGalleryOpenCameraError?.invoke()
            }

            override fun onGalleryEmpty() {
                onGalleryEmpty?.invoke()
            }

            override fun onGalleryResultCameraError() {
                onGalleryResultCameraError?.invoke()
            }

            override fun onGalleryVideoPlayError() {
                onGalleryVideoPlayError?.invoke()
            }

            override fun onGalleryCheckBox(selectCount: Int, maxCount: Int) {
                onGalleryCheckBox?.invoke(selectCount, maxCount)
            }
        }
    }
}