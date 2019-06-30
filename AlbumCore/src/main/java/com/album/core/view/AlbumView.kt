package com.album.core.view

import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumView : AlbumBaseView {
    /**
     * 刷新数据
     */
    fun refreshUI()

    /**
     * 扫描文件夹成功
     */
    fun scanFinderSuccess(finderList: ArrayList<AlbumEntity>)

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(albumEntity: AlbumEntity?)
}
