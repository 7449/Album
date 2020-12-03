package com.gallery.core.extensions

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File

fun Context.findIdByUriExpand(uri: Uri): Long = contentResolver.queryIdExpand(uri)

fun Uri?.orEmptyExpand(): Uri = this ?: Uri.EMPTY

fun Uri.filePathExpand(context: Context): String {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> context.findPathByUriExpand(this).orEmpty()
        ContentResolver.SCHEME_FILE -> path.orEmpty()
        else -> throw RuntimeException("unsupported uri")
    }
}

fun Uri.isFileExistsExpand(context: Context): Boolean {
    return runCatching {
        context.contentResolver.openAssetFileDescriptor(this, "r")?.close()
    }.isSuccess
}

fun Uri.deleteExpand(context: Context) {
    runCatching {
        context.contentResolver.delete(this, null, null)
    }
            .onSuccess { Log.i("UriUtils", "delete uri success:$this") }
            .onFailure { Log.e("UriUtils", "delete uri failure:$this") }
}

fun ContentResolver.queryIdExpand(uri: Uri): Long {
    val split = uri.toString().split("/")
    var id = -1L
    runCatching {
        id = split[split.size - 1].toLong()
    }.onFailure {
        queryExpand(uri, MediaStore.Files.FileColumns._ID).use {
            val cursor = it ?: return@use
            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
            }
        }
    }
    return id
}

fun Context.insertImageUriExpand(contentValues: ContentValues): Uri? =
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            null
        }

fun Context.insertImageUriExpand(
        file: File,
        relativePath: String = Environment.DIRECTORY_DCIM,
): Uri? = insertImageUriExpand(ContentValues().apply {
    if (hasQExpand()) {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    } else {
        put(MediaStore.MediaColumns.DATA, file.path)
    }
})

fun Context.insertVideoUriExpand(contentValues: ContentValues): Uri? =
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            null
        }

fun Context.insertVideoUriExpand(
        file: File,
        relativePath: String = Environment.DIRECTORY_DCIM,
): Uri? = insertVideoUriExpand(ContentValues().apply {
    if (hasQExpand()) {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    } else {
        put(MediaStore.MediaColumns.DATA, file.path)
    }
})