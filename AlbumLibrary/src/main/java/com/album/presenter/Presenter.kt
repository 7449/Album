package com.album.presenter

import com.album.AlbumEntity
import java.util.*

interface AlbumPresenter {

    fun getScanLoading(): Boolean

    fun scan(bucketId: String, page: Int, count: Int)

    fun mergeSelectEntity(albumList: ArrayList<AlbumEntity>, multiplePreviewList: ArrayList<AlbumEntity>)

    fun firstMergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>?)

    fun resultScan(path: String)
}


interface PreviewPresenter {
    fun scan(bucketId: String, page: Int, count: Int)

    fun mergeSelectEntity(albumEntityList: List<AlbumEntity>, selectAlbumEntityList: ArrayList<AlbumEntity>)
}
