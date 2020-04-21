package com.gallery.ui

import android.net.Uri
import com.gallery.scan.ScanEntity
import com.gallery.ui.activity.GalleryActivity

@Deprecated("recommended CustomGallery : GalleryActivity()")
interface GalleryCallback {
    /**
     * 裁剪成功
     */
    fun onGalleryCropResource(activity: GalleryActivity, uri: Uri)

    /**
     * 单选不裁剪
     */
    fun onGalleryResource(activity: GalleryActivity, scanEntity: ScanEntity)

    /**
     * 选择图片
     */
    fun onGalleryResources(activity: GalleryActivity, entities: List<ScanEntity>)
}