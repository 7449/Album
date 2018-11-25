package com.album.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils

import com.album.AlbumConstant

import java.io.File

import android.app.Activity.RESULT_OK

/**
 * by y on 11/08/2017.
 */

object FileUtils {

    fun isFile(path: String): Boolean = getPathFile(path) != null

    fun getPathFile(path: String): File? {
        if (TextUtils.isEmpty(path)) {
            return null
        }
        val file = File(path)
        return if (!file.exists()) {
            null
        } else file.parentFile ?: return null
    }

    fun getScannerFile(path: String): File = File(path)

    fun finishCamera(activity: Activity, path: String) {
        val bundle = Bundle()
        bundle.putString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path)
        val intent = Intent()
        intent.putExtras(bundle)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }

    fun getCameraFile(context: Context, path: String?, video: Boolean): File {
        var cachePath: String? = null
        if (TextUtils.isEmpty(path)) {
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                cachePath = Environment.getExternalStorageDirectory().path + "/" + Environment.DIRECTORY_DCIM
            } else {
                val externalCacheDir = context.externalCacheDir
                if (externalCacheDir != null) {
                    cachePath = externalCacheDir.path
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
}
