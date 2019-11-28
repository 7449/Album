package com.album.sample.imageloader

import android.graphics.Bitmap
import android.view.View
import android.widget.FrameLayout
import com.gallery.core.action.AlbumImageLoader
import com.gallery.core.ext.AlbumImageView
import com.gallery.core.ext.uri
import com.album.sample.R
import com.gallery.scan.ScanEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class SimpleSubsamplingScaleImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.AlbumImageView()
        Glide.with(container.context).load(albumEntity.uri()).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.AlbumImageView()
        Glide.with(container.context).load(finderEntity.uri()).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumPreview(albumEntity: ScanEntity, container: FrameLayout): View {
        val subsamplingScaleImageView = AlbumSubsamplingScaleView(container)
        Glide.with(subsamplingScaleImageView).asBitmap().load(albumEntity.uri()).apply(requestOptions).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(resource))
            }
        })
        return subsamplingScaleImageView
    }

    private fun AlbumSubsamplingScaleView(container: FrameLayout): SubsamplingScaleImageView {
        return if (container.childCount > 0) {
            container.getChildAt(0) as SubsamplingScaleImageView
        } else {
            SubsamplingScaleImageView(container.context)
        }
    }
}
