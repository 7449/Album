package com.album.sample.imageloader

import android.content.ContentUris
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity
import com.album.ui.AlbumImageView
import com.album.ui.widget.TouchImageView
import com.squareup.picasso.Picasso

/**
 * by y on 19/08/2017.
 */

class SimplePicassoAlbumImageLoader : AlbumImageLoader {

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.id))
                .centerCrop()
                .resize(width, height)
                .into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: FinderEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, finderEntity.thumbnailsId))
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return albumImageView
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = TouchImageView(container.context)
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.id))
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return albumImageView
    }
}
