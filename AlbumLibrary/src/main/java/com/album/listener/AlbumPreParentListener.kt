package com.album.listener

/**
 * @author y
 * @create 2019/2/27
 * 预览页回调
 */
interface AlbumPreParentListener {
    /**
     * 点击checkbox时调用
     */
    fun onChangedCheckBoxCount(currentMaxCount: Int)

    /**
     * 滑动时调用，可改变toolbar数据
     */
    fun onChangedViewPager(currentPos: Int, maxPos: Int)
}