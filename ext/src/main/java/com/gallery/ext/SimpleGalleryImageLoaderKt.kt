package com.gallery.ext

import android.widget.FrameLayout
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.scan.ScanEntity

class SimpleGalleryImageLoaderKt {

    private lateinit var displayGallery: ((width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) -> Unit)
    private lateinit var displayGalleryThumbnails: ((finderEntity: ScanEntity, container: FrameLayout) -> Unit)
    private lateinit var displayGalleryPreview: ((galleryEntity: ScanEntity, container: FrameLayout) -> Unit)

    fun displayGallery(displayGallery: (width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) -> Unit) {
        this.displayGallery = displayGallery
    }

    fun displayGalleryThumbnails(displayGalleryThumbnails: (finderEntity: ScanEntity, container: FrameLayout) -> Unit) {
        this.displayGalleryThumbnails = displayGalleryThumbnails
    }

    fun displayGalleryPreview(displayGalleryPreview: (galleryEntity: ScanEntity, container: FrameLayout) -> Unit) {
        this.displayGalleryPreview = displayGalleryPreview
    }

    internal fun build(): IGalleryImageLoader {
        return object : IGalleryImageLoader {
            override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) = displayGallery.invoke(width, height, galleryEntity, container)
            override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) = displayGalleryThumbnails.invoke(finderEntity, container)
            override fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) = displayGalleryPreview.invoke(galleryEntity, container)
        }
    }
}
