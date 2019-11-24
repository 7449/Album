package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.album.core.action.AlbumImageLoader
import com.album.core.uri
import com.album.sample.R
import com.album.scan.AlbumImageView
import com.album.scan.scan.AlbumEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SimpleGlideImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(albumEntity.uri()).apply(requestOptions.override(width, height)).into(imageView)
        return imageView
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(finderEntity.uri()).apply(requestOptions).into(imageView)
        return imageView
    }

    override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(albumEntity.uri()).apply(requestOptions.override(albumEntity.width, albumEntity.height)).into(imageView)
        return imageView
    }
}
