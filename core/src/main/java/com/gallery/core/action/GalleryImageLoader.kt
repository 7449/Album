package com.gallery.core.action

import android.widget.FrameLayout
import com.gallery.scan.ScanEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface GalleryImageLoader {
    /**
     * 首页图片加载
     */
    fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout)

    /**
     * 目录图片加载
     */
    fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout)

    /**
     * 预览页图片加载
     */
    fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout)
}