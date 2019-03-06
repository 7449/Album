@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author y
 * @create 2019/2/27
 */

@Parcelize
class AlbumEntity(
        var id: Long = 0,
        var path: String = "",
        var size: Long = 0,
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
        var isCheck: Boolean = false) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlbumEntity

        if (id != other.id) return false
        if (path != other.path) return false
        if (size != other.size) return false
        if (parent != other.parent) return false
        if (mimeType != other.mimeType) return false
        if (displayName != other.displayName) return false
        if (orientation != other.orientation) return false
        if (bucketId != other.bucketId) return false
        if (bucketDisplayName != other.bucketDisplayName) return false
        if (mediaType != other.mediaType) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (dataModified != other.dataModified) return false
        if (count != other.count) return false
        if (isCheck != other.isCheck) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + parent.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + orientation
        result = 31 * result + bucketId.hashCode()
        result = 31 * result + bucketDisplayName.hashCode()
        result = 31 * result + mediaType.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + dataModified.hashCode()
        result = 31 * result + count
        result = 31 * result + isCheck.hashCode()
        return result
    }

    override fun toString(): String {
        return "AlbumEntity(id=$id, path='$path', size=$size, parent=$parent, mimeType='$mimeType', displayName='$displayName', orientation=$orientation, bucketId='$bucketId', bucketDisplayName='$bucketDisplayName', mediaType='$mediaType', width=$width, height=$height, dataModified=$dataModified, count=$count, isCheck=$isCheck)"
    }
}

fun AlbumEntity.hasVideo(): Boolean {
    return this.mediaType == AlbumColumns.VIDEO
}

fun AlbumEntity.hasGif(): Boolean {
    return this.displayName.endsWith("gif") || this.displayName.endsWith("GIF")
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

