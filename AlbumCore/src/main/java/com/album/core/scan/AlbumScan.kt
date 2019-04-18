@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author y
 * @create 2019/2/27
 */

@Parcelize
data class AlbumEntity(
        var id: Long = 0,
        var path: String = "",
        var size: Long = 0,
        var duration: Long = 0,
        var parent: Long = 0,
        var mimeType: String = "",
        var displayName: String = "",
        var orientation: Int = 0,
        var bucketId: String = "",
        var bucketDisplayName: String = "",
        var mediaType: String = AlbumColumns.IMAGE,
        var width: Int = 0,
        var height: Int = 0,
        var dataModified: Long = 0,
        var count: Int = 0,
        var isCheck: Boolean = false) : Parcelable

fun AlbumEntity.hasVideo(): Boolean {
    return this.mediaType == AlbumColumns.VIDEO
}

fun AlbumEntity.hasGif(): Boolean {
    return this.mimeType == "image/gif"
}

object AlbumScan {
    /**
     * 扫描类型：图片
     */
    const val IMAGE = 0
    /**
     * 扫描类型：视频
     */
    const val VIDEO = 1
    /**
     * 扫描类型：混合
     */
    const val MIXING = 2

    /**
     * 没有更多数据了
     */
    const val SCAN_NO_MORE = 0

    /**
     * 没有扫描到图片
     */
    const val SCAN_EMPTY = 1

    /**
     * 全部 parent
     */
    const val ALL_PARENT = (-111111111).toLong()

    /**
     * 预览 parent
     */
    const val PREV_PARENT = (-222222222).toLong()

    /**
     * 预览页 LoaderManager id
     */
    internal const val PREVIEW_LOADER_ID = -111
    /**
     * 图库 LoaderManager id
     */
    internal const val ALBUM_LOADER_ID = -112
    /**
     * 拍照刷新 LoaderManager id
     */
    internal const val RESULT_LOADER_ID = -113
    /**
     * 文件夹 LoaderManager id
     */
    internal const val FINDER_LOADER_ID = -114
}

