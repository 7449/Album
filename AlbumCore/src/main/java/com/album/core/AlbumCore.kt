package com.album.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

/**
 * @author y
 * @create 2019/2/27
 */

object AlbumCore {

    fun Bundle?.orEmpty(): Bundle = this ?: Bundle.EMPTY

    fun Intent?.orEmpty(): Intent = this ?: Intent()

    fun hasL(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    fun Window.settingStatusBarColor(@ColorInt color: Int) {
        if (hasL()) {
            statusBarColor = color
        }
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

}

