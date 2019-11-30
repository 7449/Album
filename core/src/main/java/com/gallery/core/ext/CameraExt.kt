@file:Suppress("DEPRECATION")

package com.gallery.core.ext

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.gallery.core.constant.GalleryCameraConst
import com.gallery.scan.args.Columns
import com.gallery.scan.args.ScanConst
import java.io.File

fun Activity.openCamera(fileUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) GalleryCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, fileUri, video)

fun Fragment.openCamera(fileUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) GalleryCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, fileUri, video)

internal fun openCamera(root: Any, fileUri: Uri, video: Boolean): Int {
    val activity = when (root) {
        is Activity -> root
        is Fragment -> root.activity ?: throw KotlinNullPointerException()
        else -> throw KotlinNullPointerException()
    }
    val intent = if (video) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(activity.packageManager) == null) {
        return GalleryCameraConst.CAMERA_ERROR
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
    when (root) {
        is Activity -> root.startActivityForResult(intent, GalleryCameraConst.CAMERA_REQUEST_CODE)
        is Fragment -> root.startActivityForResult(intent, GalleryCameraConst.CAMERA_REQUEST_CODE)
    }
    return GalleryCameraConst.CAMERA_SUCCESS
}

fun Context.galleryPathFile(path: String?, name: String, scanType: Int = ScanConst.IMAGE): File {
    val suffix = if (scanType == ScanConst.VIDEO) "mp4" else "jpg"
    val fileName = "${System.currentTimeMillis()}_$name.$suffix"
    if (hasQ()) {
        return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path, name)
    }
    if (path != null && path.isNotEmpty()) {
        val pathFile = File(path)
        if (!pathFile.exists()) {
            pathFile.mkdirs()
        }
        return File(path, fileName)
    }
    return File(if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
    } else {
        externalCacheDir?.path
    }, fileName)
}

fun Context.cropPathFile(path: String?, name: String, scanType: Int = ScanConst.IMAGE): File {
    val suffix = if (scanType == ScanConst.VIDEO) "mp4" else "jpg"
    val fileName = "${System.currentTimeMillis()}_$name.$suffix"
    if (path != null && path.isNotEmpty()) {
        val pathFile = File(path)
        if (!pathFile.exists()) {
            pathFile.mkdirs()
        }
        return File(path, fileName)
    }
    return File(if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
    } else {
        externalCacheDir?.path
    }, fileName)
}

fun Context.uriToFilePath(uri: Uri): String? {
    contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DATA), null, null, null).use {
        val cursor = it ?: return null
        val dataColumnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            return cursor.getString(dataColumnIndex)
        }
    }
    return null
}

fun Context.getUriId(uri: Uri): Long {
    val split = uri.toString().split("/")
    var id = 0L
    try {
        id = split[split.size - 1].toLong()
    } catch (e: Exception) {
        contentResolver.query(uri, arrayOf(Columns.ID), null, null, null).use {
            val cursor = it ?: return 0L
            val dataColumnIndex = cursor.getColumnIndex(Columns.ID)
            while (cursor.moveToNext()) {
                id = cursor.getLong(dataColumnIndex)
            }
        }
    }
    return id
}

fun Context.getFileUri(file: File) = insertImage(ContentValues().apply {
    if (hasQ()) {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
    } else {
        put(MediaStore.MediaColumns.DATA, file.path)
    }
})