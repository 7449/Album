@file:Suppress("unused")

package com.gallery.core.ext

import android.Manifest
import android.app.Activity
import android.content.ContentUris
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
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.gallery.core.constant.GalleryPermissionConst
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.Columns
import java.io.File

//获取文件的Uri
fun ScanEntity.externalUri(): Uri {
    return if (mediaType == Columns.IMAGE) {
        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    } else {
        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
    }
}

//根据Id查询文件是否存在
fun Context.fileExists(uri: Uri) = fileExists(uri, Columns.ID)

//Android版本是否为L
fun hasL(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

//Android版本是否为M
fun hasM(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

//Android版本是否为N
fun hasN(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

//Android版本是否为Q
fun hasQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

//路径转[File]
fun String.toFile(): File? = if (isNullOrEmpty()) null else File(this)

//获取父级路径
fun String.parentFile(): File? = toFile()?.let { if (it.exists()) it.parentFile else null }

//文件是否存在
fun String.fileExists(): Boolean = parentFile() != null

//如果Bundle为Null则返回一个空的Bundle
fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

//如果Intent为Null则返回一个空的Intent
fun Intent?.orEmpty(): Intent = this ?: Intent()

//判断颜色是否为亮色
fun Int.isLightColor(): Boolean = ColorUtils.calculateLuminance(this) >= 0.5

//View是否显示
fun View.hasVisible(): Boolean = visibility == View.VISIBLE

//View显示
fun View.show() = let { if (!hasVisible()) visibility = View.VISIBLE }

//View隐藏
fun View.hide() = let { if (hasVisible()) visibility = View.GONE }

//Activity判断存储权限
fun Activity.permissionStorage(): Boolean = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, GalleryPermissionConst.WRITE_REQUEST_CODE)

//Fragment判断存储权限
fun Fragment.permissionStorage(): Boolean = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, GalleryPermissionConst.WRITE_REQUEST_CODE)

//Activity判断拍照权限
fun Activity.permissionCamera(): Boolean = permission(Manifest.permission.CAMERA, GalleryPermissionConst.CAMERA_REQUEST_CODE)

//Fragment判断拍照权限
fun Fragment.permissionCamera(): Boolean = permission(Manifest.permission.CAMERA, GalleryPermissionConst.CAMERA_REQUEST_CODE)

//设置状态栏颜色
fun Window.statusBarColor(@ColorInt color: Int) {
    if (hasM()) {
        statusBarColor = color
        if (color.isLightColor()) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}

//获取Gallery宽高
fun Activity.square(count: Int): Int {
    val display = window.windowManager.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.widthPixels / count
}

//获取Drawable
@Suppress("DEPRECATION")
fun Context.drawable(id: Int, color: Int): Drawable {
    val drawable = resources.getDrawable(id)
    drawable.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
}

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

//根据column查询文件是否存在
fun Context.fileExists(uri: Uri, columnsName: String): Boolean {
    contentResolver.query(uri, arrayOf(columnsName), null, null, null).use {
        return it?.moveToNext() ?: false
    }
}

//插入ContentValues
fun Context.insertImage(contentValues: ContentValues) = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
} else {
    contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
}

//合并选择数据和全部数据
fun ArrayList<ScanEntity>.mergeEntity(selectEntity: ArrayList<ScanEntity>) = also {
    forEach { it.isCheck = false }
    selectEntity.forEach { select -> this.find { it.id == select.id }?.isCheck = true }
}