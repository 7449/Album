package com.gallery.ui

import android.net.Uri
import com.gallery.scan.args.file.ScanFileEntity

interface GalleryListener {
    /**
     * 裁剪成功
     */
    fun onGalleryCropResource(uri: Uri) = Unit

    /**
     * 单选不裁剪
     */
    fun onGalleryResource(scanEntity: ScanFileEntity) = Unit

    /**
     * 选择图片
     */
    fun onGalleryResources(entities: List<ScanFileEntity>) = Unit

    /**
     * 取消图片选择
     */
    fun onGalleryCancel() = Unit
}