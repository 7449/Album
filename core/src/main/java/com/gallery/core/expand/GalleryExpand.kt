@file:Suppress("DEPRECATION")

package com.gallery.core.expand

import android.content.ContentUris
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import androidx.kotlin.expand.app.runOnUiThreadExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanType
import com.gallery.scan.args.Columns

/** 返回拍照文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cameraNameExpand: String
    get() = "${System.currentTimeMillis()}_${cameraName}.${cameraNameSuffix}"

/** 返回裁剪文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cropNameExpand: String
    get() = "${System.currentTimeMillis()}_${cropName}.${cropNameSuffix}"

/** 是否是纯视频 */
val GalleryBundle.isVideoScan: Boolean
    get() = scanType == ScanType.VIDEO

/** 文件是否是动态图 */
val ScanEntity.isGif: Boolean
    get() = mimeType.contains("gif")

/** 文件是否是视频 */
val ScanEntity.isVideo: Boolean
    get() = mediaType == "3"

/** 获取文件的Uri */
val ScanEntity.externalUri: Uri
    get() = if (mediaType == Columns.IMAGE) {
        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    } else {
        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
    }

/** 是否是扫描全部的Id */
fun Long.isScanAll(): Boolean = this == SCAN_ALL

/** 打开相机的自定义数据携带体 */
class CameraUri(val type: Int, val uri: Uri)

/** 扫描数据库 */
fun ScanFragment.scanFile(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFile(uri.filePath(requireContext()), action)
}

/** 扫描数据库 */
fun ScanFragment.scanFile(path: String, action: (uri: Uri) -> Unit) {
    MediaScannerConnection.scanFile(requireContext(), arrayOf(path), null) { _: String?, uri: Uri? ->
        runOnUiThreadExpand {
            uri ?: return@runOnUiThreadExpand
            action.invoke(uri)
        }
    }
}
