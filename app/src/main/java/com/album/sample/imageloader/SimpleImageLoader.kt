package com.album.sample.imageloader

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity


/**
 * @author y
 */
class SimpleImageLoader : AlbumImageLoader {

    override fun displayAlbum(view: View, width: Int, height: Int, albumEntity: AlbumEntity) {

    }

    override fun displayAlbumThumbnails(view: View, finderEntity: FinderEntity) {

    }

    override fun displayPreview(view: View, albumEntity: AlbumEntity) {

    }

    override fun frescoView(context: Context, type: Int): ImageView? {
        return null
    }
}
