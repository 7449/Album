package com.album.ui.widget

import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity

import java.util.ArrayList

/**
 * by y on 04/09/2017.
 */

interface ScanCallBack {
    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>, list: ArrayList<FinderEntity>)

    fun resultSuccess(albumEntity: AlbumEntity, finderEntityList: ArrayList<FinderEntity>)
}
