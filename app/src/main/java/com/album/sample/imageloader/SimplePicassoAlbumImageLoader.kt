package com.album.sample.imageloader

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.widget.ImageView
import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity
import com.album.FrescoType
import com.squareup.picasso.Picasso

/**
 * by y on 19/08/2017.
 */

class SimplePicassoAlbumImageLoader : AlbumImageLoader {

    override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.id))
                .centerCrop()
                .into(view)
    }

    override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, finderEntity.thumbnailsId))
                .resize(50, 50)
                .centerCrop()
                .into(view)
    }

    override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.id))
                .resize(50, 50)
                .centerCrop()
                .into(view)
    }

    override fun frescoView(context: Context, @FrescoType type: Int): ImageView? {
        return null
    }
}
