package com.album.core

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * @author y
 * @create 2019/2/27
 *
 * 权限处理简化工具类
 */

object AlbumPermission {

    /**
     * 读写code
     */
    const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0
    /**
     * 拍照code
     */
    const val CAMERA_REQUEST_CODE = 1

    /**
     * 图库权限
     */
    const val TYPE_PERMISSIONS_ALBUM = 0
    /**
     * 拍照权限
     */
    const val TYPE_PERMISSIONS_CAMERA = 1

    fun Any.permissionStorage(): Boolean {
        return when {
            this is Activity -> permission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            this is Fragment -> permission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            else -> throw KotlinNullPointerException()
        }
    }

    fun Any.permissionCamera(): Boolean {
        return when {
            this is Activity -> permission(this, Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
            this is Fragment -> permission(this, Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
            else -> throw KotlinNullPointerException()
        }
    }

    fun Activity.permissionStorage(): Boolean = permission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE)

    fun Fragment.permissionStorage(): Boolean = permission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_REQUEST_CODE)

    fun Activity.permissionCamera(): Boolean = permission(this, Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)

    fun Fragment.permissionCamera(): Boolean = permission(this, Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)

    private fun permission(activity: Activity, permissions: String, code: Int): Boolean {
        if (ContextCompat.checkSelfPermission(activity, permissions) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(permissions), code)
            return false
        }
        return true
    }

    private fun permission(fragment: Fragment, permissions: String, code: Int): Boolean {
        fragment.activity?.let {
            if (ContextCompat.checkSelfPermission(it, permissions) != PackageManager.PERMISSION_GRANTED) {
                fragment.requestPermissions(arrayOf(permissions), code)
                return false
            }
            return true
        }
        return false
    }
}


