package com.album.sample.kt

import com.album.core.action.OnAlbumListener
import com.gallery.scan.ScanEntity
import java.io.File


class SimpleOnAlbumListenerKt {

    private var onAlbumCameraSuccessCanceled: (() -> Unit)? = null
    private var onAlbumContainerFinish: (() -> Unit)? = null
    private var onAlbumContainerBackPressed: (() -> Unit)? = null
    private var onAlbumContainerFinderEmpty: (() -> Unit)? = null
    private var onAlbumContainerPreviewSelectEmpty: (() -> Unit)? = null
    private var onAlbumResources: ((list: List<ScanEntity>) -> Unit)? = null
    private var onAlbumResultCameraError: (() -> Unit)? = null
    private var onAlbumPreviewEmpty: (() -> Unit)? = null
    private var onAlbumPermissionsDenied: ((type: Int) -> Unit)? = null
    private var onAlbumPreviewFileNotExist: (() -> Unit)? = null
    private var onAlbumSelectEmpty: (() -> Unit)? = null
    private var onAlbumFileNotExist: (() -> Unit)? = null
    private var onAlbumCheckFileNotExist: (() -> Unit)? = null
    private var onAlbumCropCanceled: (() -> Unit)? = null
    private var onAlbumCameraCanceled: (() -> Unit)? = null
    private var onAlbumUCropError: ((data: Throwable?) -> Unit)? = null
    private var onAlbumUCropResources: ((cropFile: File) -> Unit)? = null
    private var onAlbumMaxCount: (() -> Unit)? = null
    private var onAlbumOpenCameraError: (() -> Unit)? = null
    private var onAlbumEmpty: (() -> Unit)? = null
    private var onAlbumVideoPlayError: (() -> Unit)? = null
    private var onAlbumCheckBox: ((count: Int, maxCount: Int) -> Unit)? = null

    fun onAlbumCameraSuccessCanceled(onAlbumCameraSuccessCanceled: () -> Unit) {
        this.onAlbumCameraSuccessCanceled = onAlbumCameraSuccessCanceled
    }

    fun onAlbumContainerFinish(onAlbumContainerFinish: () -> Unit) {
        this.onAlbumContainerFinish = onAlbumContainerFinish
    }

    fun onAlbumContainerBackPressed(onAlbumContainerBackPressed: () -> Unit) {
        this.onAlbumContainerBackPressed = onAlbumContainerBackPressed
    }

    fun onAlbumContainerFinderEmpty(onAlbumContainerFinderEmpty: () -> Unit) {
        this.onAlbumContainerFinderEmpty = onAlbumContainerFinderEmpty
    }

    fun onAlbumContainerPreviewSelectEmpty(onAlbumContainerPreviewSelectEmpty: () -> Unit) {
        this.onAlbumContainerPreviewSelectEmpty = onAlbumContainerPreviewSelectEmpty
    }

    fun onAlbumResources(onAlbumResources: (list: List<ScanEntity>) -> Unit) {
        this.onAlbumResources = onAlbumResources
    }

    fun onAlbumResultCameraError(onAlbumResultCameraError: () -> Unit) {
        this.onAlbumResultCameraError = onAlbumResultCameraError
    }

    fun onAlbumPreviewEmpty(onAlbumPreviewEmpty: () -> Unit) {
        this.onAlbumPreviewEmpty = onAlbumPreviewEmpty
    }

    fun onAlbumPermissionsDenied(onAlbumPermissionsDenied: (type: Int) -> Unit) {
        this.onAlbumPermissionsDenied = onAlbumPermissionsDenied
    }

    fun onAlbumPreviewFileNotExist(onAlbumPreviewFileNotExist: () -> Unit) {
        this.onAlbumPreviewFileNotExist = onAlbumPreviewFileNotExist
    }

    fun onAlbumSelectEmpty(onAlbumSelectEmpty: () -> Unit) {
        this.onAlbumSelectEmpty = onAlbumSelectEmpty
    }

    fun onAlbumFileNotExist(onAlbumFileNotExist: () -> Unit) {
        this.onAlbumFileNotExist = onAlbumFileNotExist
    }

