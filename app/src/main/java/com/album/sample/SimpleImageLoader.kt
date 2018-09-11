package com.album.sample

import android.content.Context
import android.widget.ImageView

import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.listener.AlbumImageLoader

/**
 * @author y
 */
class SimpleImageLoader : AlbumImageLoader {

    override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {

    }

    override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {

    }

    override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {

    }

    override fun frescoView(context: Context, type: Int): ImageView? {
        return null
    }
}
