package com.album.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * by y on 11/08/2017.
 */

object PermissionUtils {

    const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0
    const val CAMERA_REQUEST_CODE = 1

    fun storage(any: Any): Boolean = if (any is Activity) storage(any) else storage(any as Fragment)

    fun camera(any: Any): Boolean = if (any is Activity) camera(any) else camera(any as Fragment)

    fun storage(activity: Activity): Boolean = permission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE)

    fun camera(activity: Activity): Boolean = permission(activity, Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)

    fun storage(fragment: Fragment): Boolean = permission(fragment, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE)

    fun camera(fragment: Fragment): Boolean = permission(fragment, Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)

    private fun permission(activity: Activity, permissions: String, code: Int): Boolean {
        if (ContextCompat.checkSelfPermission(activity, permissions) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permissions), code)
            return false
        }
        return true
    }

    private fun permission(fragment: Fragment, permissions: String, code: Int): Boolean {
        if (ContextCompat.checkSelfPermission(fragment.activity!!, permissions) != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(arrayOf(permissions), code)
            return false
        }
        return true
    }
}
