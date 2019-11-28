package com.gallery.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryInternalConst
import com.gallery.core.R
import com.gallery.core.action.GalleryAction
import com.gallery.core.ui.adapter.vh.CameraViewHolder
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.gallery.scan.ScanEntity

class GalleryAdapter(
        private val display: Int,
        private val galleryBundle: GalleryBundle,
        private val galleryAction: GalleryAction?,
        private val galleryItemClickListener: OnGalleryItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnGalleryItemClickListener {
        fun onCameraItemClick(view: View, position: Int, galleryEntity: ScanEntity)
        fun onPhotoItemClick(view: View, position: Int, galleryEntity: ScanEntity)
    }

    companion object {
        private const val TYPE_CAMERA = 0
        private const val TYPE_PHOTO = 1
    }

    var galleryList: ArrayList<ScanEntity> = ArrayList()

    var multipleList: ArrayList<ScanEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(display, display)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> {
                val cameraView: View = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery_camera, parent, false)
                val cameraViewHolder = CameraViewHolder(cameraView, galleryBundle)
                cameraView.setOnClickListener { v -> galleryItemClickListener.onCameraItemClick(v, cameraViewHolder.adapterPosition, galleryList[cameraViewHolder.adapterPosition]) }
                cameraViewHolder
            }
            else -> {
                val photoView: View = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery, parent, false)
                val photoViewHolder = PhotoViewHolder(photoView, galleryBundle, display, layoutParams, galleryAction)
                photoView.setOnClickListener { v -> galleryItemClickListener.onPhotoItemClick(v, photoViewHolder.adapterPosition, galleryList[photoViewHolder.adapterPosition]) }
                photoViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CameraViewHolder) {
            holder.camera()
        } else if (holder is PhotoViewHolder) {
            holder.photo(position, galleryList[position], multipleList)
        }
    }

    override fun getItemCount(): Int = galleryList.size

    override fun getItemViewType(position: Int): Int = when {
        galleryList.isEmpty() -> TYPE_PHOTO
        galleryList[position].path == GalleryInternalConst.CAMERA -> TYPE_CAMERA
        else -> TYPE_PHOTO
    }

    fun addAll(newList: ArrayList<ScanEntity>) {
        galleryList.addAll(newList)
        notifyDataSetChanged()
    }

    fun removeAll() {
        galleryList.clear()
        notifyDataSetChanged()
    }
}
