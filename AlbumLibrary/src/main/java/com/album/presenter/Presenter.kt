package com.album.presenter

import com.album.AlbumEntity


const val PREVIEW_LOADER_ID = -111
const val ALBUM_LOADER_ID = -112
const val RESULT_LOADER_ID = -113
const val FINDER_LOADER_ID = -114

interface AlbumPresenter {
    /**
     * 开始扫描
     */
    fun startScan(bucketId: String, page: Int)

    /**
     * 扫描成功之后合并选择的数据
     */
    fun mergeEntity(albumList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>)

    /**
     * 拍照之后扫描
     */
    fun resultScan(path: String)

    /**
     * 销毁相关任务
     */
    fun destroyLoaderManager()
}


interface PreviewPresenter
