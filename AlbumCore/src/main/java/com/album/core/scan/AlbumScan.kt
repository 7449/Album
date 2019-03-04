@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author y
 * @create 2019/2/27
 */

@Parcelize
class AlbumEntity(private var dirPath: String = "",
                  private var dirName: String = "",
                  var bucketId: String = "",
                  var path: String = "",
                  var id: Long = 0,
                  var isVideo: Boolean = false,
                  var isCheck: Boolean = false) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlbumEntity

        if (dirPath != other.dirPath) return false
        if (dirName != other.dirName) return false
        if (bucketId != other.bucketId) return false
        if (path != other.path) return false
        if (id != other.id) return false
        if (isVideo != other.isVideo) return false
        if (isCheck != other.isCheck) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dirPath.hashCode()
        result = 31 * result + dirName.hashCode()
        result = 31 * result + bucketId.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + isVideo.hashCode()
        result = 31 * result + isCheck.hashCode()
        return result
    }

    override fun toString(): String {
        return "AlbumEntity(dirPath='$dirPath', dirName='$dirName', bucketId='$bucketId', path='$path', id=$id, isVideo=$isVideo, isCheck=$isCheck)"
    }
}

@Parcelize
class FinderEntity(var dirName: String = "", var thumbnailsPath: String = "", var thumbnailsId: Long = 0, var bucketId: String = "", var count: Int = 0) : Parcelable

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

