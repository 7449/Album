package com.gallery.core.ext

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.camera.CameraStatus
import androidx.kotlin.expand.os.permission.checkCameraPermissionExpand
import androidx.kotlin.expand.os.permission.checkWritePermissionExpand
import com.gallery.core.ui.widget.CameraResultContract
import com.gallery.scan.ScanType

fun Fragment.requestCameraResultLauncherExpand(cancel: () -> Unit, ok: () -> Unit): ActivityResultLauncher<CameraUri> =
        registerForActivityResult(CameraResultContract()) {
            when (it) {
                Activity.RESULT_CANCELED -> cancel.invoke()
                Activity.RESULT_OK -> ok.invoke()
            }
        }

fun Fragment.requestPermissionResultLauncherExpand(action: (isGranted: Boolean) -> Unit): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            action.invoke(it)
        }

fun Fragment.checkPermissionAndRequestWriteExpand(launcher: ActivityResultLauncher<String>): Boolean {
    return if (!checkWritePermissionExpand()) {
        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        false
    } else {
        true
    }
}

fun Fragment.checkPermissionAndRequestCameraExpand(launcher: ActivityResultLauncher<String>): Boolean {
    return if (!checkCameraPermissionExpand()) {
        launcher.launch(Manifest.permission.CAMERA)
        false
    } else {
        true
    }
}

fun Fragment.openCameraExpand(uri: CameraUri, action: (uri: CameraUri) -> Unit): CameraStatus {
    val intent: Intent = if (uri.type == ScanType.VIDEO) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    return intent.resolveActivity(requireActivity().packageManager)?.let {
        action.invoke(uri)
        CameraStatus.SUCCESS
    } ?: CameraStatus.ERROR
}

