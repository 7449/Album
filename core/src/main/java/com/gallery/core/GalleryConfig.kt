package com.gallery.core

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2

object GalleryConfig {
    /**
     * [GalleryBundle] Bundle key
     * [Bundle.putParcelable]
     */
    const val GALLERY_CONFIG = "galleryStartConfig"

    /**
     * [GalleryBundle] Bundle key
     * [Bundle.putParcelable]
     * 配置文件
     */
    const val PREV_CONFIG = "prevStartConfig"

    /**
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putParcelableArrayList]
     * 全部数据
     */
    const val PREV_START_ALL = "prevStartAllEntity"

    /**
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putParcelableArrayList]
     * 选中的数据
     */
    const val PREV_START_SELECT = "prevStartSelectEntities"

    /**
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putInt]
     * 默认的position,[ViewPager2.setCurrentItem]
     */
    const val PREV_START_POSITION = "prevStartPosition"

    /**
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putParcelableArrayList]
     * 预览页返回选中的数据
     */
    const val PREV_RESULT_SELECT = "prevResultSelectEntities"

    /**
     * [ArrayList<ScanEntity>] Bundle key
     * [Bundle.putBoolean]
     * 预览页返回是否刷新
     */
    const val PREV_RESULT_REFRESH = "prevResultRefresh"
}