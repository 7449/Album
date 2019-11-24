package com.album.core.action

import com.album.scan.scan.AlbumEntity
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
    fun onAlbumContainerPreSelectEmpty()

    /**
     * 选择图片
     */
    fun onAlbumResources(list: List<AlbumEntity>)

    /**
     * 点击预览但是未选择图片
     */
    fun onAlbumPreEmpty()

    /**
     * 点击选择但是未选择图片
     */
    fun onAlbumSelectEmpty()

    /**
     * 权限被拒
     */
    fun onAlbumPermissionsDenied(type: Int)

    /**
     * 预览页滑动但图片不存在
     */
    fun onAlbumPreFileNotExist()

    /**
     * 点击图片时图片不存在
     */
    fun onAlbumFileNotExist()

    /**
     * 多选图片时图片不存在
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
     * 拍照返回错误
     */
    fun onAlbumResultCameraError()

    /**
     * 视频播放错误
     */
    fun onAlbumVideoPlayError()

    /**
     * CheckBox选择时调用
     */
    fun onAlbumCheckBox(selectCount: Int, maxCount: Int)
}
