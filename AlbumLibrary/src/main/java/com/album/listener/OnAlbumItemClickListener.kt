package com.album.listener

import android.view.View
import com.album.core.scan.AlbumEntity


interface OnAlbumItemClickListener {

    fun onCameraItemClick(view: View, position: Int, albumEntity: AlbumEntity)

    fun onPhotoItemClick(view: View, position: Int, albumEntity: AlbumEntity)

}