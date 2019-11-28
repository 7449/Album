package com.gallery.core.ext.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gallery.core.GalleryPermissionConst

//判断权限,如果有返回true,没有返回false并直接请求
fun Activity.permission(permissions: String, code: Int): Boolean {
    if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(permissions), code)
        return false
    }
    return true
}

//判断权限,如果有返回true,没有返回false并直接请求
fun Fragment.permission(permissions: String, code: Int): Boolean {
    activity?.let {
        if (ContextCompat.checkSelfPermission(it, permissions) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permissions), code)
            return false
        }
        return true
    }
    return false
}

//Activity判断存储权限
fun Activity.permissionStorage(): Boolean = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, GalleryPermissionConst.WRITE_REQUEST_CODE)

//Fragment判断存储权限
fun Fragment.permissionStorage(): Boolean = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, GalleryPermissionConst.WRITE_REQUEST_CODE)

//Activity判断拍照权限
fun Activity.permissionCamera(): Boolean = permission(Manifest.permission.CAMERA, GalleryPermissionConst.CAMERA_REQUEST_CODE)

//Fragment判断拍照权限
fun Fragment.permissionCamera(): Boolean = permission(Manifest.permission.CAMERA, GalleryPermissionConst.CAMERA_REQUEST_CODE)
