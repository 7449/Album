package com.gallery.core.extensions

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File

fun Context.findIdByUri(uri: Uri): Long = contentResolver.queryId(uri)

/** 文件是否存在，适配至Android11(目前没有找到比较好的在高版本上检测文件是否存在的方法) */
fun Uri.fileExists(context: Context): Boolean {
    return runCatching {
        context.contentResolver.openAssetFileDescriptor(this, "r")?.close()
    }.isSuccess
}

fun Uri.delete(context: Context) {
    runCatching {
        context.contentResolver.delete(this, null, null)
    }
        .onSuccess { Log.i("UriUtils", "delete uri success:$this") }
        .onFailure { Log.e("UriUtils", "delete uri failure:$this") }
}

/** 根据Uri查询DATA(文件路径) *已过时 */
@SuppressLint("Range")
fun ContentResolver.queryData(uri: Uri): String? =
    query(uri, arrayOf(MediaStore.Files.FileColumns.DATA), null, null, null).use {
        val cursor = it ?: return null
        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        }
        return null
    }

/** 根据Uri查询Id */
@SuppressLint("Range")
fun ContentResolver.queryId(uri: Uri): Long {
    val split = uri.toString().split("/")
    var id = -1L
    runCatching {
        id = split[split.size - 1].toLong()
    }.onFailure {
        query(uri, arrayOf(MediaStore.Files.FileColumns._ID), null, null, null).use {
            val cursor = it ?: return@use
            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
            }
        }
    }
    return id
}

/** 获取图片Uri,适配至高版本,Q以上按照[MediaStore.MediaColumns.RELATIVE_PATH]，以下按照[MediaStore.MediaColumns.DATA]  */
internal fun Context.insertImageUri(
    file: File,
    relativePath: String,
): Uri? = insertImageUri(ContentValues().apply {
    if (hasQExpand()) {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    } else {
        put(MediaStore.MediaColumns.DATA, file.path)
    }
})

private fun Context.insertImageUri(contentValues: ContentValues): Uri? =
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    } else {
        null
    }

/** 获取视频Uri,适配至高版本,Q以上按照[MediaStore.MediaColumns.RELATIVE_PATH]，以下按照[MediaStore.MediaColumns.DATA] */
internal fun Context.insertVideoUri(
    file: File,
    relativePath: String,
): Uri? = insertVideoUri(ContentValues().apply {
    if (hasQExpand()) {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    } else {
        put(MediaStore.MediaColumns.DATA, file.path)
    }
})

private fun Context.insertVideoUri(contentValues: ContentValues): Uri? =
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
    } else {
        null
    }
