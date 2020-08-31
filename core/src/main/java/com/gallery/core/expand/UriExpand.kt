package com.gallery.core.expand

import android.content.Context
import android.net.Uri
import androidx.kotlin.expand.content.insertImageUriExpand
import androidx.kotlin.expand.content.insertVideoUriExpand
import androidx.kotlin.expand.util.lowerVersionFileExpand
import androidx.kotlin.expand.util.mkdirsFileExpand
import androidx.kotlin.expand.version.hasQExpand
import com.gallery.core.GalleryBundle
import java.io.File

/** content://media/external/images/media/id or content://media/external/video/media/id */
fun Context.cameraUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File("", galleryBundle.cameraNameExpand)
        galleryBundle.cameraPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cameraNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cameraPath.mkdirsFileExpand(galleryBundle.cameraNameExpand)
    }
    return if (galleryBundle.isVideoScan) insertVideoUriExpand(file) else insertImageUriExpand(file)
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