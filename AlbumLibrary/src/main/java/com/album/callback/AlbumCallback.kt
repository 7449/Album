package com.album.callback

import android.view.View
import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 * 点击单个图片最终触发这个回调,可以进入预览页
 */
interface AlbumCallback {
    /**
     * 点击进入预览页,依赖的DialogFragment或者Activity继承即可
     */
    fun onAlbumItemClick(selectEntity: ArrayList<AlbumEntity>, position: Int, parentId: Long)

    /**
     * 横竖屏切换时调用
     */
    fun onAlbumScreenChanged(selectCount: Int)

    /**
     * 点击checkbox时调用
     */
    fun onChangedCheckBoxCount(view: View, selectCount: Int, albumEntity: AlbumEntity)

    /**
     * 预览页退回之后选择数据如果有改动则触发
     */
    fun onPrevChangedCount(selectCount: Int)

    /**
     * 选择时的筛选，返回true拦截
     */
    fun onAlbumCheckBoxFilter(view: View, position: Int, albumEntity: AlbumEntity): Boolean = false

    /**
     * 接管裁剪 true
     */
    fun onAlbumCustomCrop(path: String): Boolean = false
}