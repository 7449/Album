package com.album.scan

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.ColorUtils
import java.io.File

//文件是否存在
fun String.fileExists(): Boolean = parentFile() != null

//路径转[File]
fun String.toFile(): File? = if (isNullOrEmpty()) null else File(this)

//获取父级路径
fun String.parentFile(): File? = toFile()?.let { if (it.exists()) it.parentFile else null }

//如果Bundle为Null则返回一个空的Bundle
fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

//如果Intent为Null则返回一个空的Intent
fun Intent?.orEmpty(): Intent = this ?: Intent()

//判断颜色是否为亮色
fun Int.isLightColor(): Boolean = ColorUtils.calculateLuminance(this) >= 0.5

//获取Album宽高
fun Activity.square(count: Int): Int {
    val display = window.windowManager.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.widthPixels / count
}

//获取Drawable
@Suppress("DEPRECATION")
fun Context.drawable(id: Int, color: Int): Drawable {
    val drawable = resources.getDrawable(id)
    drawable.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
}

fun Context.insertImage(contentValues: ContentValues) = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
} else {
    contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
}

//获取content Uri
@Suppress("HasPlatformType")
fun Context.uri(file: File?) = when {
    hasQ() -> insertImage(ContentValues())
    hasN() -> FileProvider.getUriForFile(this, "$packageName.AlbumProvider", file
            ?: throw KotlinNullPointerException())
    else -> Uri.fromFile(file)
}

//获取content Uri
@Suppress("HasPlatformType")
fun Context.uri(path: String?) = when {
    hasQ() -> insertImage(ContentValues())
    hasN() -> FileProvider.getUriForFile(this, "$packageName.AlbumProvider", File(path.orEmpty()))
    else -> Uri.fromFile(File(path ?: ""))
}