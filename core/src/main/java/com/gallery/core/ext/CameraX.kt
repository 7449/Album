package com.gallery.core.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.gallery.core.constant.GalleryCameraConst
import java.io.File

//activity 打开相机
fun Activity.openCamera(fileUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) GalleryCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, fileUri, video)

//fragment 打开相机
fun Fragment.openCamera(fileUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) GalleryCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, fileUri, video)

//打开相机(需要提前请求权限)）
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
    val fileName = System.currentTimeMillis().toString() + "_" + name + "." + suffix
    if (path != null && path.isNotEmpty()) {
        val pathFile = File(path)
        if (!pathFile.exists()) {
            pathFile.mkdirs()
        }
        return File(path, fileName)
    }
    return File(
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
            } else {
                externalCacheDir?.path
            },
            fileName)
}