package com.gallery.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.delegate.ScanEntity
import com.gallery.core.ui.adapter.vh.CameraViewHolder
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.xadapter.vh.XViewHolder

class GalleryAdapter(
        private val display: Int,
        private val galleryBundle: GalleryBundle,
        private val galleryCallback: IGalleryCallback,
        private val imageLoader: IGalleryImageLoader,
        private val galleryItemClickListener: OnGalleryItemClickListener
) : RecyclerView.Adapter<XViewHolder>() {

    interface OnGalleryItemClickListener {
        fun onCameraItemClick(view: View, position: Int, scanEntity: ScanEntity)
        fun onPhotoItemClick(view: View, position: Int, scanEntity: ScanEntity)
    }

    companion object {
        const val CAMERA: Long = (-1).toLong()
        private const val TYPE_CAMERA = 0
        private const val TYPE_PHOTO = 1
    }

    private val galleryList: ArrayList<ScanEntity> = arrayListOf()
    private val selectList: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> {
                val cameraView: View = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery_camera, parent, false)
                val cameraViewHolder = CameraViewHolder(cameraView, galleryBundle)
                cameraView.setOnClickListener { v -> galleryItemClickListener.onCameraItemClick(v, cameraViewHolder.bindingAdapterPosition, galleryList[cameraViewHolder.bindingAdapterPosition]) }
                cameraViewHolder
            }
            else -> {
                val photoView: View = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery, parent, false)
                val photoViewHolder = PhotoViewHolder(photoView, galleryBundle, display, galleryCallback)
                photoView.setOnClickListener { v -> galleryItemClickListener.onPhotoItemClick(v, photoViewHolder.bindingAdapterPosition, galleryList[photoViewHolder.bindingAdapterPosition]) }
                photoViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: XViewHolder, position: Int) {
        when (holder) {
            is CameraViewHolder -> holder.camera()
            is PhotoViewHolder -> holder.photo(position, galleryList[position], currentSelectList, imageLoader)
        }
    }

    override fun getItemCount(): Int = galleryList.size

    override fun getItemViewType(position: Int): Int = when {
        galleryList.isEmpty() || galleryList[position].parent != CAMERA -> TYPE_PHOTO
        else -> TYPE_CAMERA
    }

    fun addAll(newList: ArrayList<ScanEntity>) {
        galleryList.clear()
        galleryList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addSelectAll(newList: ArrayList<ScanEntity>) {
        selectList.clear()
        selectList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateEntity() {
        currentList.forEach { it.isSelected = false }
        selectList.forEach { select -> currentList.find { it.id == select.id }?.isSelected = true }
        notifyDataSetChanged()
    }

    fun addEntity(position: Int, entity: ScanEntity) {
        if (!currentList.contains(entity)) {
            currentList.add(position, entity)
        }
    }

    fun addEntity(entity: ScanEntity) {
        if (!currentList.contains(entity)) {
            currentList.add(entity)
        }
    }

    val isNotEmpty: Boolean
        get() = galleryList.isNotEmpty()

    val currentSelectList: ArrayList<ScanEntity>
        get() = selectList

    val currentList: ArrayList<ScanEntity>
        get() = galleryList
}
