package com.gallery.core.delegate.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.hideExpand
import com.gallery.core.extensions.isFileExistsExpand
import com.gallery.core.extensions.showExpand

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
                val cameraViewHolder =
                    CameraViewHolder.newInstance(parent, galleryBundle)
                cameraViewHolder.itemView.setOnClickListener { v ->
                    galleryItemClickListener.onCameraItemClick(
                        v,
                        cameraViewHolder.bindingAdapterPosition,
                        galleryList[cameraViewHolder.bindingAdapterPosition]
                    )
                }
                cameraViewHolder
            }
            else -> {
                val photoViewHolder =
                    PhotoViewHolder.newInstance(parent, galleryBundle, display, galleryCallback)
                photoViewHolder.itemView.setOnClickListener { v ->
                    galleryItemClickListener.onPhotoItemClick(
                        v,
                        photoViewHolder.bindingAdapterPosition,
                        galleryList[photoViewHolder.bindingAdapterPosition]
                    )
                }
                photoViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CameraViewHolder -> holder.camera()
            is PhotoViewHolder -> holder.photo(
                position,
                galleryList[position],
                currentSelectList,
                imageLoader
            )
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
        currentSelectList.forEach { select ->
            currentList.find { it.id == select.id }?.isSelected = true
        }
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

    class CameraViewHolder(
        private val rootView: FrameLayout,
        private val galleryImageCamera: AppCompatImageView,
        private val galleryImageCameraTv: AppCompatTextView,
        private val galleryBundle: GalleryBundle
    ) : RecyclerView.ViewHolder(rootView) {

        companion object {
            fun newInstance(parent: ViewGroup, galleryBundle: GalleryBundle): CameraViewHolder {
                val rootView = FrameLayout(parent.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                val galleryImageCamera = AppCompatImageView(rootView.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        this.gravity = Gravity.CENTER
                    }
                }
                val galleryImageCameraTv = AppCompatTextView(rootView.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        this.gravity = Gravity.CENTER or Gravity.BOTTOM
                        this.bottomMargin = 5
                    }
                }
                rootView.addView(galleryImageCamera)
                rootView.addView(galleryImageCameraTv)
                return CameraViewHolder(
                    rootView,
                    galleryImageCamera,
                    galleryImageCameraTv,
                    galleryBundle
                )
            }
        }

        fun camera() {
            val drawable: Drawable? =
                ContextCompat.getDrawable(itemView.context, galleryBundle.cameraDrawable)
            drawable?.colorFilter =
                PorterDuffColorFilter(galleryBundle.cameraDrawableColor, PorterDuff.Mode.SRC_ATOP)
            galleryImageCameraTv.text = galleryBundle.cameraText
            galleryImageCameraTv.textSize = galleryBundle.cameraTextSize
            galleryImageCameraTv.setTextColor(galleryBundle.cameraTextColor)
            galleryImageCamera.setImageDrawable(drawable)
            rootView.setBackgroundColor(galleryBundle.cameraBackgroundColor)
        }

    }

    class PhotoViewHolder(
        private val rootView: FrameLayout,
        private val container: FrameLayout,
        private val checkBox: AppCompatTextView,
        private val galleryBundle: GalleryBundle,
        private val display: Int,
        private val galleryCallback: IGalleryCallback,
    ) : RecyclerView.ViewHolder(rootView) {

        companion object {
            fun newInstance(
                parent: ViewGroup,
                galleryBundle: GalleryBundle,
                display: Int,
                galleryCallback: IGalleryCallback
            ): PhotoViewHolder {
                val rootView = FrameLayout(parent.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                val galleryContainer = FrameLayout(rootView.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                val galleryCheckBox = AppCompatTextView(rootView.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        this.gravity = Gravity.END
                        this.bottomMargin = 5
                    }
                }
                galleryCheckBox.hideExpand()
                rootView.addView(galleryContainer)
                rootView.addView(galleryCheckBox)
                return PhotoViewHolder(
                    rootView,
                    galleryContainer,
                    galleryCheckBox,
                    galleryBundle,
                    display,
                    galleryCallback
                )
            }
        }

        fun photo(
            position: Int,
            scanEntity: ScanEntity,
            selectList: ArrayList<ScanEntity>,
            imageLoader: IGalleryImageLoader
        ) {
            imageLoader.onDisplayGallery(display, display, scanEntity, container, checkBox)
            if (galleryBundle.radio) {
                return
            }
            checkBox.setOnClickListener { clickItemView(position, scanEntity, selectList) }
            checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
            checkBox.isSelected = scanEntity.isSelected
            checkBox.showExpand()
        }

        private fun clickItemView(
            position: Int,
            scanEntity: ScanEntity,
            selectList: ArrayList<ScanEntity>
        ) {
            if (!scanEntity.uri.isFileExistsExpand(itemView.context)) {
                if (selectList.contains(scanEntity)) {
                    selectList.remove(scanEntity)
                }
                checkBox.isSelected = false
                scanEntity.isSelected = false
                galleryCallback.onClickCheckBoxFileNotExist(itemView.context, scanEntity)
                return
            }
            if (!selectList.contains(scanEntity) && selectList.size >= galleryBundle.multipleMaxCount) {
                galleryCallback.onClickItemMaxCount(itemView.context, scanEntity)
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
            galleryCallback.onChangedItem(position, scanEntity)
        }
    }
}