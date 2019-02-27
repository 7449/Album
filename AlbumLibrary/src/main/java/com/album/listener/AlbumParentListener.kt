package com.album.listener

import com.album.core.scan.AlbumEntity

/**
 * @author y
 * @create 2019/2/27
 * 点击单个图片最终触发这个回调,可以进入预览页
 */
interface AlbumParentListener {
    /**
     * 点击进入预览页,依赖的DialogFragment或者Activity继承即可
     */
    fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String)
}