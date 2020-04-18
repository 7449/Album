@file:Suppress("HasPlatformType")

package com.gallery.core.ext

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
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
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.gallery.core.PermissionCode
import com.gallery.scan.args.Columns
import java.io.File

//Android版本是否为L
fun hasL() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

//Android版本是否为M
fun hasM() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

//Android版本是否为N
fun hasN() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

//Android版本是否为Q
fun hasQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

//View是否显示
fun View.hasVisible() = visibility == View.VISIBLE

//View显示
fun View.show() = let { if (!hasVisible()) visibility = View.VISIBLE }

//View隐藏
fun View.hide() = let { if (hasVisible()) visibility = View.GONE }

//如果Bundle为Null则返回一个空的Bundle
fun Bundle?.orEmpty() = this ?: Bundle.EMPTY

//如果Intent为Null则返回一个空的Intent
fun Intent?.orEmpty() = this ?: Intent()

//如果Uri为Null则返回一个空的Uri
fun Uri?.orEmpty() = this ?: Uri.EMPTY

//判断颜色是否为亮色
fun Int.isLightColor() = ColorUtils.calculateLuminance(this) >= 0.5

//获取color
fun Context.color(@ColorRes id: Int) = ContextCompat.getColor(this, id)

//获取drawable
fun Context.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

//获取color
fun Fragment.color(@ColorRes id: Int) = ContextCompat.getColor(requireActivity(), id)

//获取drawable
fun Fragment.drawable(@DrawableRes id: Int) = ContextCompat.getDrawable(requireActivity(), id)

//获取某个fragment or callback
fun <T : Fragment> AppCompatActivity.findFragmentByTag(tag: String, ifNone: (String) -> T): T = supportFragmentManager.findFragmentByTag(tag) as T?
        ?: ifNone(tag)

//插入ContentValues
fun Context.insertImage(contentValues: ContentValues) = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
} else {
    contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues)
            ?: throw KotlinNullPointerException()
}

//根据Id查询文件是否存在
fun Context.uriExists(uri: Uri) = uriExists(uri, Columns.ID)

//根据column查询文件是否存在
fun Context.uriExists(uri: Uri, name: String) = contentResolver.query(uri, name).use {
    it?.moveToNext() ?: false
}

//fragment runOnUiThread
fun Fragment.runOnUiThread(action: () -> Unit) = requireActivity().runOnUiThread { action.invoke() }

//query (uri,name)
fun ContentResolver.query(uri: Uri, name: String) = query(uri, arrayOf(name), null, null, null)

//Activity判断存储权限
fun Activity.permissionStorage() = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionCode.WRITE.code)

//Activity判断拍照权限
fun Activity.permissionCamera() = permission(Manifest.permission.CAMERA, PermissionCode.READ.code)

//Fragment判断存储权限
fun Fragment.permissionStorage() = permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionCode.WRITE.code)

//Fragment判断拍照权限
fun Fragment.permissionCamera() = permission(Manifest.permission.CAMERA, PermissionCode.READ.code)

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
    if (ContextCompat.checkSelfPermission(requireActivity(), permissions) != PackageManager.PERMISSION_GRANTED) {
        requestPermissions(arrayOf(permissions), code)
        return false
    }
    return true
}

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
    val dm = DisplayMetrics()
    window.windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels / count
}

//获取Drawable
fun Context.drawable(@DrawableRes id: Int, @ColorInt color: Int): Drawable {
    val drawable = drawable(id) ?: throw KotlinNullPointerException()
    drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
}

//根据uri获取文件id
fun Context.findIdByUri(uri: Uri): Long {
    val split = uri.toString().split("/")
    var id = 0L
    try {
        id = split[split.size - 1].toLong()
    } catch (e: Exception) {
        contentResolver.query(uri, arrayOf(Columns.ID), null, null, null).use {
            val cursor = it ?: return 0L
            while (cursor.moveToNext()) {
                id = cursor.getLong(cursor.getColumnIndex(Columns.ID))
            }
        }
    }
    return id
}

//根据uri获取文件id
fun Context.findUriByFile(file: File) = insertImage(ContentValues().apply {
    if (hasQ()) {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
    } else {
        put(MediaStore.MediaColumns.DATA, file.path)
    }
})