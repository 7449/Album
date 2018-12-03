package com.album.util.scan

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import com.album.AlbumEntity
import com.album.FinderEntity
import com.album.IMAGE
import com.album.VIDEO
import java.util.*

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

const val ALBUM_BUCKET_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? and " + ALBUM_SELECTION

const val VIDEO_BUCKET_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? and " + VIDEO_SELECTION

val ALBUM_URL: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

val VIDEO_URL: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

val ALBUM_PROJECTION = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.SIZE)

val VIDEO_PROJECTION = arrayOf(
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media._ID,
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

fun ScanBase.albumScanCursor(bucketId: String, page: Int, count: Int, filterImage: Boolean): Cursor? {
    val sortOrder = when (count) {
        SCAN_ALL -> ALBUM_SORT_ORDER
        else -> ALBUM_SORT_ORDER + " limit " + page * count + "," + count
    }
    val selection = if (TextUtils.isEmpty(bucketId)) {
        String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    } else {
        String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    }
    return contentResolver.query(ALBUM_URL, ALBUM_PROJECTION, selection, selectionArgs(IMAGE, bucketId), sortOrder)
}

fun ScanBase.albumResultScanCursor(path: String): Cursor? {
    return contentResolver.query(
            ALBUM_URL,
            ALBUM_PROJECTION,
            ALBUM_RESULT_SELECTION,
            arrayOf(path),
            ALBUM_SORT_ORDER)
}

fun ScanBase.albumFinderScanCursor(filterImage: Boolean): Cursor? {
    return contentResolver.query(
            ALBUM_URL,
            ALBUM_FINDER_PROJECTION,
            String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            selectionArgs(IMAGE, null),
            ALBUM_SORT_ORDER)
}

@SuppressLint("Recycle")
fun ScanBase.albumFinderCursorCount(bucketId: String, filterImage: Boolean): Int {
    val query = contentResolver.query(
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


fun ScanBase.videoScanCursor(bucketId: String, page: Int, count: Int, filterImage: Boolean): Cursor? {
    val sortOrder = when (count) {
        SCAN_ALL -> VIDEO_SORT_ORDER
        else -> VIDEO_SORT_ORDER + " limit " + page * count + "," + count
    }
    val selection = if (TextUtils.isEmpty(bucketId)) {
        String.format(VIDEO_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    } else {
        String.format(VIDEO_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
    }
    return contentResolver.query(VIDEO_URL, VIDEO_PROJECTION, selection, selectionArgs(VIDEO, bucketId), sortOrder)
}

fun ScanBase.videoResultScanCursor(path: String): Cursor? {
    return contentResolver.query(
            VIDEO_URL,
            VIDEO_PROJECTION,
            VIDEO_RESULT_SELECTION,
            arrayOf(path),
            VIDEO_SORT_ORDER)
}

fun ScanBase.videoFinderScanCursor(filterImage: Boolean): Cursor? {
    return contentResolver.query(
            VIDEO_URL,
            VIDEO_FINDER_PROJECTION,
            String.format(VIDEO_SELECTION, if (filterImage) FILTER_SUFFIX else ""),
            selectionArgs(VIDEO, null),
            VIDEO_SORT_ORDER)
}

@SuppressLint("Recycle")
fun ScanBase.videoFinderCursorCount(bucketId: String, filterImage: Boolean): Int {
    val query = contentResolver.query(
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

fun ScanBase.selectionArgs(type: Int, bucketId: String?): Array<String> {
    return when (type) {
        IMAGE -> if (!TextUtils.isEmpty(bucketId)) arrayOf(bucketId!!, "image/jpeg", "image/png", "image/jpg", "image/gif") else arrayOf("image/jpeg", "image/png", "image/jpg", "image/gif")
        else -> if (!TextUtils.isEmpty(bucketId)) arrayOf(bucketId!!, "video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg") else arrayOf("video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv", "video/mkv", "video/mov", "video/mpg")
    }
}

interface ScanView {

    fun startScan(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int, filterImage: Boolean, sdName: String)

    fun resultScan(scanCallBack: ScanCallBack, path: String, filterImage: Boolean, sdName: String)

    fun cursorFinder(finderList: ArrayList<FinderEntity>, filterImage: Boolean, sdName: String)

}

interface ScanCallBack {
    fun scanCallBack(imageList: ArrayList<AlbumEntity>, finderList: ArrayList<FinderEntity>)

    fun resultCallBack(image: AlbumEntity?, finderList: ArrayList<FinderEntity>)
}

abstract class ScanBase(val contentResolver: ContentResolver) : ScanView