package com.album.sample.imageloader

import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import com.album.R
import com.album.core.*
import com.album.core.scan.AlbumEntity
import com.album.listener.AlbumImageLoader
import com.album.ui.wechat.activity.AlbumWeChatTouchImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author y
 * @create 2019/3/6
 */
class SimpleAlbumWeChatImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.AlbumWeChatTouchImageView()
        if (albumEntity.hasGif()) {
            imageView.gifTipView().show()
        } else {
            imageView.gifTipView().hide()
        }
        if (albumEntity.hasVideo()) {
            imageView.videoTipView().show()
        } else {
            imageView.videoTipView().hide()
        }
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height).dontAnimate()).into(imageView.imageView())
        return imageView
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(finderEntity.path).apply(requestOptions).into(imageView)
        return imageView
    }

    override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.AlbumImageView()
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(imageView)
        imageView.setBackgroundColor(Color.BLACK)
        return imageView
    }
}