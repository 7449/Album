@file:Suppress("MemberVisibilityCanBePrivate")

package com.album.core

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.album.core.AlbumPermission.permissionCamera
import com.album.core.AlbumPermission.permissionStorage
import com.album.core.ui.AlbumBaseActivity
import java.io.File

/**
 * @author y
 * @create 2019/2/27
 */
object AlbumCamera {

    /**
     * 自定义拍照返回时使用的 REQUEST_CODE
     */
    const val CUSTOMIZE_CAMERA_REQUEST_CODE = 113
    /**
     * 自定义拍照返回时使用的路径Key,返回时直接调用[finishCamera]即可
     */
    const val CUSTOMIZE_CAMERA_RESULT_PATH_KEY = "customize_camera_path"

    /**
     * 拍照返回的 REQUEST_CODE
     */
    const val OPEN_CAMERA_REQUEST_CODE = 1111
    /**
     * 打开相机没有权限code
     */
    const val OPEN_CAMERA_PERMISSION_ERROR = -1
    /**
     * 打开相机成功code
     */
    const val OPEN_CAMERA_SUCCESS = 0
    /**
     * 打开相机错误code
     */
    const val OPEN_CAMERA_ERROR = 1


    /**
     * 打开相机
     * [OPEN_CAMERA_ERROR]
     * [OPEN_CAMERA_SUCCESS]
     * [OPEN_CAMERA_PERMISSION_ERROR]
     */
    private fun openCamera(any: Any, cameraUri: Uri, video: Boolean): Int {

        if (!any.permissionCamera() || !any.permissionStorage()) {
            return OPEN_CAMERA_PERMISSION_ERROR
        }

        var cameraActivity: Activity? = null
        if (any is Activity) {
            cameraActivity = any
        }
        if (any is Fragment) {
            cameraActivity = any.activity
        }

        cameraActivity?.let {
            val intent = if (video) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(it.packageManager) == null) {
                return OPEN_CAMERA_ERROR
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            } else {
                val contentValues = ContentValues(1)
                contentValues.put(MediaStore.Images.Media.DATA, cameraUri.path)
                contentValues.put(MediaStore.Images.Media.MIME_TYPE, OPEN_CAMERA_REQUEST_CODE)
                val uri = it.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            }
            if (any is Activity) {
                any.startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE)
            }
            if (any is Fragment) {
                any.startActivityForResult(intent, OPEN_CAMERA_REQUEST_CODE)
            }
            return OPEN_CAMERA_SUCCESS
        }
        return OPEN_CAMERA_ERROR

    }

    /**
     * activity 打开相机
     */
    fun Activity.openCamera(cameraUri: Uri, video: Boolean): Int = openCamera(this, cameraUri, video)

    /**
     * fragment 打开相机
     */
    fun Fragment.openCamera(cameraUri: Uri, video: Boolean): Int = openCamera(this, cameraUri, video)

    /**
     * 获取相机拍照或者录像时的路径
     */
    fun Context.getCameraFile(path: String?, video: Boolean): File {
        var cachePath: String? = null
        if (TextUtils.isEmpty(path)) {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                cachePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path
            } else {
                externalCacheDir?.let {
                    cachePath = it.path
                }
            }
        } else {
            cachePath = path
            val pathFile = File(path)
            if (!pathFile.exists()) {
                pathFile.mkdirs()
            }
        }
        return File(cachePath, System.currentTimeMillis().toString() + if (video) ".mp4" else ".jpg")
    }

    /**
     * 自定义相机可以使用此方法直接返回路径,也可以自定义
     */
    fun AlbumBaseActivity.finishCamera(path: String) {
        val bundle = Bundle()
        bundle.putString(CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}