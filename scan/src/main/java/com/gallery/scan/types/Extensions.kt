package com.gallery.scan.types

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.gallery.scan.args.ScanMinimumEntity

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = (-11111112).toLong()

/** 获取可使用的uri */
val ScanMinimumEntity.externalUriExpand: Uri
    get() = when (mediaType) {
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString() -> ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString() -> ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
        MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString() -> ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        else -> Uri.EMPTY
    }

/** 文件是否是动态图 */
val ScanMinimumEntity.isGifExpand: Boolean
    get() = mimeType.contains("gif")

/** 文件是否是视频 */
val ScanMinimumEntity.isVideoExpand: Boolean
    get() = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

/** 文件是否是图片 */
val ScanMinimumEntity.isImageExpand: Boolean
    get() = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()

/** 文件是否是音频 */
val ScanMinimumEntity.isAudioExpand: Boolean
    get() = mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()

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