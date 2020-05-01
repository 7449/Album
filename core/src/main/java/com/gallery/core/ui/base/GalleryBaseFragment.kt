package com.gallery.core.ui.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.camera.CameraStatus
import androidx.kotlin.expand.os.permission.PermissionCode
import com.gallery.core.ext.CameraResultContract
import com.gallery.core.ext.CameraUri
import com.gallery.scan.ScanType

/**
 * @author y
 */
abstract class GalleryBaseFragment(layoutId: Int) : Fragment(layoutId) {

    private val requestCameraResultLauncher: ActivityResultLauncher<CameraUri> = registerForActivityResult(CameraResultContract()) {
        when (it) {
            Activity.RESULT_CANCELED -> onCameraResultCanceled()
            Activity.RESULT_OK -> onCameraResultOk()
        }
    }

    private val requestPermissionResultLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
        if (map.containsKey(Manifest.permission.CAMERA)) {
            if (map[Manifest.permission.CAMERA] == true) {
                permissionsGranted(PermissionCode.READ)
            } else {
                permissionsDenied(PermissionCode.READ)
            }
        } else if (map.containsKey(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (map[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                permissionsGranted(PermissionCode.WRITE)
            } else {
                permissionsDenied(PermissionCode.WRITE)
            }
        }
    }

    protected fun requestCamera() {
        requestPermissionResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    protected fun requestWrite() {
        requestPermissionResultLauncher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    protected fun checkPermissionCamera(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    protected fun checkPermissionWrite(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    protected fun checkPermissionAndRequestCamera(): Boolean {
        return if (!checkPermissionCamera()) {
            requestCamera()
            false
        } else {
            true
        }
    }

    protected fun checkPermissionAndRequestWrite(): Boolean {
        return if (!checkPermissionWrite()) {
            requestWrite()
            false
        } else {
            true
        }
    }

    protected fun openCamera(uri: CameraUri): CameraStatus {
        val intent = if (uri.type == ScanType.VIDEO) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) == null) {
            return CameraStatus.ERROR
        }
        requestCameraResultLauncher.launch(uri)
        return CameraStatus.SUCCESS
    }

    protected open fun onCameraResultCanceled() {}

    protected open fun onCameraResultOk() {}

    protected open fun permissionsGranted(type: PermissionCode) {}

    protected open fun permissionsDenied(type: PermissionCode) {}
}

