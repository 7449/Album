package com.gallery.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.impl.file.FileScanEntity
import java.io.File

/** 是否高于等于Q */
fun hasQExpand(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

/** 获取安全Bundle */
fun Bundle?.orEmptyExpand(): Bundle = this ?: Bundle.EMPTY

/** 获取Drawable */
fun Context.drawableExpand(@DrawableRes id: Int): Drawable? =
    if (id == 0) null else ContextCompat.getDrawable(this, id)

/** 获取计算的Item宽高 */
fun Activity.squareExpand(count: Int): Int = resources.displayMetrics.widthPixels / count

/** 安全Toast(过滤空数据) */
fun String?.safeToastExpand(context: Context?) {
    if (!isNullOrEmpty() && context != null) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

/** 打开视频(使用系统默认视频播放器) */
fun Context.openVideoExpand(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

/** 返回拍照文件名称 */
val GalleryBundle.cameraNameExpand: String get() = "${cameraName}_${System.currentTimeMillis()}.${cameraNameSuffix}"

/** 返回裁剪文件名称 */
val GalleryBundle.cropNameExpand: String get() = "${cropName}_${System.currentTimeMillis()}.${cropNameSuffix}"

/** 是否是视频 */
val GalleryBundle.isVideoScanExpand: Boolean
    get() = scanType.size == 1 && scanType.contains(
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
    )

/** 是否是图片 */
val GalleryBundle.isImageScanExpand: Boolean
    get() = scanType.size == 1 && scanType.contains(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
    )

/** [FileScanEntity]转换为[ScanEntity] */
fun ArrayList<FileScanEntity>.toScanEntity(): ArrayList<ScanEntity> =
    mapTo(ArrayList()) { ScanEntity(it) }

/** [ScanEntity]转换为[FileScanEntity] */
fun ArrayList<ScanEntity>.toScanFileEntity(): ArrayList<FileScanEntity> =
    mapTo(ArrayList()) { it.delegate }

/** [FileScanEntity]转换为[ScanEntity] */
fun FileScanEntity.toScanEntity(): ScanEntity = ScanEntity(this)

/** content://media/external/images/media/id or content://media/external/video/media/id */
fun Context.cameraUriExpand(bundle: GalleryBundle): Uri? {
    val file: File = when {
        // Android Q
        hasQExpand() -> File("", bundle.cameraNameExpand)
        // 获取file.path
        bundle.cameraPath.isNullOrEmpty() -> lowerVersionFileExpand(bundle.cameraNameExpand)
        else -> bundle.cameraPath.mkdirsFileExpand(bundle.cameraNameExpand)
    }
    return if (bundle.isVideoScanExpand)
        insertVideoUriExpand(file, bundle.relativePath)
    else
        insertImageUriExpand(file, bundle.relativePath)
}

/** content://media/external/images/media/id */
fun Context.cropUriExpand(bundle: GalleryBundle): Uri? {
    val file: File = when {
        // Android Q
        hasQExpand() -> File("", bundle.cropNameExpand)
        // 获取file.path
        bundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(bundle.cropNameExpand)
        else -> bundle.cropPath.mkdirsFileExpand(bundle.cropNameExpand)
    }
    return insertImageUriExpand(file, bundle.relativePath)
}