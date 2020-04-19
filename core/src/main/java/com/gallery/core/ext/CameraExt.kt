package com.gallery.core.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.gallery.core.CameraStatus
import com.gallery.core.callback.IGallery
import com.gallery.scan.ScanType
import java.io.File

fun Activity.openCamera(fileUri: Uri, video: Boolean) = if (!permissionCamera() || !permissionStorage()) CameraStatus.PERMISSION else openCamera(this, fileUri, video)

fun Fragment.openCamera(fileUri: Uri, video: Boolean) = if (!permissionCamera() || !permissionStorage()) CameraStatus.PERMISSION else openCamera(this, fileUri, video)

internal fun openCamera(root: Any, fileUri: Uri, video: Boolean): CameraStatus {
    val activity = when (root) {
        is Activity -> root
        is Fragment -> root.requireActivity()
        else -> throw KotlinNullPointerException()
    }
    val intent = if (video) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(activity.packageManager) == null) {
        return CameraStatus.ERROR
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
    when (root) {
        is Activity -> root.startActivityForResult(intent, IGallery.CAMERA_REQUEST_CODE)
        is Fragment -> root.startActivityForResult(intent, IGallery.CAMERA_REQUEST_CODE)
    }
    return CameraStatus.SUCCESS
}

@Suppress("DEPRECATION")
fun Context.galleryPathFile(path: String?, name: String, scanType: ScanType = ScanType.IMAGE): File {
    val suffix = if (scanType == ScanType.VIDEO) "mp4" else "jpg"
    val fileName = "${System.currentTimeMillis()}_$name.$suffix"
    if (hasQ()) {
        //Q只用到了File.getName()
        //具体可见findUriByFile(file)
        //val contentValues = ContentValues().apply {
        //    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        //    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        //}
        return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path, fileName)
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

fun Context.cropPathFile(path: String?, name: String, scanType: ScanType = ScanType.IMAGE): File {
    val suffix = if (scanType == ScanType.VIDEO) "mp4" else "jpg"
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