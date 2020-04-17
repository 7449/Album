package com.gallery.core.ui.adapter.vh

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.xadapter.vh.*

class CameraViewHolder(itemView: View, private val galleryBundle: GalleryBundle) : XViewHolder(itemView) {

    fun camera() {
        val drawable = ContextCompat.getDrawable(context, galleryBundle.cameraDrawable)
        drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, galleryBundle.cameraDrawableColor), PorterDuff.Mode.SRC_ATOP)
        textView(R.id.galleryImageCameraTv).text = galleryBundle.cameraText
        textView(R.id.galleryImageCameraTv).textSize = galleryBundle.cameraTextSize
        textView(R.id.galleryImageCameraTv).setTextColor(ContextCompat.getColor(context, galleryBundle.cameraTextColor))
        viewById(R.id.gallery_camera_root_view).setBackgroundColor(ContextCompat.getColor(context, galleryBundle.cameraBackgroundColor))
        imageView(R.id.galleryImageCamera).setImageDrawable(drawable)
    }
}