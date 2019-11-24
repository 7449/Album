package com.album.core.action

import android.view.View
import android.widget.FrameLayout
import com.album.scan.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumImageLoader {
    /**
     * 首页图片加载
     */
    fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View

    /**
     * 目录图片加载
     */
    fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View

    /**
     * 预览页图片加载
     */
    fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View
}