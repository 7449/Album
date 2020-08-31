package com.gallery.scan.types

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.content.findPathByUriExpand
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

/** 这个操作比较耗时,目前没有找到高版本上判断文件是否存在的有效方法 */
fun ScanEntity.isFileExistsExpand(context: Context): Boolean {
    return runCatching { context.contentResolver.openAssetFileDescriptor(externalUriExpand, "r")?.close() }.isSuccess
}

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

/** 删除Uri */
fun Uri.deleteExpand(context: Context) {
    runCatching {
        context.contentResolver.delete(this, null, null)
    }.onSuccess { Log.i("gallery", "delete uri success") }.onFailure { Log.e("gallery", "delete uri failure:$this") }
}

/** Uri path */
fun Uri.filePathExpand(context: Context): String {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> context.findPathByUriExpand(this).orEmpty()
        ContentResolver.SCHEME_FILE -> path.orEmpty()
        else -> throw RuntimeException("unsupported uri")
    }
}

/** 扫描数据库 */
fun FragmentActivity.scanFileExpand(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFileExpand(uri.filePathExpand(this), action)
}

/** 扫描数据库 */
fun FragmentActivity.scanFileExpand(path: String, action: (uri: Uri) -> Unit) {
    MediaScannerConnection.scanFile(this, arrayOf(path), null) { _: String?, uri: Uri? ->
        runOnUiThread {
            uri ?: return@runOnUiThread
            action.invoke(uri)
        }
    }
}