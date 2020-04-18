package com.gallery.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGallery
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.ui.adapter.vh.CameraViewHolder
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.gallery.scan.ScanEntity
import com.xadapter.vh.XViewHolder

class GalleryAdapter(
        private val display: Int,
        private val galleryBundle: GalleryBundle,
        private val galleryCallback: IGalleryCallback,
        private val imageLoader: IGalleryImageLoader,
        private val galleryItemClickListener: OnGalleryItemClickListener
) : RecyclerView.Adapter<XViewHolder>() {

    interface OnGalleryItemClickListener {
        fun onCameraItemClick(view: View, position: Int, galleryEntity: ScanEntity)
        fun onPhotoItemClick(view: View, position: Int, galleryEntity: ScanEntity)
    }

    companion object {
        private const val TYPE_CAMERA = 0
        private const val TYPE_PHOTO = 1
    }

    private val galleryList: ArrayList<ScanEntity> = ArrayList()
    private val selectList: ArrayList<ScanEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> {
                val cameraView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery_camera, parent, false)
                val cameraViewHolder = CameraViewHolder(cameraView, galleryBundle)
                cameraView.setOnClickListener { v -> galleryItemClickListener.onCameraItemClick(v, cameraViewHolder.bindingAdapterPosition, galleryList[cameraViewHolder.bindingAdapterPosition]) }
                cameraViewHolder
            }
            else -> {
                val photoView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery, parent, false)
                val photoViewHolder = PhotoViewHolder(photoView, galleryBundle, display, galleryCallback)
                photoView.setOnClickListener { v -> galleryItemClickListener.onPhotoItemClick(v, photoViewHolder.bindingAdapterPosition, galleryList[photoViewHolder.bindingAdapterPosition]) }
                photoViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: XViewHolder, position: Int) {
        when (holder) {
            is CameraViewHolder -> holder.camera()
            is PhotoViewHolder -> holder.photo(item(position), currentSelectList, imageLoader)
        }
    }

    override fun getItemCount(): Int = galleryList.size

    override fun getItemViewType(position: Int): Int = when {
        galleryList.isEmpty() || galleryList[position].id != IGallery.CAMERA_PARENT_ID -> TYPE_PHOTO
        else -> TYPE_CAMERA
    }

    fun cleanAll() {
        galleryList.clear()
        notifyDataSetChanged()
    }

    fun cleanSelectAll() {
        selectList.clear()
        notifyDataSetChanged()
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
        currentList.forEach { it.isCheck = false }
        selectList.forEach { select -> currentList.find { it.id == select.id }?.isCheck = true }
        notifyDataSetChanged()
    }

    fun isCheck(position: Int) = galleryList[position].isCheck

    fun item(position: Int) = galleryList[position]

    fun containsSelect(selectEntity: ScanEntity) = selectList.contains(selectEntity)

    fun removeSelectEntity(removeEntity: ScanEntity) = selectList.remove(removeEntity)

    fun addSelectEntity(addEntity: ScanEntity) = selectList.add(addEntity)

    fun addEntity(position: Int, entity: ScanEntity) = currentList.add(position, entity)

    val isNotEmpty: Boolean
        get() = galleryList.isNotEmpty()

    val currentSelectList: ArrayList<ScanEntity>
        get() = selectList

    val currentList: ArrayList<ScanEntity>
        get() = galleryList
}
