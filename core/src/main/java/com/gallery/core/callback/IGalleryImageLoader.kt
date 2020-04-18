package com.gallery.core.callback

import android.widget.FrameLayout
import com.gallery.scan.ScanEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface IGalleryImageLoader {
    /**
     * 首页图片加载
     */
    fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) {}

    /**
     * 目录图片加载
     */
    fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {}

    /**
     * 预览页图片加载
     */
    fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) {}
}