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
     * 扫描成功
     */
    fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>)

    /**
     * 隐藏进度条
     */
    fun hideProgress()

    /**
     * 显示进度条
     */
    fun showProgress()

    /**
     * 当前扫描类型
     * []
     */
    fun currentScanType(): Int
}