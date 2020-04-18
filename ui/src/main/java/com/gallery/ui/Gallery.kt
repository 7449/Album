package com.gallery.ui

class Gallery private constructor() {

    /**
     * 回调
     */
    var galleryListener: OnGalleryListener? = null

    companion object {

        val instance by lazy { Gallery() }

        fun destroy() = instance.apply {
            galleryListener = null
        }
    }
}