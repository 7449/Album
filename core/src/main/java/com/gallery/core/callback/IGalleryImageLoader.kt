package com.gallery.core.callback

import android.widget.FrameLayout
import android.widget.TextView
import com.gallery.scan.args.ScanMinimumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface IGalleryImageLoader {
    /**
     * 首页图片加载
     */
    fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanMinimumEntity, container: FrameLayout, selectView: TextView) {}

    /**
     * 目录图片加载
     */
    fun onDisplayGalleryThumbnails(finderEntity: ScanMinimumEntity, container: FrameLayout) {}

    /**
     * 预览页图片加载
     */
    fun onDisplayGalleryPrev(galleryEntity: ScanMinimumEntity, container: FrameLayout) {}
}