package com.gallery.core.extensions

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.gallery.core.args.GalleryConfigs
import java.io.File

fun Context.findIdByUri(uri: Uri): Long = contentResolver.queryId(uri)

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

@SuppressLint("Range")
fun ContentResolver.queryData(uri: Uri): String? =
    query(uri, arrayOf(MediaStore.Files.FileColumns.DATA), null, null, null).use {
        val cursor = it ?: return null
        while (cursor.moveToNext()) {
            return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
        }
        return null
    }

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

/** content://media/external/images/media/id or content://media/external/video/media/id */
fun Context.takePictureUri(configs: GalleryConfigs): Uri? {
    val file: File = when {
        hasQ() -> File(configs.fileConfig.picturePath + File.separator + configs.takePictureName)
        else -> Environment.getExternalStoragePublicDirectory(configs.fileConfig.picturePath).absolutePath
            .mkdirsFile(configs.takePictureName)
    }
    return if (configs.isScanVideoMedia) insertVideoUri(file) else insertImageUri(file)
}

/** content://media/external/images/media/id */
fun Context.takeCropUri(configs: GalleryConfigs): Uri? {
    val file: File = when {
        hasQ() -> File(configs.fileConfig.cropPath + File.separator + configs.takeCropName)
        else -> Environment.getExternalStoragePublicDirectory(configs.fileConfig.cropPath).absolutePath
            .mkdirsFile(configs.takeCropName)
    }
    return insertImageUri(file)
}

internal fun Context.insertImageUri(file: File): Uri? = insertImageUri(ContentValues().apply {
    if (hasQ()) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, file.parent)
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
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

internal fun Context.insertVideoUri(file: File): Uri? = insertVideoUri(ContentValues().apply {
    if (hasQ()) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, file.parent)
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
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
