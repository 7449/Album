package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.gallery.core.action.AlbumImageLoader
import com.gallery.core.ext.AlbumImageView
import com.gallery.core.ext.uri
import com.gallery.scan.ScanEntity
import com.squareup.picasso.Picasso

/**
 * by y on 19/08/2017.
 */

class SimplePicassoAlbumImageLoader : AlbumImageLoader {

    override fun displayAlbum(width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.AlbumImageView()
        Picasso.get()
                .load(albumEntity.uri())
                .centerCrop()
                .resize(width, height)
                .into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.AlbumImageView()
        Picasso.get()
                .load(finderEntity.uri())
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumPreview(albumEntity: ScanEntity, container: FrameLayout): View {
        val albumImageView = container.AlbumImageView()
        Picasso.get()
                .load(albumEntity.uri())
                .resize(50, 50)
                .centerCrop()
                .into(albumImageView)
        return albumImageView
    }
}
