package com.album.presenter

import com.album.AlbumEntity
import java.util.*

interface AlbumPresenter {

    /**
     * 是否在扫描图片
     */
    fun getScanLoading(): Boolean

    /**
     * 开始扫描
     */
    fun scan(bucketId: String, page: Int, count: Int)

    /**
     * 扫描成功之后合并选择的数据
     */
    fun mergeSelectEntity(albumList: ArrayList<AlbumEntity>, multiplePreviewList: ArrayList<AlbumEntity>)

    /**
     * 第一次进来合并扫描之后的数据
     */
    fun firstMergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>?)

    /**
     * 拍照之后扫描
     */
    fun resultScan(path: String)
}


interface PreviewPresenter {

    /**
     * 预览页扫描
     */
    fun scan(bucketId: String, page: Int, count: Int)

    /**
     * 扫描成功之后合并选择的数据
     */
    fun mergeSelectEntity(albumEntityList: List<AlbumEntity>, selectAlbumEntityList: ArrayList<AlbumEntity>)
}
