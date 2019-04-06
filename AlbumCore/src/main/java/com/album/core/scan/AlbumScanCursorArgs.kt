@file:Suppress("FunctionName")

package com.album.core.scan

import android.net.Uri
import android.provider.MediaStore

/**
 * 是否扫描全部
 */
const val SCAN_ALL = -1

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
 * 文件夹信息字段
 */
internal val ALBUM_FINDER_ALL_COLUMNS = arrayOf(
        "count(" + AlbumColumns.PARENT + ") AS " + AlbumColumns.COUNT,
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
        AlbumColumns.DATE_MODIFIED + "  FROM (SELECT *"
)

/**
 * 文件夹信息条件
 */
internal const val ALBUM_FINDER_ALL_SELECTION = AlbumColumns.SIZE + " > 0 AND " +
        AlbumColumns.MEDIA_TYPE + "=? or " +
        AlbumColumns.MEDIA_TYPE + "=? )) GROUP BY (" + AlbumColumns.PARENT

/**
 * 排序条件
 */
internal const val ALBUM_ORDER_BY = AlbumColumns.DATE_MODIFIED + " DESC"

/**
 * 排序分页条件
 */
internal fun ALBUM_ORDER_BY_LIMIT(page: Int, scanCount: Int) = ALBUM_ORDER_BY + " limit " + page * scanCount + "," + scanCount

/**
 * 扫描条件
 */
internal fun ALBUM_SELECTION_ARGS(scanType: Int): Array<String> {
    return when (scanType) {
        AlbumScan.VIDEO -> arrayOf(AlbumColumns.VIDEO)
        AlbumScan.IMAGE -> arrayOf(AlbumColumns.IMAGE)
        AlbumScan.MIXING -> arrayOf(AlbumColumns.IMAGE, AlbumColumns.VIDEO)
        else -> throw  KotlinNullPointerException()
    }
}