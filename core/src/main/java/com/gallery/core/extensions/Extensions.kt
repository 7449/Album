package com.gallery.core.extensions

import android.provider.MediaStore
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.scan.extensions.ScanFileEntity

/** 返回拍照文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cameraNameExpand: String get() = "${System.currentTimeMillis()}_${cameraName}.${cameraNameSuffix}"

/** 返回裁剪文件名称 为了防止重复前缀加时间戳 */
val GalleryBundle.cropNameExpand: String get() = "${System.currentTimeMillis()}_${cropName}.${cropNameSuffix}"

/** 是否是视频 */
val GalleryBundle.isVideoScanExpand: Boolean get() = scanType.size == 1 && scanType.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

/** 是否是图片 */
val GalleryBundle.isImageScanExpand: Boolean get() = scanType.size == 1 && scanType.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

/** [ScanFileEntity]转换为[ScanEntity] */
fun ArrayList<ScanFileEntity>.toScanEntity(): ArrayList<ScanEntity> = mapTo(ArrayList()) { ScanEntity(it) }

/** [ScanEntity]转换为[ScanFileEntity] */
fun ArrayList<ScanEntity>.toScanFileEntity(): ArrayList<ScanFileEntity> = mapTo(ArrayList()) { it.delegate }

/** [ScanFileEntity]转换为[ScanEntity] */
fun ScanFileEntity.toScanEntity(): ScanEntity = ScanEntity(this)