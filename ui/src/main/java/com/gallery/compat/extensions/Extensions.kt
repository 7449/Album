package com.gallery.compat.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.PrevCompatFragment
import com.gallery.core.extensions.drawableExpand

/** [GalleryCompatFragment] */
val AppCompatActivity.galleryFragment: GalleryCompatFragment
    get() = supportFragmentManager.findFragmentByTag(
        GalleryCompatFragment::class.java.simpleName
    ) as GalleryCompatFragment

/** [PrevCompatFragment] */
val AppCompatActivity.prevFragment: PrevCompatFragment
    get() = supportFragmentManager.findFragmentByTag(
        PrevCompatFragment::class.java.simpleName
    ) as PrevCompatFragment

/** 设置状态栏颜色 */
fun Window.statusBarColorExpand(@ColorInt color: Int) {
    statusBarColor = color
}

fun Bundle?.getBooleanExpand(key: String): Boolean = getObjExpand(key) { false }

inline fun <reified T : Parcelable> Bundle?.getParcelableExpand(key: String): T =
    getParcelableOrDefault(key)

inline fun <reified T : Parcelable> Bundle?.getParcelableArrayListExpand(key: String): ArrayList<T> =
    getObjExpand(key) { arrayListOf() }

inline fun <reified T : Parcelable> Bundle?.getParcelableOrDefault(
    key: String,
    defaultValue: Parcelable = this?.getParcelable<T>(
        key
    )!!
): T = getObjExpand(key) { defaultValue as T }

inline fun <reified T> Bundle?.getObjExpand(key: String, action: () -> T): T = this?.get(key) as? T
    ?: action.invoke()

/** 获取颜色 */
fun Int.colorExpand(activity: Context): Int = activity.colorExpand(this)

/** 获取颜色 */
fun Context.colorExpand(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

/** 获取自定义颜色Drawable */
fun Context.minimumDrawableExpand(@DrawableRes id: Int, @ColorInt color: Int): Drawable? =
    drawableExpand(id)?.minimumWidthAndHeightDrawableExpand(color)

/** 获取自定义Drawable */
fun Drawable.minimumWidthAndHeightDrawableExpand(@ColorInt color: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
    return this
}