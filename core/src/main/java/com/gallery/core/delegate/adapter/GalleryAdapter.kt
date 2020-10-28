package com.gallery.core.delegate.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.kotlin.expand.net.isFileExistsExpand
import androidx.kotlin.expand.view.showExpand
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.delegate.entity.ScanEntity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gallery_item_gallery.*
import kotlinx.android.synthetic.main.gallery_item_gallery_camera.*

class GalleryAdapter(
        private val display: Int,
        private val galleryBundle: GalleryBundle,
        private val galleryCallback: IGalleryCallback,
        private val imageLoader: IGalleryImageLoader,
        private val galleryItemClickListener: OnGalleryItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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
        currentList.clear()
        currentList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addSelectAll(newList: ArrayList<ScanEntity>) {
        currentSelectList.clear()
        currentSelectList.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateEntity() {
        currentList.forEach { it.isSelected = false }
        currentSelectList.forEach { select -> currentList.find { it.id == select.id }?.isSelected = true }
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
        get() = currentList.isNotEmpty()

    val currentSelectList: ArrayList<ScanEntity>
        get() = selectList

    val currentList: ArrayList<ScanEntity>
        get() = galleryList

    @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
    class CameraViewHolder(
            itemView: View,
            private val galleryBundle: GalleryBundle,
    ) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View
            get() = itemView

        fun camera() {
            val drawable: Drawable? = ContextCompat.getDrawable(containerView.context, galleryBundle.cameraDrawable)
            drawable?.colorFilter = PorterDuffColorFilter(galleryBundle.cameraDrawableColor, PorterDuff.Mode.SRC_ATOP)
            galleryImageCameraTv.text = galleryBundle.cameraText
            galleryImageCameraTv.textSize = galleryBundle.cameraTextSize
            galleryImageCameraTv.setTextColor(galleryBundle.cameraTextColor)
            gallery_camera_root_view.setBackgroundColor(galleryBundle.cameraBackgroundColor)
            galleryImageCamera.setImageDrawable(drawable)
        }

    }

    @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
    class PhotoViewHolder(
            itemView: View,
            private val galleryBundle: GalleryBundle,
            private val display: Int,
            private val galleryCallback: IGalleryCallback,
    ) : RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View
            get() = itemView

        private val container: FrameLayout = galleryContainer
        private val checkBox: TextView = galleryCheckBox

        fun photo(position: Int, scanEntity: ScanEntity, selectList: ArrayList<ScanEntity>, imageLoader: IGalleryImageLoader) {
            imageLoader.onDisplayGallery(display, display, scanEntity, container, checkBox)
            container.setBackgroundColor(galleryBundle.photoBackgroundColor)
            if (galleryBundle.radio) {
                return
            }
            checkBox.setOnClickListener { clickCheckBox(position, scanEntity, selectList) }
            checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
            checkBox.isSelected = scanEntity.isSelected
            checkBox.showExpand()
        }

        private fun clickCheckBox(position: Int, scanEntity: ScanEntity, selectList: ArrayList<ScanEntity>) {
            if (!scanEntity.uri.isFileExistsExpand(containerView.context)) {
                if (selectList.contains(scanEntity)) {
                    selectList.remove(scanEntity)
                }
                checkBox.isSelected = false
                scanEntity.isSelected = false
                galleryCallback.onClickCheckBoxFileNotExist(containerView.context, scanEntity)
                return
            }
            if (!selectList.contains(scanEntity) && selectList.size >= galleryBundle.multipleMaxCount) {
                galleryCallback.onClickCheckBoxMaxCount(containerView.context, scanEntity)
                return
            }
            if (!scanEntity.isSelected) {
                scanEntity.isSelected = true
                checkBox.isSelected = true
                selectList.add(scanEntity)
            } else {
                selectList.remove(scanEntity)
                scanEntity.isSelected = false
                checkBox.isSelected = false
            }
            galleryCallback.onChangedCheckBox(position, scanEntity)
        }
    }
}
