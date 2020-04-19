package com.gallery.ui.callback

import com.gallery.scan.ScanEntity

/**
 *
 */
interface IGalleryPrevRootCallback {

    /**
     *  预览页点击确定但是没有选中图片
     */
    fun onGalleryPreRootSelectEmpty()

    /**
     * 选择图片
     */
    fun onGalleryResources(entities: List<ScanEntity>)
}