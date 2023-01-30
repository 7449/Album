package com.gallery.core.extensions

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
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

/** 是否高于等于Q */
@ChecksSdkIntAtLeast(api = 29)
fun hasQExpand(): Boolean = SDK_INT >= 29

/** 获取安全Bundle */
fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

/** 获取Drawable */
fun Context.drawable(@DrawableRes id: Int): Drawable? =
    if (id == 0) null else ContextCompat.getDrawable(this, id)

/** 获取计算的Item宽高 */
fun Context.square(count: Int): Int = resources.displayMetrics.widthPixels / count

/** 安全Toast(过滤空数据) */
fun String?.toast(context: Context?) {
    if (!isNullOrEmpty() && context != null) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

/** 打开视频(使用系统默认视频播放器) */
fun Context.openVideo(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

/** [FileScanEntity]转换为[ScanEntity] */
fun ArrayList<FileScanEntity>.toScanEntity(): ArrayList<ScanEntity> =
    mapTo(ArrayList()) { ScanEntity(it) }

/** [ScanEntity]转换为[FileScanEntity] */
fun ArrayList<ScanEntity>.toFileEntity(): ArrayList<FileScanEntity> =
    mapTo(ArrayList()) { it.delegate }

/** [FileScanEntity]转换为[ScanEntity] */
fun FileScanEntity.toScanEntity(): ScanEntity = ScanEntity(this)

/** content://media/external/images/media/id or content://media/external/video/media/id */
fun Context.takePictureUri(configs: GalleryConfigs): Uri? {
    val file: File = when {
        // Android Q
        hasQExpand() -> File("", configs.takePictureName)
        // 获取file.path
        configs.picturePathAndCropPath.first.isEmpty() -> lowerVersionFile(configs.takePictureName)
        else -> configs.picturePathAndCropPath.first
            .mkdirsFile(configs.takePictureName)
    }
    return if (configs.isScanVideoMedia)
        insertVideoUri(file, configs.relativePath)
    else
        insertImageUri(file, configs.relativePath)
}

/** content://media/external/images/media/id */
fun Context.takeCropUri(configs: GalleryConfigs): Uri? {
    val file: File = when {
        // Android Q
        hasQExpand() -> File("", configs.takeCropName)
        // 获取file.path
        configs.picturePathAndCropPath.second.isEmpty() -> lowerVersionFile(configs.takeCropName)
        else -> configs.picturePathAndCropPath.second
            .mkdirsFile(configs.takeCropName)
    }
    return insertImageUri(file, configs.relativePath)
}