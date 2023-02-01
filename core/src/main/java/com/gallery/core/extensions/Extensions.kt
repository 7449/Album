package com.gallery.core.extensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.widget.Toast
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gallery.core.GalleryConfigs
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.impl.file.FileScanEntity
import java.io.File

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

@ChecksSdkIntAtLeast(api = 29)
fun hasQ(): Boolean = SDK_INT >= 29

fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

fun Context.drawable(@DrawableRes id: Int): Drawable? =
    if (id == 0) null else ContextCompat.getDrawable(this, id)

fun Context.square(count: Int): Int = resources.displayMetrics.widthPixels / count

fun String?.toast(context: Context?) {
    if (!isNullOrEmpty() && context != null) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

fun Context.openVideo(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

fun ArrayList<FileScanEntity>.toScanEntity(): ArrayList<ScanEntity> =
    mapTo(ArrayList()) { ScanEntity(it) }

fun ArrayList<ScanEntity>.toFileEntity(): ArrayList<FileScanEntity> =
    mapTo(ArrayList()) { it.delegate }

fun FileScanEntity.toScanEntity(): ScanEntity = ScanEntity(this)

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