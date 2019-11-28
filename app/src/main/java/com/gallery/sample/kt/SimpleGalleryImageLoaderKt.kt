package com.gallery.sample.kt

import android.view.View
import android.widget.FrameLayout
import com.gallery.core.action.GalleryImageLoader
import com.gallery.scan.ScanEntity

class SimpleGalleryImageLoaderKt {

    private lateinit var displayGallery: ((width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) -> View)
    private lateinit var displayGalleryThumbnails: ((finderEntity: ScanEntity, container: FrameLayout) -> View)
    private lateinit var displayGalleryPreview: ((galleryEntity: ScanEntity, container: FrameLayout) -> View)

    fun displayGallery(displayGallery: (width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) -> View) {
        this.displayGallery = displayGallery
    }

    fun displayGalleryThumbnails(displayGalleryThumbnails: (finderEntity: ScanEntity, container: FrameLayout) -> View) {
        this.displayGalleryThumbnails = displayGalleryThumbnails
    }

    fun displayGalleryPreview(displayGalleryPreview: (galleryEntity: ScanEntity, container: FrameLayout) -> View) {
        this.displayGalleryPreview = displayGalleryPreview
    }

    internal fun build(): GalleryImageLoader {
        return object : GalleryImageLoader {
            override fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout): View = displayGallery.invoke(width, height, galleryEntity, container)
            override fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout): View = displayGalleryThumbnails.invoke(finderEntity, container)
            override fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout): View = displayGalleryPreview.invoke(galleryEntity, container)
        }
    }
}
