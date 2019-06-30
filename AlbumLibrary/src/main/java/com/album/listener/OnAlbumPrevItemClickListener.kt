package com.album.listener

import android.view.View
import com.album.core.scan.AlbumEntity

interface OnAlbumPrevItemClickListener {

    fun onItemCheckBoxClick(view: View, currentMaxCount: Int, albumEntity: AlbumEntity)

    fun onItemClick(view: View, position: Int, albumEntity: AlbumEntity)

}