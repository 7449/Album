@file:Suppress("FunctionName")

package com.album.core.scan

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.loader.content.CursorLoader
import com.album.core.scan.AlbumScan.IMAGE
import com.album.core.scan.AlbumScan.VIDEO

/**
 * 图片扫描 Uri
 */
internal val ALBUM_URL: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

/**
 * 视频扫描 Uri
 */
internal val VIDEO_URL: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

/**
 * 是否扫描全部
 */
const val SCAN_ALL = -1

/**
 * 过滤大小小于0的脏数据
 */
const val FILTER_SUFFIX = " and _size > 0 "

/**
 * 图片 sortOrder
 */
const val ALBUM_SORT_ORDER = MediaStore.Images.Media.DATE_MODIFIED + " desc"

/**
 * 视频 sortOrder
 */
const val VIDEO_SORT_ORDER = MediaStore.Video.Media.DATE_MODIFIED + " desc"

/**
 * 图片 selection 个数
 */
const val ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " +
        MediaStore.Images.Media.MIME_TYPE + "= ? or " +
        MediaStore.Images.Media.MIME_TYPE + "= ? or " +
        MediaStore.Images.Media.MIME_TYPE + "= ? %s"

/**
 * 视频 selection 个数
 */
const val VIDEO_SELECTION = MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? %s"

/**
 * 图片拍照返回 selection 个数
 */
const val ALBUM_RESULT_SELECTION = MediaStore.Images.Media.DATA + "= ? "

/**
 * 视频拍摄返回 selection 个数
 */
const val VIDEO_RESULT_SELECTION = MediaStore.Video.Media.DATA + "= ? "

const val ALBUM_BUCKET_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? and (" + ALBUM_SELECTION + ")"

const val VIDEO_BUCKET_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? and (" + VIDEO_SELECTION + ")"

val ALBUM_PROJECTION = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.SIZE)

val VIDEO_PROJECTION = arrayOf(
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.SIZE)

val ALBUM_FINDER_PROJECTION = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE)

val VIDEO_FINDER_PROJECTION = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.SIZE)


/**
 * 获取图片扫描 [CursorLoader]
 */
internal fun Context.albumImageCursorLoader(count: Int, page: Int, bucketId: String, filterImage: Boolean): CursorLoader {
    val sortOrder = when (count) {
        SCAN_ALL -> ALBUM_SORT_ORDER
        else -> ALBUM_SORT_ORDER + " limit " + page * count + "," + count
    }
    val selection = if (TextUtils.isEmpty(bucketId)) {
        String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    } else {
        String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    }
    return CursorLoader(this, ALBUM_URL, ALBUM_PROJECTION, selection, bucketId.albumSelectionArgs(IMAGE), sortOrder)
}

/**
 * 获取视频扫描 [CursorLoader]
 */
internal fun Context.albumVideoCursorLoader(count: Int, page: Int, bucketId: String, filterImage: Boolean): CursorLoader {
    val sortOrder = when (count) {
        SCAN_ALL -> VIDEO_SORT_ORDER
        else -> VIDEO_SORT_ORDER + " limit " + page * count + "," + count
    }
    val selection = if (TextUtils.isEmpty(bucketId)) {
        String.format(VIDEO_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    } else {
        String.format(VIDEO_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    }
    return CursorLoader(this, VIDEO_URL, VIDEO_PROJECTION, selection, bucketId.albumSelectionArgs(VIDEO), sortOrder)
}

internal fun Context.albumImageResultCursorLoader(path: String): CursorLoader {
    return CursorLoader(this, ALBUM_URL, ALBUM_PROJECTION, ALBUM_RESULT_SELECTION, arrayOf(path), ALBUM_SORT_ORDER)
}

internal fun Context.albumVideoResultCursorLoader(path: String): CursorLoader {
    return CursorLoader(this, VIDEO_URL, VIDEO_PROJECTION, VIDEO_RESULT_SELECTION, arrayOf(path), VIDEO_SORT_ORDER)
}

internal fun Context.albumImageFinderScanCursor(filterImage: Boolean): Cursor? {
    return contentResolver.query(
            ALBUM_URL,
            ALBUM_FINDER_PROJECTION,
            String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            "".albumSelectionArgs(IMAGE),
            ALBUM_SORT_ORDER)
}

internal fun Context.albumVideoFinderScanCursor(filterImage: Boolean): Cursor? {
    return contentResolver.query(
            VIDEO_URL,
            VIDEO_FINDER_PROJECTION,
            String.format(VIDEO_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            "".albumSelectionArgs(VIDEO),
            VIDEO_SORT_ORDER)
}

@SuppressLint("Recycle")
internal fun Context.albumImageFinderCursorCount(bucketId: String, filterImage: Boolean): Int {
    val query = contentResolver.query(
            ALBUM_URL,
            ALBUM_FINDER_PROJECTION,
            String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            bucketId.albumSelectionArgs(IMAGE),
            ALBUM_SORT_ORDER)
            ?: return 0
    val count = query.count
    query.close()
    return count
}

@SuppressLint("Recycle")
internal fun Context.albumVideoFinderCursorCount(bucketId: String, filterImage: Boolean): Int {
    val query = contentResolver.query(
            VIDEO_URL,
            VIDEO_FINDER_PROJECTION,
            String.format(VIDEO_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            bucketId.albumSelectionArgs(VIDEO),
            VIDEO_SORT_ORDER)
            ?: return 0
    val count = query.count
    query.close()
    return count
}

/**
 * 获取扫描类型
 */
internal fun String?.albumSelectionArgs(type: Int): Array<String> {
    return when (type) {
        IMAGE -> if (!this.isNullOrEmpty()) arrayOf(this.toString(), "image/jpeg", "image/png", "image/jpg", "image/gif") else arrayOf("image/jpeg", "image/png", "image/jpg", "image/gif")
        else -> if (!this.isNullOrEmpty()) arrayOf(this.toString(), "video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg") else arrayOf("video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg")
    }
}

