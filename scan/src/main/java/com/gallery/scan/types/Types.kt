package com.gallery.scan.types

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.Columns

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = -1111L

val ScanEntity.externalUri: Uri
    get() = when (mediaType) {
        Columns.IMAGE -> ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        Columns.VIDEO -> ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
        Columns.AUDIO -> ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        else -> Uri.EMPTY
    }

/** 这个操作比较耗时,目前没有找到高版本上判断文件是否存在的有效方法 */
fun ScanEntity.isFileExists(context: Context): Boolean {
    return runCatching { context.contentResolver.openAssetFileDescriptor(externalUri, "r")?.close() }.isSuccess
}