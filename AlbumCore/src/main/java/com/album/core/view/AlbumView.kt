package com.album.core.view

import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumView : AlbumBaseView {
    /**
     * 获取已经选择数据
     */
    fun getSelectEntity(): ArrayList<AlbumEntity>

    /**
     * 页码
     */
    fun getPage(): Int

    /**
     * 刷新数据
     */
    fun refreshUI()

    /**
     * 扫描文件夹成功
     */
    fun scanFinderSuccess(list: ArrayList<AlbumEntity>)

    /**
     * type用来区分扫描结果的回调
     */
    fun onAlbumScanCallback(type: Int)

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(albumEntity: AlbumEntity?)
}