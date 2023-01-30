package com.gallery.ui.material

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.gallery.core.extensions.drawable
import com.gallery.ui.material.args.MaterialGalleryBundle

/** 获取自定义颜色Drawable */
fun Context.minimumDrawable(@DrawableRes id: Int, @ColorInt color: Int): Drawable? =
    drawable(id)?.minimumWidthAndHeightDrawable(color)

/** 获取自定义Drawable */
fun Drawable.minimumWidthAndHeightDrawable(@ColorInt color: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
    return this
}

internal val Parcelable?.materialGalleryArgOrDefault: MaterialGalleryBundle
    get() = this as? MaterialGalleryBundle ?: MaterialGalleryBundle()