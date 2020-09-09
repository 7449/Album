package com.gallery.scan.types

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.Columns

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = (-11111112).toLong()

/** 获取可使用的uri */
val ScanEntity.externalUriExpand: Uri
    get() = when (mediaType) {
        Columns.IMAGE -> ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        Columns.VIDEO -> ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
        Columns.AUDIO -> ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        else -> Uri.EMPTY
    }

/** 文件是否是动态图 */
val ScanEntity.isGifExpand: Boolean
    get() = mimeType.contains("gif")

/** 文件是否是视频 */
val ScanEntity.isVideoExpand: Boolean
    get() = mediaType == Columns.VIDEO

/** 文件是否是图片 */
val ScanEntity.isImageExpand: Boolean
    get() = mediaType == Columns.IMAGE

/** 文件是否是音频 */
val ScanEntity.isAudioExpand: Boolean
    get() = mediaType == Columns.AUDIO

/** postValue */
fun <T> MutableLiveData<T>.postValueExpand(value: T) {
    if (hasObservers()) {
        postValue(value)
    }
}

/** 是否是扫描全部的Id */
fun Long.isScanAllExpand(): Boolean = this == SCAN_ALL

/** 是否是空扫描 */
fun Long.isScanNoNeExpand(): Boolean = this == SCAN_NONE