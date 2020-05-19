@file:Suppress("DEPRECATION")

package com.gallery.core.ext

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.kotlin.expand.content.findPathByUriExpand
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanType

/** 文件是否是动态图 */
fun ScanEntity.isGif(): Boolean = mimeType.contains("gif")

/** 文件是否是视频 */
fun ScanEntity.isVideo(): Boolean = mediaType == "3"

/** 是否是扫描全部的Id */
fun Long.isScanAll(): Boolean = this == SCAN_ALL

/** 根据[Uri]获取文件路径 */
fun Context.findPathByUriExpand(uri: Uri): String? =
        when {
            ContentResolver.SCHEME_CONTENT == uri.scheme -> findPathByUriExpand(uri)
            ContentResolver.SCHEME_FILE == uri.scheme -> uri.path
            else -> null
        }

/** 打开相机的自定义数据携带体 */
class CameraUri(val type: ScanType, val uri: Uri)