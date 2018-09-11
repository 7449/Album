package com.album.presenter

import com.album.entity.AlbumEntity
import java.util.*

/**
 * by y on 14/08/2017.
 */

interface AlbumPresenter {

    fun getScanLoading(): Boolean

    fun scan(bucketId: String, page: Int, count: Int)

    fun mergeSelectEntity(albumList: ArrayList<AlbumEntity>, multiplePreviewList: ArrayList<AlbumEntity>)

    fun firstMergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>?)

    fun resultScan(path: String)
}
