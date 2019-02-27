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

const val SCAN_ALL = -1

const val FILTER_SUFFIX = " and _size > 0 "

const val ALBUM_SORT_ORDER = MediaStore.Images.Media.DATE_MODIFIED + " desc"

const val VIDEO_SORT_ORDER = MediaStore.Video.Media.DATE_MODIFIED + " desc"

const val ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " +
        MediaStore.Images.Media.MIME_TYPE + "= ? or " +
        MediaStore.Images.Media.MIME_TYPE + "= ? or " +
        MediaStore.Images.Media.MIME_TYPE + "= ? %s"

const val VIDEO_SELECTION = MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? or " +
        MediaStore.Video.Media.MIME_TYPE + "= ? %s"

const val ALBUM_RESULT_SELECTION = MediaStore.Images.Media.DATA + "= ? "

const val VIDEO_RESULT_SELECTION = MediaStore.Video.Media.DATA + "= ? "

const val ALBUM_BUCKET_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? and (" + ALBUM_SELECTION + ")"

const val VIDEO_BUCKET_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? and (" + VIDEO_SELECTION + ")"

val ALBUM_URL: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

val VIDEO_URL: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

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


fun ImageCursorLoader(context: Context, count: Int, page: Int, bucketId: String, filterImage: Boolean): CursorLoader {
    val sortOrder = when (count) {
        SCAN_ALL -> ALBUM_SORT_ORDER
        else -> ALBUM_SORT_ORDER + " limit " + page * count + "," + count
    }
    val selection = if (TextUtils.isEmpty(bucketId)) {
        String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    } else {
        String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    }
    return CursorLoader(context, ALBUM_URL, ALBUM_PROJECTION, selection, selectionArgs(IMAGE, bucketId), sortOrder)
}

fun VideoCursorLoader(context: Context, count: Int, page: Int, bucketId: String, filterImage: Boolean): CursorLoader {
    val sortOrder = when (count) {
        SCAN_ALL -> VIDEO_SORT_ORDER
        else -> VIDEO_SORT_ORDER + " limit " + page * count + "," + count
    }
    val selection = if (TextUtils.isEmpty(bucketId)) {
        String.format(VIDEO_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    } else {
        String.format(VIDEO_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    }
    return CursorLoader(context, VIDEO_URL, VIDEO_PROJECTION, selection, selectionArgs(VIDEO, bucketId), sortOrder)
}


fun ImageResultCursorLoader(context: Context, path: String): CursorLoader {
    return CursorLoader(context,
            ALBUM_URL,
            ALBUM_PROJECTION,
            ALBUM_RESULT_SELECTION,
            arrayOf(path),
            ALBUM_SORT_ORDER)
}

fun VideoResultCursorLoader(context: Context, path: String): CursorLoader {
    return CursorLoader(context, VIDEO_URL,
            VIDEO_PROJECTION,
            VIDEO_RESULT_SELECTION,
            arrayOf(path),
            VIDEO_SORT_ORDER)
}

fun ImageFinderScanCursor(context: Context, filterImage: Boolean): Cursor? {
    return context.contentResolver.query(
            ALBUM_URL,
            ALBUM_FINDER_PROJECTION,
            String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            selectionArgs(IMAGE, null),
            ALBUM_SORT_ORDER)
}

@SuppressLint("Recycle")
fun ImageFinderCursorCount(context: Context, bucketId: String, filterImage: Boolean): Int {
    val query = context.contentResolver.query(
            ALBUM_URL,
            ALBUM_FINDER_PROJECTION,
            String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            selectionArgs(IMAGE, bucketId),
            ALBUM_SORT_ORDER)
            ?: return 0
    val count = query.count
    query.close()
    return count
}

fun VideoFinderScanCursor(context: Context, filterImage: Boolean): Cursor? {
    return context.contentResolver.query(
            VIDEO_URL,
            VIDEO_FINDER_PROJECTION,
            String.format(VIDEO_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            selectionArgs(VIDEO, null),
            VIDEO_SORT_ORDER)
}

@SuppressLint("Recycle")
fun VideoFinderCursorCount(context: Context, bucketId: String, filterImage: Boolean): Int {
    val query = context.contentResolver.query(
            VIDEO_URL,
            VIDEO_FINDER_PROJECTION,
            String.format(VIDEO_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            selectionArgs(VIDEO, bucketId),
            VIDEO_SORT_ORDER)
            ?: return 0
    val count = query.count
    query.close()
    return count
}

fun selectionArgs(type: Int, bucketId: String?): Array<String> {
    return when (type) {
        IMAGE -> if (!TextUtils.isEmpty(bucketId)) arrayOf(bucketId!!, "image/jpeg", "image/png", "image/jpg", "image/gif") else arrayOf("image/jpeg", "image/png", "image/jpg", "image/gif")
        else -> if (!TextUtils.isEmpty(bucketId)) arrayOf(bucketId!!, "video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg") else arrayOf("video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg")
    }
}
