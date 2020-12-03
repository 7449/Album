package com.gallery.scan.extensions

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import com.gallery.scan.types.ScanType

/** 是否是扫描全部的Id */
fun Long.isScanAllExpand(): Boolean = this == ScanType.SCAN_ALL

/** 是否是空扫描 */
fun Long.isScanNoNeExpand(): Boolean = this == ScanType.SCAN_NONE

/** 获取可使用的多个扫描Bundle */
fun Long.multipleScanExpand(): Bundle = Bundle().apply { putLong(MediaStore.Files.FileColumns.PARENT, this@multipleScanExpand) }

/** 获取可使用的单个扫描Bundle */
fun Long.singleScanExpand(): Bundle = Bundle().apply { putLong(BaseColumns._ID, this@singleScanExpand) }

/** 获取可使用的uri */
fun Long.externalUriExpand(mediaType: String): Uri {
    return when (mediaType) {
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString() -> ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, this)
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString() -> ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, this)
        MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString() -> ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, this)
        else -> Uri.EMPTY
    }
}

/** 是否是动态图 */
val String.isGifExpand: Boolean
    get() = contains("gif")

/** 是否是视频 */
val String.isVideoExpand: Boolean
    get() = this == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

/** 是否是图片 */
val String.isImageExpand: Boolean
    get() = this == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()

/** 是否是音频 */
val String.isAudioExpand: Boolean
    get() = this == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

internal fun Cursor?.getIntOrDefault(columnName: String, defaultValue: Int = 0): Int =
        getValueOrDefault(columnName, { defaultValue }) { it.getInt(it.getColumnIndex(columnName)) }

internal fun Cursor?.getLongOrDefault(columnName: String, defaultValue: Long = 0.toLong()): Long =
        getValueOrDefault(columnName, { defaultValue }) { it.getLong(it.getColumnIndex(columnName)) }

internal fun Cursor?.getStringOrDefault(columnName: String, defaultValue: String = ""): String =
        getValueOrDefault(columnName, { defaultValue }) { it.getString(it.getColumnIndex(columnName)) }

internal inline fun <T> Cursor?.getValueOrDefault(name: String, valueNull: () -> T, a: (c: Cursor) -> T): T =
        if (this?.isNull(getColumnIndex(name)) == false) a.invoke(this) else valueNull.invoke()