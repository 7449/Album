package com.gallery.core.action

import android.view.View
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
    fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout): View

    /**
     * 目录图片加载
     */
    fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout): View

    /**
     * 预览页图片加载
     */
    fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout): View
}