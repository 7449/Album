package com.album.core.view

import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumPreViewView {
    /**
     *  [LoaderManager.getInstance]
     */
    fun getPrevContext(): FragmentActivity

    /**
     * 扫描成功回调,如果是点击preview则不会触发
     */
    fun scanSuccess(entityList: ArrayList<AlbumEntity>)

    /**
     * 隐藏进度条
     */
    fun hideProgress()

    /**
     * 显示进度条
     */
    fun showProgress()
}