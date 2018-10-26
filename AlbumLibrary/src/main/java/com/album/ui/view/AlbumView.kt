package com.album.ui.view

import android.app.Activity
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import java.util.*

/**
 * by y on 14/08/2017.
 */

interface AlbumView {

    fun getSelectEntity(): ArrayList<AlbumEntity>

    fun getAlbumActivity(): Activity

    fun showProgress()

    fun hideProgress()

    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    fun scanFinder(list: ArrayList<FinderEntity>)

    fun onAlbumNoMore()

    fun resultSuccess(albumEntity: AlbumEntity?)
}
