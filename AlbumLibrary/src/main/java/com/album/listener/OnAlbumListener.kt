package com.album.listener

import com.album.core.scan.AlbumEntity
import java.io.File

/**
 * @author y
 * @create 2019/2/27
 */
interface OnAlbumListener {

    /**
     * fragment 依赖的activity或者dialog被销毁
     */
    fun onAlbumContainerFinish()

    /**
     * fragment 依赖的activity back返回
     */
    fun onAlbumContainerBackPressed()

    /**
     * 扫描到的文件目录为空
     */
    fun onAlbumContainerFinderEmpty()

    /**
     * fragment 依赖的activity或者dialog 预览页点击确定但是没有选中图片
     */
    fun onAlbumContainerPreviewSelectEmpty()

    /**
     * 选择图片
     */
    fun onAlbumResources(list: List<AlbumEntity>)

    /**
     * 获取预览但是未选择图片
     */
    fun onAlbumPreviewEmpty()

    /**
     * 权限被拒
     */
    fun onAlbumPermissionsDenied(type: Int)

    /**
     * 预览滑动图片已经被删
     */
    fun onAlbumPreviewFileNotExist()

    /**
     * 点击选择但是未选择图片 [onAlbumResources]
     */
    fun onAlbumSelectEmpty()

    /**
     * 点击图片时图片已经被删
     */
    fun onAlbumFileNotExist()

    /**
     * 多选图片时图片已经被删
     */
    fun onAlbumCheckFileNotExist()

    /**
     * 取消裁剪
     */
    fun onAlbumCropCanceled()

    /**
     * 取消拍照
     */
    fun onAlbumCameraCanceled()

    /**
     * 拍照完成之后选择取消拍照
     */
    fun onAlbumCameraSuccessCanceled()

    /**
     * 裁剪错误
     */
    fun onAlbumUCropError(data: Throwable?)

    /**
     * 裁剪成功
     */
    fun onAlbumUCropResources(cropFile: File)

    /**
     * 已达多选最大数
     */
    fun onAlbumMaxCount()

    /**
     * 打开相机错误
     */
    fun onAlbumOpenCameraError()

    /**
     * 没有扫描到图片
     */
    fun onAlbumEmpty()

    /**
     * 没有更多图片，图片已经全部加载
     */
    fun onAlbumNoMore()

    /**
     * 拍照返回错误
     */
    fun onAlbumResultCameraError()

    /**
     * 视频播放错误
     */
    fun onAlbumVideoPlayError()

    /**
     * 每次checkbox选择时调用
     */
    fun onAlbumCheckBox(count: Int, maxCount: Int)
}

open class SimpleOnAlbumListener : OnAlbumListener {
    override fun onAlbumCameraSuccessCanceled() {}
    override fun onAlbumContainerFinish() {}
    override fun onAlbumContainerBackPressed() {}
    override fun onAlbumContainerFinderEmpty() {}
    override fun onAlbumContainerPreviewSelectEmpty() {}
    override fun onAlbumResources(list: List<AlbumEntity>) {}
    override fun onAlbumResultCameraError() {}
    override fun onAlbumPermissionsDenied(type: Int) {}
    override fun onAlbumPreviewFileNotExist() {}
    override fun onAlbumPreviewEmpty() {}
    override fun onAlbumSelectEmpty() {}
    override fun onAlbumFileNotExist() {}
    override fun onAlbumCheckFileNotExist() {}
    override fun onAlbumCropCanceled() {}
    override fun onAlbumCameraCanceled() {}
    override fun onAlbumUCropError(data: Throwable?) {}
    override fun onAlbumUCropResources(cropFile: File) {}
    override fun onAlbumMaxCount() {}
    override fun onAlbumOpenCameraError() {}
    override fun onAlbumEmpty() {}
    override fun onAlbumNoMore() {}
    override fun onAlbumVideoPlayError() {}
    override fun onAlbumCheckBox(count: Int, maxCount: Int) {}
}

class SimpleOnAlbumListenerKt {

    private var onAlbumCameraSuccessCanceled: (() -> Unit)? = null
    private var onAlbumContainerFinish: (() -> Unit)? = null
    private var onAlbumContainerBackPressed: (() -> Unit)? = null
    private var onAlbumContainerFinderEmpty: (() -> Unit)? = null
    private var onAlbumContainerPreviewSelectEmpty: (() -> Unit)? = null
    private var onAlbumResources: ((list: List<AlbumEntity>) -> Unit)? = null
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
    private var onAlbumNoMore: (() -> Unit)? = null
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

    fun onAlbumResources(onAlbumResources: (list: List<AlbumEntity>) -> Unit) {
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

    fun onAlbumNoMore(onAlbumNoMore: () -> Unit) {
        this.onAlbumNoMore = onAlbumNoMore
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

            override fun onAlbumContainerPreviewSelectEmpty() {
                onAlbumContainerPreviewSelectEmpty?.invoke()
            }

            override fun onAlbumResources(list: List<AlbumEntity>) {
                onAlbumResources?.invoke(list)
            }

            override fun onAlbumPreviewEmpty() {
                onAlbumPreviewEmpty?.invoke()
            }

            override fun onAlbumPermissionsDenied(type: Int) {
                onAlbumPermissionsDenied?.invoke(type)
            }

            override fun onAlbumPreviewFileNotExist() {
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

            override fun onAlbumCameraSuccessCanceled() {
                onAlbumCameraSuccessCanceled?.toString()
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

            override fun onAlbumNoMore() {
                onAlbumNoMore?.invoke()
            }

            override fun onAlbumResultCameraError() {
                onAlbumResultCameraError?.invoke()
            }

            override fun onAlbumVideoPlayError() {
                onAlbumVideoPlayError?.invoke()
            }

            override fun onAlbumCheckBox(count: Int, maxCount: Int) {
                onAlbumCheckBox?.invoke(count, maxCount)
            }
        }
    }
}