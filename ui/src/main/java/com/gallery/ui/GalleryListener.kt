package com.gallery.ui

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.gallery.scan.ScanEntity

interface GalleryListener {
    /**
     * 裁剪成功
     */
    fun onGalleryCropResource(activity: FragmentActivity, uri: Uri): Boolean = true

    /**
     * 单选不裁剪
     */
    fun onGalleryResource(activity: FragmentActivity, scanEntity: ScanEntity): Boolean = true

    /**
     * 选择图片
     */
    fun onGalleryResources(activity: FragmentActivity, entities: List<ScanEntity>): Boolean = true
}