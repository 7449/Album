package com.album.core.view

import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumView {
    /**
     * 获取已经选择数据
     */
    fun getSelectEntity(): ArrayList<AlbumEntity>

    /**
     * [LoaderManager.getInstance]
     */
    fun getAlbumActivity(): FragmentActivity

    /**
     * 页码
     */
    fun getPage(): Int

    /**
     * 显示进度
     */
    fun showProgress()

    /**
     * 隐藏进度
     */
    fun hideProgress()

    /**
     * 刷新数据
     */
    fun refreshUI()

    /**
     * 扫描成功
     */
    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    /**
     * 扫描文件夹成功
     */
    fun scanFinderSuccess(list: ArrayList<AlbumEntity>)

    /**
     * 没有数据了
     */
    fun onAlbumNoMore()

    /**
     * 没有数据
     */
    fun onAlbumEmpty()

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(albumEntity: AlbumEntity?)
}