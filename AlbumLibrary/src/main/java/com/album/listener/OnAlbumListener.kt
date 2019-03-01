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
    fun onVideoPlayError()

    /**
     * 每次checkbox选择时调用
     */
    fun onCheckBoxAlbum(count: Int, maxCount: Int)
}