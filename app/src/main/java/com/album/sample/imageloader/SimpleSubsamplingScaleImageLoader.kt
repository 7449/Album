package com.album.sample.imageloader

import android.graphics.Bitmap
import android.view.View
import android.widget.FrameLayout
import com.album.sample.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.gallery.core.action.GalleryImageLoader
import com.gallery.core.ext.GalleryImageView
import com.gallery.core.ext.uri
import com.gallery.scan.ScanEntity

class SimpleSubsamplingScaleImageLoader : GalleryImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

    override fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.GalleryImageView()
        Glide.with(container.context).load(galleryEntity.uri()).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.GalleryImageView()
        Glide.with(container.context).load(finderEntity.uri()).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout): View {
        val subsamplingScaleImageView = GallerySubsamplingScaleView(container)
        Glide.with(subsamplingScaleImageView).asBitmap().load(galleryEntity.uri()).apply(requestOptions).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(resource))
            }
        })
        return subsamplingScaleImageView
    }

    private fun GallerySubsamplingScaleView(container: FrameLayout): SubsamplingScaleImageView {
        return if (container.childCount > 0) {
            container.getChildAt(0) as SubsamplingScaleImageView
        } else {
            SubsamplingScaleImageView(container.context)
        }
    }
}
