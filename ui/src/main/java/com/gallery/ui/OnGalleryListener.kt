package com.gallery.ui

import com.gallery.scan.ScanEntity

/**
 * @author y
 * @create 2019/2/27
 */
@Deprecated("@see [GalleryActivity] [PreActivity]")
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
     *  预览页点击确定但是没有选中图片
     */
    fun onGalleryContainerPreSelectEmpty()

    /**
     * 选择图片
     */
    fun onGalleryResources(entities: List<ScanEntity>)

    /**
     * 点击预览但是未选择图片
     */
    fun onGalleryPreEmpty()

    /**
     * 点击选择但是未选择图片
     */
    fun onGallerySelectEmpty()

    /**
     * 预览页滑动但图片不存在
     */
    fun onGalleryPreFileNotExist()

}
