@file:Suppress("DEPRECATION")

package com.gallery.core.expand

import android.provider.MediaStore
import com.gallery.core.GalleryBundle

/** 返回拍照文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cameraNameExpand: String
    get() = "${System.currentTimeMillis()}_${cameraName}.${cameraNameSuffix}"

/** 返回裁剪文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cropNameExpand: String
    get() = "${System.currentTimeMillis()}_${cropName}.${cropNameSuffix}"

/** 是否是视频 */
val GalleryBundle.isVideoScanExpand: Boolean
    get() = scanType.size == 1 && scanType.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

/** 是否是图片 */
val GalleryBundle.isImageScanExpand: Boolean
    get() = scanType.size == 1 && scanType.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

/** 列表管理器 */
enum class LayoutManager {
    LINEAR,
    GRID
}