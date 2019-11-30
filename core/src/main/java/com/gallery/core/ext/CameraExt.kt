package com.gallery.core.ext

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.gallery.core.constant.GalleryCameraConst
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.Columns
import java.io.File

fun Activity.openCamera(fileUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) GalleryCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, fileUri, video)

fun Fragment.openCamera(fileUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) GalleryCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, fileUri, video)

internal fun openCamera(root: Any, fileUri: Uri, video: Boolean): Int {
    val activity = when (root) {
        is Activity -> root
        is Fragment -> root.activity
        else -> throw KotlinNullPointerException()
    }
    val intent = if (video) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (activity == null || intent.resolveActivity(activity.packageManager) == null) {
        return GalleryCameraConst.CAMERA_ERROR
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
    when (root) {
        is Activity -> root.startActivityForResult(intent, GalleryCameraConst.CAMERA_REQUEST_CODE)
        is Fragment -> root.startActivityForResult(intent, GalleryCameraConst.CAMERA_REQUEST_CODE)
    }
    return GalleryCameraConst.CAMERA_SUCCESS
}

fun Context.fileExists(uri: Uri): Boolean {
    contentResolver.query(uri, arrayOf(Columns.ID), null, null, null).use {
        return it?.moveToNext() ?: false
    }
}

fun Context.scanFilePath(uri: Uri, fileProviderPath: String): String? {
    when {
        hasQ() -> contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null).use {
            val index = it?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) ?: -11
            it?.moveToFirst()
            return if (index == -11) {
                null
            } else {
                it?.getString(index)
            }
        }
        hasN() -> {
            return fileProviderPath
        }
        else -> return uri.path
    }
}

fun Context.galleryPathFile(path: String?, name: String, suffix: String): File {
    val fileName = "$name.$suffix"
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


@Deprecated("deprecated")
fun Context.insertImage(contentValues: ContentValues) = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
} else {
    contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
}

//获取content Uri
@Deprecated("deprecated")
fun Context.externalUri(file: File?): Uri = when {
    hasQ() -> insertImage(ContentValues())
    hasN() -> FileProvider.getUriForFile(this, "$packageName.GalleryProvider", file
            ?: throw KotlinNullPointerException())
    else -> Uri.fromFile(file)
}

//获取content Uri
@Deprecated("deprecated")
fun Context.externalUri(path: String?): Uri = when {
    hasQ() -> insertImage(ContentValues())
    hasN() -> FileProvider.getUriForFile(this, "$packageName.GalleryProvider", File(path.orEmpty()))
    else -> Uri.fromFile(File(path.orEmpty()))
}

@Deprecated("deprecated")
fun ArrayList<ScanEntity>.mergeEntity(selectEntity: ArrayList<ScanEntity>) = also {
    forEach { it.isCheck = false }
    selectEntity.forEach { select -> this.find { it.id == select.id }?.isCheck = true }
}