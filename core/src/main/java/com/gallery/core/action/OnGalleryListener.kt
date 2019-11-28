package com.gallery.core.action

import com.gallery.scan.ScanEntity
import java.io.File

/**
 * @author y
 * @create 2019/2/27
 */
interface OnGalleryListener {
    /**
     * fragment 依赖的activity或者dialog被销毁
     */
    fun onGalleryContainerFinish()

    /**
     * fragment 依赖的activity back返回
     */
    fun onGalleryContainerBackPressed()

    /**
     * 扫描到的文件目录为空
     */
    fun onGalleryContainerFinderEmpty()

    /**
     * fragment 依赖的activity或者dialog 预览页点击确定但是没有选中图片
     */
    fun onGalleryContainerPreSelectEmpty()

    /**
     * 选择图片
     */
    fun onGalleryResources(list: List<ScanEntity>)

    /**
     * 点击预览但是未选择图片
     */
    fun onGalleryPreEmpty()

    /**
     * 点击选择但是未选择图片
     */
    fun onGallerySelectEmpty()

    /**
     * 权限被拒
     */
    fun onGalleryPermissionsDenied(type: Int)

    /**
     * 预览页滑动但图片不存在
     */
    fun onGalleryPreFileNotExist()

    /**
     * 点击图片时图片不存在
     */
    fun onGalleryFileNotExist()

    /**
     * 多选图片时图片不存在
     */
    fun onGalleryCheckFileNotExist()

    /**
     * 取消裁剪
     */
    fun onGalleryCropCanceled()

    /**
     * 取消拍照
     */
    fun onGalleryCameraCanceled()

    /**
     * 裁剪错误
     */
    fun onGalleryUCropError(data: Throwable?)

    /**
     * 裁剪成功
     */
    fun onGalleryUCropResources(cropFile: File)

    /**
     * 已达多选最大数
     */
    fun onGalleryMaxCount()

    /**
     * 打开相机错误
     */
    fun onGalleryOpenCameraError()

    /**
     * 没有扫描到图片
     */
    fun onGalleryEmpty()

    /**
     * 拍照返回错误
     */
    fun onGalleryResultCameraError()

    /**
     * 视频播放错误
     */
    fun onGalleryVideoPlayError()

    /**
     * CheckBox选择时调用
     */
    fun onGalleryCheckBox(selectCount: Int, maxCount: Int)
}
