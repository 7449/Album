package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.album.core.action.AlbumImageLoader
import com.album.core.ext.AlbumImageView
import com.album.scan.ScanEntity
import com.album.scan.uri
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
