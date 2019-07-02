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
     *  [LoaderManager.getInstance]
     */
    fun getAlbumContext(): FragmentActivity

    /**
     * 获取已经选择数据
     */
    fun getSelectEntity(): ArrayList<AlbumEntity>

    /**
     * 当前扫描类型
     */
    fun currentScanType(): Int

    /**
     * 刷新数据
     */
    fun refreshUI()

    /**
     * 扫描成功
     */
    fun scanSuccess(arrayList: ArrayList<AlbumEntity>)

    /**
     * 扫描文件夹成功
     */
    fun scanFinderSuccess(finderList: ArrayList<AlbumEntity>)

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(albumEntity: AlbumEntity?)
}
