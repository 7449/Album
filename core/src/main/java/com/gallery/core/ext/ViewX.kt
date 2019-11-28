package com.gallery.core.ext

import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.ColorInt
import com.gallery.core.ui.widget.AlbumImageView

//View是否显示
fun View.hasVisible(): Boolean = visibility == View.VISIBLE

//View显示
fun View.show() = let { if (!hasVisible()) visibility = View.VISIBLE }

//View隐藏
fun View.hide() = let { if (hasVisible()) visibility = View.GONE }

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

//添加子View
fun ViewGroup.addChildView(childView: View?) {
    if (indexOfChild(childView) == -1 && childView != null) {
        addView(childView)
    }
}

//添加子View
fun ViewGroup.addChildView(childView: View?, layoutParams: ViewGroup.LayoutParams?) {
    if (indexOfChild(childView) == -1 && childView != null) {
        if (layoutParams != null) {
            addView(childView, layoutParams)
        } else {
            addView(childView)
        }
    }
}

//AlbumImageView
fun ViewGroup.AlbumImageView(): AlbumImageView = let {
    for (i in 0 until childCount) {
        val childAt = getChildAt(i)
        if (childAt is AlbumImageView) {
            return childAt
        }
    }
    return AlbumImageView(context)
}
