package com.gallery.ui.material

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.extensions.drawable
import com.gallery.ui.material.args.MaterialGalleryConfig

internal fun Context.minimumDrawable(@DrawableRes id: Int): Drawable? =
    drawable(id)?.minimumWidthAndHeightDrawable()

internal fun Drawable.minimumWidthAndHeightDrawable(): Drawable {
//    this.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    this.setBounds(0, 0, this.minimumWidth, this.minimumHeight)
    return this
}

internal val Parcelable?.materialGalleryArgOrDefault: MaterialGalleryConfig
    get() = this as? MaterialGalleryConfig ?: MaterialGalleryConfig()

internal fun ViewGroup.createGalleryImageView(): GalleryImageView {
    return GalleryImageView(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER
        }
    }
}