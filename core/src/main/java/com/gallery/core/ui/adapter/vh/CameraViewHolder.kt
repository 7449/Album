package com.gallery.core.ui.adapter.vh

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.xadapter.vh.*

class CameraViewHolder(itemView: View, private val galleryBundle: GalleryBundle) : XViewHolder(itemView) {

    fun camera() {
        val drawable: Drawable? = ContextCompat.getDrawable(context, galleryBundle.cameraDrawable)
        drawable?.colorFilter = PorterDuffColorFilter(galleryBundle.cameraDrawableColor, PorterDuff.Mode.SRC_ATOP)
        textView(R.id.galleryImageCameraTv).text = galleryBundle.cameraText
        textView(R.id.galleryImageCameraTv).textSize = galleryBundle.cameraTextSize
        textView(R.id.galleryImageCameraTv).setTextColor(galleryBundle.cameraTextColor)
        viewById(R.id.gallery_camera_root_view).setBackgroundColor(galleryBundle.cameraBackgroundColor)
        imageView(R.id.galleryImageCamera).setImageDrawable(drawable)
    }
}