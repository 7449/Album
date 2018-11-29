package com.album.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.album.CUSTOMIZE_CAMERA_RESULT_PATH_KEY
import com.album.ITEM_CAMERA
import java.io.File

fun checkNotStringNull(notNull: String?): String {
    return notNull ?: ""
}

fun checkNotBundleNull(notNull: Bundle?): Bundle {
    return notNull ?: Bundle.EMPTY
}

fun checkNotIntentNull(notNull: Intent?): Intent {
    return notNull ?: Intent()
}

fun setStatusBarColor(@ColorInt color: Int, window: Window?) {
    if (window != null && hasL()) {
        window.statusBarColor = color
    }
}

fun getImageViewWidth(activity: Activity, count: Int): Int {
    val display = activity.window.windowManager.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.widthPixels / count
}

fun hasL(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
}

fun getDrawable(context: Context, id: Int, color: Int): Drawable {
    val drawable = context.resources.getDrawable(id)
    drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
}

fun finishCamera(activity: Activity, path: String) {
    val bundle = Bundle()
    bundle.putString(CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path)
    val intent = Intent()
    intent.putExtras(bundle)
    activity.setResult(Activity.RESULT_OK, intent)
    activity.finish()
}

fun openCamera(any: Any, cameraUri: Uri, video: Boolean): Int {
    var cameraActivity: Activity? = null
    if (any is Activity) {
        cameraActivity = any
    }
    if (any is Fragment) {
        cameraActivity = any.activity!!
    }
    if (!PermissionUtils.camera(any) || !PermissionUtils.storage(any)) {
        return -1
    }
    val intent = if (video) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    if (intent.resolveActivity(cameraActivity!!.packageManager) == null) {
        return 1
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
    } else {
        val contentValues = ContentValues(1)
        contentValues.put(MediaStore.Images.Media.DATA, cameraUri.path)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, ITEM_CAMERA)
        val uri = cameraActivity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }
    if (any is Activity) {
        cameraActivity.startActivityForResult(intent, ITEM_CAMERA)
    }
    if (any is Fragment) {
        any.startActivityForResult(intent, ITEM_CAMERA)
    }
    return 0
}

fun fileExists(path: String): Boolean {
    return getParentFile(path) != null
}

fun pathToFile(path: String): File {
    return File(path)
}

fun getParentFile(path: String): File? {
    if (TextUtils.isEmpty(path)) {
        return null
    }
    val file = File(path)
    return if (!file.exists()) {
        null
    } else file.parentFile ?: return null
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