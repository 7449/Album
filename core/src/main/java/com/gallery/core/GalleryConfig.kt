package com.gallery.core

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

object GalleryConfig {
    /**
     * [GalleryBundle] Bundle key
     * [Bundle.putParcelable]
     */
    const val GALLERY_CONFIG = "galleryConfig"

    /**
     * 图库选中的文件
     * [ArrayList<ScanEntity>] Bundle key
     */
    const val GALLERY_SELECT = "gallerySelect"

    /**
     * 预览页单独扫描
     */
    const val GALLERY_RESULT_SCAN_ALONE = "galleryPrevScanAlone"

    /**
     *  parentId Bundle key,如果是预览进入为[DEFAULT_PARENT_ID]
     * [Bundle.putParcelableArrayList]
     */
    const val GALLERY_PARENT_ID = "galleryParentId"

    /**
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putInt]
     * 默认的position,[ViewPager2.setCurrentItem]
     */
    const val GALLERY_POSITION = "galleryInitPosition"

    /**
     * 点击预览携带的parentId
     * 点击图片进入预览应该是当前文件的parentId
     */
    const val DEFAULT_PARENT_ID = -1111L

    /**
     * 预览页单独扫描默认值
     */
    const val DEFAULT_SCAN_ALONE_TYPE = -1
}