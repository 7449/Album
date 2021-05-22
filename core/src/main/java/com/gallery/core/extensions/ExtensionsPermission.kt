package com.gallery.core.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

enum class PermissionCode {
    READ,
    WRITE
}

/** 检查相机权限 */
fun Fragment.checkCameraPermissionExpand() =
    requireContext().checkSelfPermissionExpand(Manifest.permission.CAMERA)

/** 检查读写权限 */
fun Fragment.checkWritePermissionExpand() =
    requireContext().checkSelfPermissionExpand(Manifest.permission.WRITE_EXTERNAL_STORAGE)

/** 判断是否获得权限 */
fun Context.checkSelfPermissionExpand(name: String) =
    ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED