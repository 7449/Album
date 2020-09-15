package com.gallery.ui

import android.net.Uri
import com.gallery.core.delegate.ScanEntity

interface GalleryListener {
    /**
     * 裁剪成功
     */
    fun onGalleryCropResource(uri: Uri) {}

    /**
     * 单选不裁剪
     */
    fun onGalleryResource(scanEntity: ScanEntity) {}

    /**
     * 选择图片
     */
    fun onGalleryResources(entities: List<ScanEntity>) {}

    /**
     * 取消图片选择
     */
    fun onGalleryCancel() {}
}