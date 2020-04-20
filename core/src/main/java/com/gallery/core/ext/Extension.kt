package com.gallery.core.ext

import android.content.Context
import android.os.Environment
import com.gallery.scan.ScanType
import com.kotlin.x.hasQ
import java.io.File

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
