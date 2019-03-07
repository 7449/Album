package com.album.sample.imageloader

import android.content.ContentUris
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import com.album.core.scan.AlbumEntity
import com.album.listener.AlbumImageLoader
import com.album.listener.AlbumImageView
import com.album.listener.AppCompatImageView
import com.album.listener.DisplayView
import com.squareup.picasso.Picasso

/**
 * by y on 19/08/2017.
 */

class SimplePicassoAlbumImageLoader : AlbumImageLoader {

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View? {
        val albumImageView = AlbumImageView(container)
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.id))
                .centerCrop()
                .resize(width, height)
                .into(albumImageView)
        return DisplayView(container, albumImageView)
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View? {
        val albumImageView = AlbumImageView(container)
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, finderEntity.id))
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return DisplayView(container, albumImageView)
    }

    override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View? {
        val albumImageView = AppCompatImageView(container)
        Picasso.get()
                .load(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumEntity.id))
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return DisplayView(container, albumImageView)
    }
}
