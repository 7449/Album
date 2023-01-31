package com.gallery.core.callback

import android.widget.FrameLayout
import com.gallery.core.entity.ScanEntity

interface IGalleryImageLoader {
    /*** 首页图片加载*/
    fun onDisplayHomeGallery(width: Int, height: Int, entity: ScanEntity, container: FrameLayout) {}

    /*** 目录图片加载*/
    fun onDisplayThumbnailsGallery(entity: ScanEntity, container: FrameLayout) {}

    /*** 预览页图片加载*/
    fun onDisplayPrevGallery(entity: ScanEntity, container: FrameLayout) {}
}