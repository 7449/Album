package com.album.listener

/**
 * @author y
 * @create 2019/2/27
 * 预览页回调
 */
interface AlbumPreviewParentListener {
    /**
     * 点击checkbox时调用
     */
    fun onChangedCount(currentCount: Int)

    /**
     * 滑动时调用，可改变toolbar数据
     */
    fun onChangedToolbarCount(currentPos: Int, maxPos: Int)
}