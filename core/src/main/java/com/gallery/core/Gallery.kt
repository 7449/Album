package com.gallery.core

import android.view.View
import com.gallery.core.action.GalleryImageLoader
import com.gallery.core.action.OnGalleryListener
import com.gallery.core.ui.base.GalleryBaseFragment
import com.gallery.scan.ScanEntity
import com.yalantis.ucrop.UCrop

class Gallery private constructor() {

    /**
     * UCrop setting
     */
    var options: UCrop.Options? = null

    /**
     * 图片加载框架
     */
    var galleryImageLoader: GalleryImageLoader? = null

    /**
     * 回调
     */
    var galleryListener: OnGalleryListener? = null

    /**
     * 自定义相机
     */
    var customCameraListener: ((fragment: GalleryBaseFragment) -> Unit)? = null

    /**
     * 占位符自定义点击
     */
    var emptyClickListener: ((view: View) -> Unit)? = null

    /**
     * 默认初始化选中数据
     */
    var selectList: ArrayList<ScanEntity>? = null

    companion object {

        val instance by lazy { Gallery() }

        fun destroy() = instance.apply {
            options = null
            galleryImageLoader = null
            galleryListener = null
            customCameraListener = null
            emptyClickListener = null
            selectList = null
        }
    }
}