package com.album.core.view

import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/3/8
 */
interface AlbumBaseView {
    /**
     *  [LoaderManager.getInstance]
     */
    fun getAlbumContext(): FragmentActivity

    /**
     * 获取已经选择数据
     */
    fun getSelectEntity(): ArrayList<AlbumEntity>

    /**
     * 扫描成功
     */
    fun scanSuccess(arrayList: ArrayList<AlbumEntity>)

    /**
     * 当前扫描类型
     */
    fun currentScanType(): Int
}