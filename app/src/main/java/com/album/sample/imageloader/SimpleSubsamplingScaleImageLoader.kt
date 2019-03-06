package com.album.sample.imageloader

import android.graphics.Bitmap
import android.view.View
import android.widget.FrameLayout
import com.album.R
import com.album.core.scan.AlbumEntity
import com.album.listener.AlbumImageLoader
import com.album.listener.AlbumImageView
import com.album.listener.DisplayView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class SimpleSubsamplingScaleImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View? {
        val albumImageView = AlbumImageView(container)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(albumImageView)
        return DisplayView(container, albumImageView)
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View? {
        val albumImageView = AlbumImageView(container)
        Glide.with(container.context).load(finderEntity.path).apply(requestOptions).into(albumImageView)
        return DisplayView(container, albumImageView)
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View? {
        val subsamplingScaleImageView = AlbumSubsamplingScaleView(container)
        Glide.with(subsamplingScaleImageView).asBitmap().load(albumEntity.path).apply(requestOptions).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                subsamplingScaleImageView.setImage(ImageSource.cachedBitmap(resource))
            }
        })
        return DisplayView(container, subsamplingScaleImageView)
    }

    private fun AlbumSubsamplingScaleView(container: FrameLayout): SubsamplingScaleImageView {
        return if (container.childCount > 0) {
            container.getChildAt(0) as SubsamplingScaleImageView
        } else {
            SubsamplingScaleImageView(container.context)
        }
    }
}
