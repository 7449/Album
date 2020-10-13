package com.gallery.core.extensions

import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.permission.checkCameraPermissionExpand
import androidx.kotlin.expand.os.permission.checkWritePermissionExpand

/** check permission */
fun Fragment.checkPermissionAndRequestWriteExpand(launcher: ActivityResultLauncher<String>): Boolean {
    return if (!checkWritePermissionExpand()) {
        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        false
    } else {
        true
    }
}

/** check permission */
fun Fragment.checkPermissionAndRequestCameraExpand(launcher: ActivityResultLauncher<String>): Boolean {
    return if (!checkCameraPermissionExpand()) {
        launcher.launch(Manifest.permission.CAMERA)
        false
    } else {
        true
    }
}

/** check camera expand */
fun Fragment.checkCameraStatusExpand(uri: CameraUri, action: (uri: CameraUri) -> Unit): CameraStatus {
    val intent: Intent = if (uri.type.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    else Intent(MediaStore.ACTION_VIDEO_CAPTURE)
    return intent.resolveActivity(requireActivity().packageManager)?.let {
        action.invoke(uri)
        CameraStatus.SUCCESS
    } ?: CameraStatus.ERROR
}