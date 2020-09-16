package com.gallery.core.callback

import android.widget.FrameLayout
import android.widget.TextView
import com.gallery.core.delegate.ScanEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface IGalleryImageLoader {
    /**
     * 首页图片加载
     */
    fun onDisplayGallery(width: Int, height: Int, scanEntity: ScanEntity, container: FrameLayout, checkBox: TextView) {}

    /**
     * 目录图片加载
     */
    fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {}

    /**
     * 预览页图片加载
     */
    fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout) {}
}