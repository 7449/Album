package com.gallery.core.extensions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.checkCameraPermissionExpand() =
        requireContext().checkSelfPermissionExpand(Manifest.permission.CAMERA)

fun Fragment.checkWritePermissionExpand() =
        requireContext().checkSelfPermissionExpand(Manifest.permission.WRITE_EXTERNAL_STORAGE)

fun Context.checkSelfPermissionExpand(name: String) =
        ContextCompat.checkSelfPermission(this, name) == PackageManager.PERMISSION_GRANTED