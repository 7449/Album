package com.album.ui.widget

import android.content.Context
import android.widget.ImageView
import com.album.R
import com.album.annotation.FrescoType
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.listener.AlbumImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * by y on 15/08/2017.
 */

class SimpleGlideAlbumImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_launcher)
            .error(R.drawable.ic_launcher)
            .centerCrop()

    override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {
        Glide
                .with(view.context)
                .load(albumEntity.path)
                .apply(requestOptions.override(width, height))
                .into(view)
    }

    override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {
        Glide
                .with(view.context)
                .load(finderEntity.thumbnailsPath).apply(requestOptions)
                .into(view)
    }

    override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {
        Glide
                .with(view.context)
                .load(albumEntity.path).apply(requestOptions)
                .into(view)
    }

    override fun frescoView(context: Context, @FrescoType type: Int): ImageView? {
        return null
    }

}
