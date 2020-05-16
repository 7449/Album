package com.gallery.core.ext

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.kotlin.expand.content.findUriByFileExpand
import androidx.kotlin.expand.content.insertImageUriExpand
import androidx.kotlin.expand.version.hasQExpand
import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun Context.galleryPathToUri(
        cameraPath: String?,
        cameraName: String,
        cameraNameSuffix: String,
        relativePath: String
): Uri {
    val fileName = "$cameraName.$cameraNameSuffix"
    val file: File = when {
        hasQExpand() -> File(cameraPath.toString(), fileName)
        cameraPath.isNullOrEmpty() -> lowerVersionFile(fileName, relativePath)
        else -> galleryPathFile(cameraPath, fileName)
    }
    //content://media/external/images/media/id
    return findUriByFileExpand(file, relativePath)
}

fun Context.uCropPathToUri(
        uCropPath: String?,
        uCropName: String,
        uCropNameSuffix: String,
        relativePath: String
): Uri {
    val fileName = "$uCropName.$uCropNameSuffix"
    val file: File = when {
        hasQExpand() -> File(externalCacheDir, fileName)
        uCropPath.isNullOrEmpty() -> lowerVersionFile(fileName, relativePath)
        else -> galleryPathFile(uCropPath, fileName)
    }
    return Uri.fromFile(file)
}

@SuppressLint("InlinedApi")
fun Context.saveCropToGalleryLegacy(
        cropUri: Uri,
        uCropName: String,
        uCropNameSuffix: String,
        relativePath: String
): Uri? {
    if (!hasQExpand()) {
        return null
    }
    val contentValues = ContentValues()
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$uCropName.$uCropNameSuffix")
    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    val uri: Uri = insertImageUriExpand(contentValues)
    val outStream: OutputStream = contentResolver.openOutputStream(uri) ?: return null
    val inStream: InputStream = contentResolver.openInputStream(cropUri) ?: return null
    outStream.use { out -> inStream.use { input -> input.copyTo(out) } }
    return uri
}

internal fun galleryPathFile(path: String, child: String): File {
    val pathFile = File(path, child)
    if (!pathFile.exists()) {
        pathFile.mkdirs()
    }
    return pathFile
}

internal fun Context.lowerVersionFile(fileName: String, relativePath: String = Environment.DIRECTORY_DCIM): File {
    val path: String = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
        @Suppress("DEPRECATION")
        Environment.getExternalStoragePublicDirectory(relativePath).path
    } else {
        cacheDir.path
    }
    return File(path, fileName)
}