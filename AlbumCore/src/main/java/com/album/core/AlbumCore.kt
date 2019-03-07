package com.album.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils


/**
 * @author y
 * @create 2019/2/27
 */


fun Context.px2dip(pxValue: Int): Int = (pxValue / resources.displayMetrics.density + 0.5f).toInt()

fun Context.dip2px(dpValue: Int): Int = (dpValue * resources.displayMetrics.density + 0.5f).toInt()

fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

fun Intent?.orEmpty(): Intent = this ?: Intent()

fun hasL(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun hasM(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun Window.settingStatusBarColor(@ColorInt color: Int) {
    if (hasM()) {
        statusBarColor = color
        if (isLightColor(color)) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}

private fun isLightColor(@ColorInt color: Int): Boolean {
    return ColorUtils.calculateLuminance(color) >= 0.5
}

fun Activity.imageViewWidthAndHeight(count: Int): Int {
    val display = window.windowManager.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.widthPixels / count
}

fun Context.drawable(id: Int, color: Int): Drawable {
    val drawable = resources.getDrawable(id)
    drawable.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_ATOP)
    drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
    return drawable
}


