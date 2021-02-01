package com.gallery.compat.extensions

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gallery.core.extensions.drawableExpand
import com.gallery.core.extensions.hasMExpand
import com.gallery.core.extensions.isLightColorExpand

/** 设置状态栏颜色 */
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

/** 获取颜色 */
fun Int.colorExpand(activity: Context): Int = activity.colorExpand(this)


/** 获取颜色 */
fun Context.colorExpand(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

/** 获取自定义颜色Drawable */
fun Context.minimumDrawableExpand(@DrawableRes id: Int, @ColorInt color: Int): Drawable? = drawableExpand(id)?.minimumWidthAndHeightDrawableExpand(color)

/** 获取自定义Drawable */
fun Drawable.minimumWidthAndHeightDrawableExpand(@ColorInt color: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
    return this
}