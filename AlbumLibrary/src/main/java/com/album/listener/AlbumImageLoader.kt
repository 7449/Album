package com.album.listener

import android.content.Context
import android.widget.ImageView
import com.album.annotation.FrescoType
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity

/**
 * by y on 15/08/2017.
 */

interface AlbumImageLoader {

    fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity)

    fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity)

    fun displayPreview(view: ImageView, albumEntity: AlbumEntity)

    fun frescoView(context: Context, @FrescoType type: Int): ImageView?
}
