package com.gallery.core.ui.adapter.vh

import android.graphics.PorterDuff
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.R

class CameraViewHolder(itemView: View, private val galleryBundle: GalleryBundle) : RecyclerView.ViewHolder(itemView) {

    private val container: LinearLayout = itemView.findViewById(R.id.gallery_camera_root_view)
    private val cameraIv: AppCompatImageView = itemView.findViewById(R.id.galleryImageCamera)
    private val cameraTv: AppCompatTextView = itemView.findViewById(R.id.galleryImageCameraTv)

    fun camera() {
        val drawable = ContextCompat.getDrawable(itemView.context, galleryBundle.cameraDrawable)
        drawable?.setColorFilter(ContextCompat.getColor(itemView.context, galleryBundle.cameraDrawableColor), PorterDuff.Mode.SRC_ATOP)
        cameraTv.setText(galleryBundle.cameraText)
        cameraTv.textSize = galleryBundle.cameraTextSize
        cameraTv.setTextColor(ContextCompat.getColor(itemView.context, galleryBundle.cameraTextColor))
        container.setBackgroundColor(ContextCompat.getColor(itemView.context, galleryBundle.cameraBackgroundColor))
        cameraIv.setImageDrawable(drawable)
    }
}