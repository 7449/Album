package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.album.sample.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.action.GalleryImageLoader
import com.gallery.core.ext.GalleryImageView
import com.gallery.core.ext.uri
import com.gallery.scan.ScanEntity

class SimpleGlideImageLoader : GalleryImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

    override fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout): View {
        val imageView = container.GalleryImageView()
        Glide.with(container.context).load(galleryEntity.uri()).apply(requestOptions.override(width, height)).into(imageView)
        return imageView
    }

    override fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout): View {
        val imageView = container.GalleryImageView()
        Glide.with(container.context).load(finderEntity.uri()).apply(requestOptions).into(imageView)
        return imageView
    }

    override fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout): View {
        val imageView = container.GalleryImageView()
        Glide.with(container.context).load(galleryEntity.uri()).apply(requestOptions.override(galleryEntity.width, galleryEntity.height)).into(imageView)
        return imageView
    }
}
