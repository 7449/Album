package com.gallery.core

import android.net.Uri
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
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putBoolean]
     * 预览页返回是否刷新
     */
    internal const val GALLERY_RESULT_REFRESH = "galleryPrevResultRefresh"

    /**
     * 当前拍照之后的[Uri]
     * 有一种情况为横竖屏切换,横屏点进去,然后竖屏拍照返回之后,切换之后会重走生命周期,
     * 所以这里需要保存下当时拍照时的[Uri]
     */
    const val GALLERY_CAMERA_URI = "galleryCameraUri"

    /**
     * 点击预览携带的parentId
     * 点击图片进入预览应该是当前文件的parentId
     */
    const val DEFAULT_PARENT_ID = -1111L
}