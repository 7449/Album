package com.gallery.core.extensions

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import java.io.InputStream
import java.io.OutputStream

/** 获取安全Bundle */
fun Bundle?.orEmptyExpand(): Bundle = this ?: Bundle.EMPTY

fun Context.findPathByUriExpand(uri: Uri): String? =
        contentResolver.queryDataExpand(uri)

fun ContentResolver.queryExpand(uri: Uri, vararg name: String): Cursor? =
        query(uri, name, null, null, null)

fun ContentResolver.queryDataExpand(uri: Uri): String? =
        queryExpand(uri, MediaStore.MediaColumns.DATA).use {
            val cursor = it ?: return null
            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            }
            return null
        }

fun Activity.squareExpand(count: Int): Int {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    return dm.widthPixels / count
}

fun Context.drawableExpand(@DrawableRes id: Int): Drawable? =
        ContextCompat.getDrawable(this, id)

fun Context.openVideoExpand(uri: Uri, error: () -> Unit) {
    runCatching {
        val video = Intent(Intent.ACTION_VIEW)
        video.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        video.setDataAndType(uri, "video/*")
        startActivity(video)
    }.onFailure { error.invoke() }
}

fun Activity.scanFileExpand(uri: Uri, action: (uri: Uri) -> Unit) {
    scanFileExpand(uri.filePathExpand(this), action)
}

fun Activity.scanFileExpand(path: String, action: (uri: Uri) -> Unit) {
    MediaScannerConnection.scanFile(this, arrayOf(path), null) { _: String?, uri: Uri? ->
        runOnUiThread {
            uri ?: return@runOnUiThread
            action.invoke(uri)
        }
    }
}

/** Toast */
fun String?.safeToastExpand(context: Context?) {
    if (!isNullOrEmpty()) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }
}

fun Activity?.bundleOrEmptyExpand(): Bundle = this?.intent?.extras.orEmptyExpand()

fun Window.statusBarColorExpand(@ColorInt color: Int) {
    if (hasMExpand()) {
        statusBarColor = color
        if (color.isLightColorExpand()) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}

fun Int.isLightColorExpand(): Boolean = ColorUtils.calculateLuminance(this) >= 0.5

fun Context.minimumDrawableExpand(@DrawableRes id: Int, @ColorInt color: Int): Drawable? =
        drawableExpand(id)?.minimumWidthAndHeightDrawableExpand(color)

fun Drawable.minimumWidthAndHeightDrawableExpand(@ColorInt color: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
    return this
}

fun <T : Parcelable> Bundle?.getParcelableExpand(key: String): T =
        getParcelableOrDefault(key)

fun <T : Parcelable> Bundle?.getParcelableArrayListExpand(key: String): ArrayList<T> =
        getParcelableArrayListOrDefault(key)

fun <T : Parcelable> Bundle?.getParcelableOrDefault(
        key: String,
        defaultValue: Parcelable = this?.getParcelable<T>(key)!!,
): T = getParcelableOrDefault(key) { defaultValue as T }

fun <T : Parcelable> Bundle?.getParcelableOrDefault(
        key: String,
        ifNone: () -> T,
): T = getObjExpand(key, ifNone)

fun <T : Parcelable> Bundle?.getParcelableArrayListOrDefault(
        key: String,
        defaultValue: ArrayList<T> = arrayListOf(),
): ArrayList<T> = getParcelableArrayListOrDefault(key) { defaultValue }

fun <T : Parcelable> Bundle?.getParcelableArrayListOrDefault(
        key: String,
        ifNone: () -> ArrayList<T>,
): ArrayList<T> = getObjExpand(key, ifNone)

fun <T> Bundle?.getObjExpand(key: String, action: () -> T): T =
        this?.get(key) as? T ?: action.invoke()

fun Context.colorExpand(@ColorRes id: Int): Int =
        ContextCompat.getColor(this, id)

fun Bundle?.getBooleanExpand(key: String): Boolean =
        getBooleanOrDefault(key)

fun Bundle?.getBooleanOrDefault(key: String, defaultValue: Boolean = false): Boolean =
        getBooleanOrDefault(key) { defaultValue }

fun Bundle?.getBooleanOrDefault(key: String, ifNone: () -> Boolean): Boolean =
        getObjExpand(key, ifNone)

fun Int.colorExpand(activity: Context): Int = ContextCompat.getColor(activity, this)

fun Context.copyImageExpand(
        inputUri: Uri,
        displayName: String,
        relativePath: String = Environment.DIRECTORY_DCIM,
): Uri? {
    if (!hasQExpand()) {
        return null
    }
    val contentValues = ContentValues()
    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
    val outPutUri: Uri? = insertImageUriExpand(contentValues)
    outPutUri ?: return null
    return copyFileExpand(inputUri, outPutUri)
}

fun Context.copyFileExpand(inputUri: Uri, outPutUri: Uri): Uri? {
    val outStream: OutputStream = contentResolver.openOutputStream(outPutUri) ?: return null
    val inStream: InputStream = contentResolver.openInputStream(inputUri) ?: return null
    outStream.use { out -> inStream.use { input -> input.copyTo(out) } }
    return outPutUri
}