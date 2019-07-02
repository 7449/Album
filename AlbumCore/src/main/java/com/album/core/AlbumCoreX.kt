@file:Suppress("NOTHING_TO_INLINE")

package com.album.core

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.album.core.scan.AlbumColumns
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseActivity
import com.album.core.widget.AlbumImageView
import java.io.File

//文件是否为视频
inline fun AlbumEntity.hasVideo(): Boolean = this.mediaType == AlbumColumns.VIDEO

//文件是否为gif
inline fun AlbumEntity.hasGif(): Boolean = this.mimeType == "image/gif"

//文件是否存在
inline fun String.fileExists(): Boolean = parentFile() != null

//路径转[File]
inline fun String.toFile(): File? = if (isNullOrEmpty()) null else File(this)

//获取父级路径
inline fun String.parentFile(): File? = toFile()?.let { if (it.exists()) it.parentFile else null }

//View是否显示
inline fun View.hasVisible(): Boolean = visibility == View.VISIBLE

//View显示
inline fun View.show() = let { if (!hasVisible()) visibility = View.VISIBLE }

//View隐藏
inline fun View.hide() = let { if (hasVisible()) visibility = View.GONE }

//px 转 dip
inline fun Context.px2dip(pxValue: Int): Int = (pxValue / resources.displayMetrics.density + 0.5f).toInt()

//dip 转 px
inline fun Context.dip2px(dpValue: Int): Int = (dpValue * resources.displayMetrics.density + 0.5f).toInt()

//如果Bundle为Null则返回一个空的Bundle
inline fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

//如果Intent为Null则返回一个空的Intent
inline fun Intent?.orEmpty(): Intent = this ?: Intent()

//Android版本是否为L
inline fun hasL(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

//Android版本是否为M
inline fun hasM(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

//判断颜色是否为亮色
inline fun isLightColor(@ColorInt color: Int): Boolean = ColorUtils.calculateLuminance(color) >= 0.5

//Activity判断存储权限
inline fun Activity.permissionStorage(): Boolean = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, AlbumPermissionConst.WRITE_REQUEST_CODE)

//Fragment判断存储权限
inline fun Fragment.permissionStorage(): Boolean = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, AlbumPermissionConst.WRITE_REQUEST_CODE)

//Activity判断拍照权限
inline fun Activity.permissionCamera(): Boolean = permission(Manifest.permission.CAMERA, AlbumPermissionConst.CAMERA_REQUEST_CODE)

//Fragment判断拍照权限
inline fun Fragment.permissionCamera(): Boolean = permission(Manifest.permission.CAMERA, AlbumPermissionConst.CAMERA_REQUEST_CODE)

//activity 打开相机
inline fun Activity.openCamera(cameraUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) AlbumCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, cameraUri, video)

//fragment 打开相机
inline fun Fragment.openCamera(cameraUri: Uri, video: Boolean): Int = if (!permissionCamera() || !permissionStorage()) AlbumCameraConst.CAMERA_PERMISSION_ERROR else openCamera(this, cameraUri, video)

//合并选择数据
inline fun ArrayList<AlbumEntity>.mergeEntity(selectEntity: ArrayList<AlbumEntity>) = apply {
    forEach { it.isCheck = false }
    selectEntity.forEach { select -> this.find { it.path == select.path }?.isCheck = true }
}

//设置状态栏颜色
inline fun Window.statusBarColor(@ColorInt color: Int) {
    if (hasM()) {
        statusBarColor = color
        if (isLightColor(color)) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}

//添加子View
inline fun ViewGroup.addChildView(childView: View?) {
    if (indexOfChild(childView) == -1 && childView != null) {
        addView(childView)
    }
}

//添加子View
inline fun ViewGroup.addChildView(childView: View?, layoutParams: ViewGroup.LayoutParams?) {
    if (indexOfChild(childView) == -1 && childView != null) {
        if (layoutParams != null) {
            addView(childView, layoutParams)
        } else {
            addView(childView)
        }
    }
}

//AlbumImageView
inline fun ViewGroup.AlbumImageView(): AlbumImageView = let {
    for (i in 0 until childCount) {
        val childAt = getChildAt(i)
        if (childAt is AlbumImageView) {
            return childAt
        }
    }
    return AlbumImageView(context)
}

//获取Album宽高
inline fun Activity.square(count: Int): Int {
    val display = window.windowManager.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.widthPixels / count
}

//获取Drawable
@Suppress("DEPRECATION")
inline fun Context.drawable(id: Int, color: Int): Drawable {
    val drawable = resources.getDrawable(id)
    drawable.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
}

//判断权限,如果有返回true,没有返回false并直接请求
inline fun Activity.permission(permissions: String, code: Int): Boolean {
    if (ContextCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, arrayOf(permissions), code)
        return false
    }
    return true
}

//判断权限,如果有返回true,没有返回false并直接请求
inline fun Fragment.permission(permissions: String, code: Int): Boolean {
    activity?.let {
        if (ContextCompat.checkSelfPermission(it, permissions) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permissions), code)
            return false
        }
        return true
    }
    return false
}

//打开相机(需要提前请求权限)）
inline fun openCamera(root: Any, cameraUri: Uri, video: Boolean): Int {
    var activity: Activity? = null
    if (root is Activity) {
        activity = root
    }
    if (root is Fragment) {
        activity = root.activity
    }
    val intent = if (video) Intent(MediaStore.ACTION_VIDEO_CAPTURE) else Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    activity?.let {
        if (intent.resolveActivity(it.packageManager) == null) {
            return AlbumCameraConst.CAMERA_ERROR
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        } else {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, cameraUri.path)
            val uri = it.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        if (root is Activity) {
            root.startActivityForResult(intent, AlbumCameraConst.CAMERA_REQUEST_CODE)
        }
        if (root is Fragment) {
            root.startActivityForResult(intent, AlbumCameraConst.CAMERA_REQUEST_CODE)
        }
        return AlbumCameraConst.CAMERA_SUCCESS
    }
    return AlbumCameraConst.CAMERA_ERROR

}

//获取相机拍照或者录像时的路径
inline fun Context.cameraFile(path: String?, name: String, suffix: String): File {
    var cachePath: String? = null
    if (TextUtils.isEmpty(path)) {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path
        } else {
            externalCacheDir?.let { cachePath = it.path }
        }
    } else {
        cachePath = path
        val pathFile = File(path)
        if (!pathFile.exists()) {
            pathFile.mkdirs()
        }
    }
    return File(cachePath, System.currentTimeMillis().toString() + "_" + name + "." + suffix)
}

//自定义相机可以使用此方法直接返回路径,也可以自定义
inline fun AlbumBaseActivity.finishCamera(path: String) {
    val bundle = Bundle()
    bundle.putString(AlbumCameraConst.RESULT_PATH, path)
    val intent = Intent()
    intent.putExtras(bundle)
    setResult(Activity.RESULT_OK, intent)
    finish()
}