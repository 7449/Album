package com.album.sample.imageloader

import android.view.View
import android.widget.FrameLayout
import com.album.R
import com.album.core.AlbumView.hide
import com.album.core.AlbumView.show
import com.album.core.scan.AlbumEntity
import com.album.core.scan.hasGif
import com.album.listener.AlbumImageLoader
import com.album.listener.AlbumImageView
import com.album.listener.DisplayView
import com.album.ui.wechat.activity.AlbumWeChatTouchImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @author y
 * @create 2019/3/6
 */
class SimpleAlbumWeChatImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View? {
        val imageView = AlbumWeChatTouchImageView(container)
        if (albumEntity.hasGif()) {
            imageView.gifTipView().show()
        } else {
            imageView.gifTipView().hide()
        }
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height).dontAnimate()).into(imageView.imageView())
        return DisplayView(container, imageView)
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View? {
        val imageView = AlbumImageView(container)
        Glide.with(container.context).load(finderEntity.path).apply(requestOptions).into(imageView)
        return DisplayView(container, imageView)
    }

    override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View? {
        val imageView = AlbumImageView(container)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(imageView)
        return DisplayView(container, imageView)
    }
}