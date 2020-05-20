package com.gallery.core.expand

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.kotlin.expand.content.insertImageUriExpand
import androidx.kotlin.expand.content.insertVideoUriExpand
import androidx.kotlin.expand.os.tryCatchOrDefault
import androidx.kotlin.expand.util.lowerVersionFileExpand
import androidx.kotlin.expand.util.mkdirsFileExpand
import androidx.kotlin.expand.version.hasQExpand
import com.gallery.core.GalleryBundle
import java.io.File

/** content://media/external/images/media/id */
fun Context.cameraUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File("", galleryBundle.cameraNameExpand)
        galleryBundle.cameraPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cameraNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cameraPath.mkdirsFileExpand(galleryBundle.cameraNameExpand)
    }
    return if (galleryBundle.isVideoScan) insertVideoUriExpand(file) else insertImageUriExpand(file)
}

/** file:///path/xxxxx.jpg */
fun Context.cropUriExpand(galleryBundle: GalleryBundle): Uri? {
    val file: File = when {
        hasQExpand() -> File(externalCacheDir, galleryBundle.cropNameExpand)
        galleryBundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(galleryBundle.cropNameExpand, galleryBundle.relativePath)
        else -> galleryBundle.cropPath.mkdirsFileExpand(galleryBundle.cropNameExpand)
    }
    return Uri.fromFile(file)
}

/** 删除Uri */
fun Uri.reset(context: Context) {
    if (!path.isNullOrEmpty() && scheme == ContentResolver.SCHEME_CONTENT) {
        tryCatchOrDefault({
            context.contentResolver.delete(this, null, null)
            Unit
        }) { /** delete uri error */ }
    }
}