package com.gallery.ui.callback

import com.gallery.scan.ScanEntity
import com.gallery.ui.activity.GalleryActivity

/**
 *
 */
interface IGalleryRootCallback {

    /**
     * [GalleryActivity]被销毁
     */
    fun onGalleryRootFinish()

    /**
     * [GalleryActivity]back返回
     */
    fun onGalleryRootBackPressed()

    /**
     * 点击预览但是未选择图片
     */
    fun onGalleryPreEmpty()

    /**
     * 点击选择但是未选择图片
     */
    fun onGallerySelectEmpty()

    /**
     * 扫描到的文件目录为空
     */
    fun onGalleryFinderEmpty()

    /**
     * 选择图片
     */
    fun onGalleryResources(entities: List<ScanEntity>)

}