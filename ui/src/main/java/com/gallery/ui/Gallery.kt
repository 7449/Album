package com.gallery.ui

import com.gallery.core.callback.IGalleryImageLoader

class Gallery private constructor() {

    /**
     * 图片加载框架
     */
    var galleryImageLoader: IGalleryImageLoader? = null

    /**
     * 回调
     */
    var galleryListener: OnGalleryListener? = null

    companion object {

        val instance by lazy { Gallery() }

        fun destroy() = instance.apply {
            galleryImageLoader = null
            galleryListener = null
        }
    }
}