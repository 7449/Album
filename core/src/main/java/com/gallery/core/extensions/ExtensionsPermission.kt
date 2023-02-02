package com.gallery.core.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

enum class PermissionCode {
    CAMERA,
    READ
}

internal fun Fragment.checkCameraPermission() =
    requireContext().checkSelfPermissions(Manifest.permission.CAMERA)

internal fun Fragment.checkReadPermission() =
    requireContext().checkSelfPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)

private fun Context.checkSelfPermissions(name: String) =
    ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED