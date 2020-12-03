package com.gallery.core.extensions

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.scan.extensions.ScanFileEntity
import java.io.File

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

/** content://media/external/images/media/id or content://media/external/video/media/id */
fun Context.cameraUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File("", galleryBundle.cameraNameExpand)
        galleryBundle.cameraPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cameraNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cameraPath.mkdirsFileExpand(galleryBundle.cameraNameExpand)
    }
    return if (galleryBundle.isVideoScanExpand) insertVideoUriExpand(file) else insertImageUriExpand(file)
}

/** content://media/external/images/media/id */
fun Context.cropUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File("", galleryBundle.cropNameExpand)
        galleryBundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cropNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cropPath.mkdirsFileExpand(galleryBundle.cropNameExpand)
    }
    return insertImageUriExpand(file)
}

/** file:///path/xxxxx.jpg */
@Deprecated("no support for higher version, annoying version support")
fun Context.cropUriExpand2(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File(externalCacheDir, galleryBundle.cropNameExpand)
        galleryBundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cropNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cropPath.mkdirsFileExpand(galleryBundle.cropNameExpand)
    }
    return Uri.fromFile(file)
}