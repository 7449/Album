package com.gallery.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.extensions.ScanFileEntity
import java.io.File

/** 是否为亮色 */
fun Int.isLightColorExpand(): Boolean = ColorUtils.calculateLuminance(this) >= 0.5

/** 获取颜色 */
fun Int.colorExpand(activity: Context): Int = activity.colorExpand(this)

/** 获取安全Bundle */
fun Bundle?.orEmptyExpand(): Bundle = this ?: Bundle.EMPTY

/** 获取颜色 */
fun Context.colorExpand(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

/** 获取Drawable */
fun Context.drawableExpand(@DrawableRes id: Int): Drawable? = ContextCompat.getDrawable(this, id)

/** 获取自定义颜色Drawable */
fun Context.minimumDrawableExpand(@DrawableRes id: Int, @ColorInt color: Int): Drawable? = drawableExpand(id)?.minimumWidthAndHeightDrawableExpand(color)

/** 获取自定义Drawable */
fun Drawable.minimumWidthAndHeightDrawableExpand(@ColorInt color: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
    return this
}

/** 获取计算的Item宽高 */
fun Activity.squareExpand(count: Int): Int {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels / count
}

/** 设置状态栏颜色 */
fun Window.statusBarColorExpand(@ColorInt color: Int) {
    if (hasMExpand()) {
        statusBarColor = color
        if (color.isLightColorExpand()) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}

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

/** 返回拍照文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cameraNameExpand: String get() = "${System.currentTimeMillis()}_${cameraName}.${cameraNameSuffix}"

/** 返回裁剪文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cropNameExpand: String get() = "${System.currentTimeMillis()}_${cropName}.${cropNameSuffix}"

/** 是否是视频 */
val GalleryBundle.isVideoScanExpand: Boolean get() = scanType.size == 1 && scanType.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

/** 是否是图片 */
val GalleryBundle.isImageScanExpand: Boolean get() = scanType.size == 1 && scanType.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

/** [ScanFileEntity]转换为[ScanEntity] */
fun ArrayList<ScanFileEntity>.toScanEntity(): ArrayList<ScanEntity> = mapTo(ArrayList()) { ScanEntity(it) }

/** [ScanEntity]转换为[ScanFileEntity] */
fun ArrayList<ScanEntity>.toScanFileEntity(): ArrayList<ScanFileEntity> = mapTo(ArrayList()) { it.delegate }

/** [ScanFileEntity]转换为[ScanEntity] */
fun ScanFileEntity.toScanEntity(): ScanEntity = ScanEntity(this)

/** content://media/external/images/media/id or content://media/external/video/media/id */
fun Context.cameraUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File("", galleryBundle.cameraNameExpand)
        galleryBundle.cameraPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cameraNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cameraPath.mkdirsFileExpand(galleryBundle.cameraNameExpand)
    }
    return if (galleryBundle.isVideoScanExpand) insertVideoUriExpand(file) else insertImageUriExpand(file)
}

/** content://media/external/images/media/id */
fun Context.cropUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File("", galleryBundle.cropNameExpand)
        galleryBundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cropNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cropPath.mkdirsFileExpand(galleryBundle.cropNameExpand)
    }
    return insertImageUriExpand(file)
}

/** file:///path/xxxxx.jpg */
@Deprecated("no support for higher version, annoying version support")
fun Context.cropUriExpand2(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File(externalCacheDir, galleryBundle.cropNameExpand)
        galleryBundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cropNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cropPath.mkdirsFileExpand(galleryBundle.cropNameExpand)
    }
    return Uri.fromFile(file)
}