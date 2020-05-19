package com.gallery.core.ext

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.kotlin.expand.content.insertImageUriExpand
import androidx.kotlin.expand.util.copyFileExpand
import androidx.kotlin.expand.util.lowerVersionFileExpand
import androidx.kotlin.expand.util.mkdirsFileExpand
import androidx.kotlin.expand.version.hasQExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.ui.fragment.ScanFragment
import java.io.File

//content://media/external/images/media/id
fun Context.galleryPathToUri(galleryBundle: GalleryBundle): Uri? {
    val fileName = "${galleryBundle.cameraName}.${galleryBundle.cameraNameSuffix}"
    val file: File = when {
        hasQExpand() -> File("", fileName)
        galleryBundle.cameraPath.isNullOrEmpty() -> lowerVersionFileExpand(fileName, galleryBundle.relativePath)
        else -> galleryBundle.cameraPath.mkdirsFileExpand(fileName)
    }
    return insertImageUriExpand(file, galleryBundle.relativePath)
}

//file:///path/xxxxx.jpg
fun Context.cropPathToUri(galleryBundle: GalleryBundle): Uri {
    val fileName = "${galleryBundle.cropName}.${galleryBundle.cropNameSuffix}"
    val file: File = when {
        hasQExpand() -> File(externalCacheDir, fileName)
        galleryBundle.cropPath.isNullOrEmpty() -> lowerVersionFileExpand(fileName, galleryBundle.relativePath)
        else -> galleryBundle.cropPath.mkdirsFileExpand(fileName)
    }
    return Uri.fromFile(file)
}

@SuppressLint("InlinedApi")
fun Context.saveCropToGalleryLegacy(
        cropUri: Uri,
        cropName: String,
        cropNameSuffix: String,
        relativePath: String
): Uri? {
    return copyFileExpand(cropUri, "$cropName.$cropNameSuffix", relativePath)
}

@Suppress("NOTHING_TO_INLINE")
inline fun ScanFragment.cropResultOk(
        cropUri: Uri?,
        galleryBundle: GalleryBundle
) {
    cropUri?.let {
        if (!hasQExpand() || !galleryBundle.cropSuccessSave) {
            onCropSuccess(it)
            return
        }
        requireActivity().saveCropToGalleryLegacy(it, galleryBundle.cropName, galleryBundle.cropNameSuffix, galleryBundle.relativePath)?.let { androidQUri ->
            onCropSuccess(androidQUri)
            File(it.path.toString()).delete()
        } ?: onCropSuccess(it)
    } ?: onCropError(null)
}