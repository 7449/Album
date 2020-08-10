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
     * 文件夹扫描Id,如果是预览进入为[]
     * [Bundle.putParcelableArrayList]
     */
    const val PREV_PARENT_ID = "prevParentId"

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

    /**
     * 自定义相机路径
     */
    const val CUSTOM_CAMERA_OUT_PUT_URI = "customCameraOutPutUri"

    /**
     * 点击预览携带的parentId
     * 点击图片进入预览应该是当前文件的parentId
     */
    const val PREV_SELECT_PARENT_ID = -1111L
}