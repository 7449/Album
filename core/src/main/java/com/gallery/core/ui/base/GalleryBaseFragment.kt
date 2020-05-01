package com.gallery.core.ui.base

import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.camera.CameraStatus
import androidx.kotlin.expand.os.permission.PermissionCode
import com.gallery.core.ext.CameraUri
import com.gallery.core.ext.openCameraExpand
import com.gallery.core.ext.requestCameraResultLauncherExpand
import com.gallery.core.ext.requestPermissionResultLauncherExpand

/**
 * @author y
 */
abstract class GalleryBaseFragment(layoutId: Int) : Fragment(layoutId) {

    private val openCameraLauncher: ActivityResultLauncher<CameraUri> by lazy {
        requestCameraResultLauncherExpand({ onCameraResultCanceled() }) { onCameraResultOk() }
    }

    protected val cameraPermissionLauncher: ActivityResultLauncher<String> by lazy {
        requestPermissionResultLauncherExpand {
            if (it) {
                permissionsGranted(PermissionCode.READ)
            } else {
                permissionsDenied(PermissionCode.READ)
            }
        }
    }

    protected val writePermissionLauncher: ActivityResultLauncher<String> by lazy {
        requestPermissionResultLauncherExpand {
            if (it) {
                permissionsGranted(PermissionCode.WRITE)
            } else {
                permissionsDenied(PermissionCode.WRITE)
            }
        }
    }

    protected fun openCamera(uri: CameraUri): CameraStatus {
        return openCameraExpand(uri) {
            openCameraLauncher.launch(uri)
        }
    }

    protected open fun onCameraResultCanceled() {}

    protected open fun onCameraResultOk() {}

    protected open fun permissionsGranted(type: PermissionCode) {}

    protected open fun permissionsDenied(type: PermissionCode) {}
}

