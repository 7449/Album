@file:Suppress("DEPRECATION")

package com.gallery.core.expand

import android.content.Context
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.GalleryBundle
import com.gallery.scan.types.ScanType

/** 返回拍照文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cameraNameExpand: String
    get() = "${System.currentTimeMillis()}_${cameraName}.${cameraNameSuffix}"

/** 返回裁剪文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cropNameExpand: String
    get() = "${System.currentTimeMillis()}_${cropName}.${cropNameSuffix}"

/** 是否是纯视频 */
val GalleryBundle.isVideoScan: Boolean
    get() = scanType == ScanType.VIDEO

/** 是否是纯图片 */
val GalleryBundle.isImageScan: Boolean
    get() = scanType == ScanType.IMAGE

/** toast 空字符串屏蔽 */
fun String?.safeToastExpand(context: Context?) {
    if (!isNullOrEmpty()) {
        context?.let { toastExpand(it) }
    }
}