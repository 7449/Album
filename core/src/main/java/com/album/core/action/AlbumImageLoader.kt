package com.album.core.action

import android.view.View
import android.widget.FrameLayout
import com.album.scan.ScanEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumImageLoader {
    /**
     * 首页图片加载
     */
    fun displayAlbum(width: Int, height: Int, albumEntity: ScanEntity, container: FrameLayout): View

    /**
     * 目录图片加载
     */
    fun displayAlbumThumbnails(finderEntity: ScanEntity, container: FrameLayout): View

    /**
     * 预览页图片加载
     */
    fun displayAlbumPreview(albumEntity: ScanEntity, container: FrameLayout): View
}