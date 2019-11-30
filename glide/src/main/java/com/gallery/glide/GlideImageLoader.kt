package com.gallery.glide

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.action.GalleryImageLoader
import com.gallery.core.ext.uri
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity

class GlideImageLoader : GalleryImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

    override fun displayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) {
        Log.d("glide", galleryEntity.id.toString() + "  " + galleryEntity.path)
        val imageView = container.galleryImageView()
        Glide.with(container.context).load(galleryEntity.uri()).apply(requestOptions.override(width, height)).into(imageView)
        container.addChildView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun displayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        val imageView = container.galleryImageView()
        Glide.with(container.context).load(finderEntity.uri()).apply(requestOptions).into(imageView)
        container.addChildView(imageView)
    }

    override fun displayGalleryPreview(galleryEntity: ScanEntity, container: FrameLayout) {
        val imageView = container.imageView()
        Glide.with(container.context).load(galleryEntity.uri()).into(imageView)
        container.addChildView(imageView)
    }

    private fun ViewGroup.addChildView(childView: View?) {
        if (indexOfChild(childView) == -1 && childView != null) {
            addView(childView)
        }
    }

    private fun ViewGroup.addChildView(childView: View?, layoutParams: ViewGroup.LayoutParams?) {
        if (indexOfChild(childView) == -1 && childView != null) {
            if (layoutParams != null) {
                addView(childView, layoutParams)
            } else {
                addView(childView)
            }
        }
    }

    private fun ViewGroup.imageView(): ImageView = let {
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt is ImageView) {
                return childAt
            }
        }
        return ImageView(context).also { layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT) }
    }

    private fun ViewGroup.galleryImageView(): GalleryImageView = let {
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt is GalleryImageView) {
                return childAt
            }
        }
        return GalleryImageView(context)
    }
}