    fun onAlbumCheckFileNotExist(onAlbumCheckFileNotExist: () -> Unit) {
        this.onAlbumCheckFileNotExist = onAlbumCheckFileNotExist
    }

    fun onAlbumCropCanceled(onAlbumCropCanceled: () -> Unit) {
        this.onAlbumCropCanceled = onAlbumCropCanceled
    }

    fun onAlbumCameraCanceled(onAlbumCameraCanceled: () -> Unit) {
        this.onAlbumCameraCanceled = onAlbumCameraCanceled
    }

    fun onAlbumUCropError(onAlbumUCropError: (data: Throwable?) -> Unit) {
        this.onAlbumUCropError = onAlbumUCropError
    }

    fun onAlbumUCropResources(onAlbumUCropResources: (file: File) -> Unit) {
        this.onAlbumUCropResources = onAlbumUCropResources
    }

    fun onAlbumMaxCount(onAlbumMaxCount: () -> Unit) {
        this.onAlbumMaxCount = onAlbumMaxCount
    }

    fun onAlbumOpenCameraError(onAlbumOpenCameraError: () -> Unit) {
        this.onAlbumOpenCameraError = onAlbumOpenCameraError
    }

    fun onAlbumEmpty(onAlbumEmpty: () -> Unit) {
        this.onAlbumEmpty = onAlbumEmpty
    }

    fun onAlbumVideoPlayError(onAlbumVideoPlayError: () -> Unit) {
        this.onAlbumVideoPlayError = onAlbumVideoPlayError
    }

    fun onAlbumCheckBox(onAlbumCheckBox: (count: Int, maxCount: Int) -> Unit) {
        this.onAlbumCheckBox = onAlbumCheckBox
    }

    internal fun build(): OnAlbumListener {
        return object : OnAlbumListener {
            override fun onAlbumContainerFinish() {
                onAlbumContainerFinish?.invoke()
            }

            override fun onAlbumContainerBackPressed() {
                onAlbumContainerBackPressed?.invoke()
            }

            override fun onAlbumContainerFinderEmpty() {
                onAlbumContainerFinderEmpty?.invoke()
            }

            override fun onAlbumContainerPreSelectEmpty() {
                onAlbumContainerPreviewSelectEmpty?.invoke()
            }

            override fun onAlbumResources(list: List<ScanEntity>) {
                onAlbumResources?.invoke(list)
            }

            override fun onAlbumPreEmpty() {
                onAlbumPreviewEmpty?.invoke()
            }

            override fun onAlbumPermissionsDenied(type: Int) {
                onAlbumPermissionsDenied?.invoke(type)
            }

            override fun onAlbumPreFileNotExist() {
                onAlbumPreviewFileNotExist?.invoke()
            }

            override fun onAlbumSelectEmpty() {
                onAlbumSelectEmpty?.invoke()
            }

            override fun onAlbumFileNotExist() {
                onAlbumFileNotExist?.invoke()
            }

            override fun onAlbumCheckFileNotExist() {
                onAlbumCheckFileNotExist?.invoke()
            }

            override fun onAlbumCropCanceled() {
                onAlbumCropCanceled?.invoke()
            }

            override fun onAlbumCameraCanceled() {
                onAlbumCameraCanceled?.invoke()
            }

            override fun onAlbumUCropError(data: Throwable?) {
                onAlbumUCropError?.invoke(data)
            }

            override fun onAlbumUCropResources(cropFile: File) {
                onAlbumUCropResources?.invoke(cropFile)
            }

            override fun onAlbumMaxCount() {
                onAlbumMaxCount?.invoke()
            }

            override fun onAlbumOpenCameraError() {
                onAlbumOpenCameraError?.invoke()
            }

            override fun onAlbumEmpty() {
                onAlbumEmpty?.invoke()
            }

            override fun onAlbumResultCameraError() {
                onAlbumResultCameraError?.invoke()
            }

            override fun onAlbumVideoPlayError() {
                onAlbumVideoPlayError?.invoke()
            }

            override fun onAlbumCheckBox(selectCount: Int, maxCount: Int) {
                onAlbumCheckBox?.invoke(selectCount, maxCount)
            }
        }
    }
}