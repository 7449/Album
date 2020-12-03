package com.gallery.core.extensions

import android.content.Context
import android.os.Environment
import java.io.File

fun String.mkdirsFileExpand(child: String): File {
    val pathFile = File(this, child)
    if (!pathFile.exists()) {
        pathFile.mkdirs()
    }
    return pathFile
}

fun Context.lowerVersionFileExpand(
        fileName: String,
        relativePath: String = Environment.DIRECTORY_DCIM,
): File = File(
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            Environment.getExternalStoragePublicDirectory(relativePath).path
        } else {
            externalCacheDir?.path ?: cacheDir.path
        }, fileName
)
