@file:Suppress("FunctionName")

package com.album.core.scan

import android.net.Uri
import android.provider.MediaStore
import com.album.core.AlbumScanConst

/**
 * Album Uri
 */
internal val ALBUM_FILE_URI: Uri = MediaStore.Files.getContentUri("external")

/**
 * 图片信息字段
 */
internal val ALBUM_ALL_COLUMNS = arrayOf(
        AlbumColumns.DATA,
        AlbumColumns.ID,
        AlbumColumns.SIZE,
        AlbumColumns.DURATION,
        AlbumColumns.PARENT,
        AlbumColumns.MIME_TYPE,
        AlbumColumns.DISPLAY_NAME,
        AlbumColumns.ORIENTATION,
        AlbumColumns.BUCKET_ID,
        AlbumColumns.BUCKET_DISPLAY_NAME,
        AlbumColumns.MEDIA_TYPE,
        AlbumColumns.WIDTH,
        AlbumColumns.HEIGHT,
        AlbumColumns.DATE_MODIFIED
)

/**
 * 图片信息条件
 */
internal const val ALBUM_ALL_SELECTION = AlbumColumns.SIZE + " > 0 AND " + AlbumColumns.MEDIA_TYPE + "=? or " + AlbumColumns.MEDIA_TYPE + "=? "

/**
 * 图片信息条件
 */
internal fun ALBUM_PARENT_SELECTION(parent: Long) = AlbumColumns.PARENT + "=" + parent + " and (" + ALBUM_ALL_SELECTION + ")"

/**
 * 图片信息条件
 */
internal fun ALBUM_PATH_SELECTION(path: String) = AlbumColumns.DATA + "=\"" + path + "\" and (" + ALBUM_ALL_SELECTION + ")"

/**
 * 排序条件
 */
internal const val ALBUM_ORDER_BY = AlbumColumns.DATE_MODIFIED + " DESC"

/**
 * 扫描条件
 */
internal fun ALBUM_SELECTION_ARGS(scanType: Int): Array<String> {
    return when (scanType) {
        AlbumScanConst.VIDEO -> arrayOf(AlbumColumns.VIDEO)
        AlbumScanConst.IMAGE -> arrayOf(AlbumColumns.IMAGE)
        AlbumScanConst.MIX -> arrayOf(AlbumColumns.IMAGE, AlbumColumns.VIDEO)
        else -> throw  KotlinNullPointerException()
    }
}