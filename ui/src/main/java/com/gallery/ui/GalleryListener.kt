package com.gallery.ui

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.gallery.scan.ScanEntity

interface GalleryListener {
    /**
     * 裁剪成功
     */
    fun onGalleryCropResource(activity: FragmentActivity, uri: Uri) = Unit

    /**
     * 单选不裁剪
     */
    fun onGalleryResource(activity: FragmentActivity, scanEntity: ScanEntity) = Unit

    /**
     * 选择图片
     */
    fun onGalleryResources(activity: FragmentActivity, entities: List<ScanEntity>) = Unit

    /**
     * 取消图片选择
     */
    fun onGalleryCancel(activity: FragmentActivity) = Unit
}