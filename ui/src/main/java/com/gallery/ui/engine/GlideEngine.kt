package com.gallery.ui.engine

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.scan.types.externalUriExpand
import com.gallery.ui.R

internal fun FrameLayout.displayGallery(width: Int, height: Int, galleryEntity: ScanMinimumEntity) {
    removeAllViews()
    val imageView = GalleryImageView(context)
    Glide.with(context).load(galleryEntity.externalUriExpand).apply(RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop().override(width, height)).into(imageView)
    addView(imageView, FrameLayout.LayoutParams(width, height))
}

internal fun FrameLayout.displayGalleryThumbnails(finderEntity: ScanMinimumEntity) {
    removeAllViews()
    val imageView = GalleryImageView(context)
    Glide.with(context).load(finderEntity.externalUriExpand).apply(RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()).into(imageView)
    addView(imageView)
}

internal fun FrameLayout.displayGalleryPrev(scanEntity: ScanMinimumEntity) {
    removeAllViews()
    val imageView: GalleryImageView = GalleryImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
            gravity = Gravity.CENTER
        }
    }
    Glide.with(context).load(scanEntity.externalUriExpand).into(imageView)
    addView(imageView)
}