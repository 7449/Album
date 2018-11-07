package com.album.sample.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.view.View

import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity
import com.album.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

class SimpleSubsamplingScaleImageLoader : AlbumImageLoader {
    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).centerCrop()
    override fun displayAlbum(view: View, width: Int, height: Int, albumEntity: AlbumEntity) {
        if (view is SubsamplingScaleImageView) {
            Glide.with(view).asBitmap().load(albumEntity.path).apply(requestOptions.override(width, height)).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    view.setImage(ImageSource.cachedBitmap(resource))
                }
            })
        }
    }

    override fun displayAlbumThumbnails(view: View, finderEntity: FinderEntity) {
        if (view is SubsamplingScaleImageView) {
            Glide.with(view).asBitmap().load(finderEntity.thumbnailsPath).apply(requestOptions).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    view.setImage(ImageSource.cachedBitmap(resource))
                }
            })
        }
    }

    override fun displayPreview(view: View, albumEntity: AlbumEntity) {
        if (view is SubsamplingScaleImageView) {
            Glide.with(view).asBitmap().load(albumEntity.path).apply(requestOptions).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    view.setImage(ImageSource.cachedBitmap(resource))
                }
            })
        }
    }

    override fun frescoView(context: Context, type: Int): View? {
        return SubsamplingScaleImageView(context)
    }
}
