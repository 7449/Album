package com.gallery.core.delegate.adapter

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryConfigs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.fileExists
import com.gallery.core.extensions.hide
import com.gallery.core.extensions.show

internal class GalleryAdapter(
    private val display: Int,
    private val configs: GalleryConfigs,
    private val galleryCallback: IGalleryCallback,
    private val imageLoader: IGalleryImageLoader,
    private val itemClickListener: OnGalleryItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnGalleryItemClickListener {
        fun onCameraItemClick(view: View, position: Int, scanEntity: ScanEntity)
        fun onPhotoItemClick(view: View, position: Int, scanEntity: ScanEntity)
    }

    companion object {
        const val CAMERA: Long = (-1).toLong()
        private const val divider = 2
        private const val TYPE_CAMERA = 0
        private const val TYPE_PHOTO = 1
    }

    private val galleryList: ArrayList<ScanEntity> = arrayListOf()
    private val selectList: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CAMERA -> CameraViewHolder.newInstance(parent, display, configs).apply {
                itemView.setOnClickListener { v ->
                    itemClickListener.onCameraItemClick(
                        v,
                        bindingAdapterPosition,
                        galleryList[bindingAdapterPosition]
                    )
                }
            }

            else -> {
                val photoViewHolder =
                    PhotoViewHolder.newInstance(parent, configs, display, galleryCallback)
                photoViewHolder.itemView.setOnClickListener { v ->
                    itemClickListener.onPhotoItemClick(
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
            is CameraViewHolder -> holder.cameraSetting()
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

    private class CameraViewHolder(
        private val rootView: View,
        private val imageCamera: ImageView,
        private val imageCameraTv: TextView,
        private val galleryConfigs: GalleryConfigs
    ) : RecyclerView.ViewHolder(rootView) {

        companion object {
            fun newInstance(
                parent: ViewGroup,
                display: Int,
                configs: GalleryConfigs
            ): CameraViewHolder {
                val rootView = LinearLayout(parent.context).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(display, display)
                }
                val imageCamera = AppCompatImageView(rootView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    ).apply {
                        setMargins(divider, divider, divider, 0)
                        weight = 1f
                    }
                }
                val imageCameraTv = AppCompatTextView(rootView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(divider, 0, divider, divider)
                    }
                }
                imageCameraTv.gravity = Gravity.CENTER
                rootView.addView(imageCamera)
                rootView.addView(imageCameraTv)
                return CameraViewHolder(rootView, imageCamera, imageCameraTv, configs)
            }
        }

        fun cameraSetting() {
            val drawable =
                ContextCompat.getDrawable(itemView.context, galleryConfigs.cameraConfig.icon)
            drawable?.colorFilter =
                PorterDuffColorFilter(
                    galleryConfigs.cameraConfig.iconColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            imageCameraTv.text = galleryConfigs.cameraConfig.text
            imageCameraTv.textSize = galleryConfigs.cameraConfig.textSize
            imageCameraTv.setTextColor(galleryConfigs.cameraConfig.textColor)
            imageCamera.setImageDrawable(drawable)
            imageCamera.setBackgroundColor(galleryConfigs.cameraConfig.bg)
            imageCameraTv.setBackgroundColor(galleryConfigs.cameraConfig.bg)
        }

    }

    class PhotoViewHolder(
        private val rootView: View,
        private val container: FrameLayout,
        private val checkBox: AppCompatTextView,
        private val galleryConfigs: GalleryConfigs,
        private val display: Int,
        private val galleryCallback: IGalleryCallback,
    ) : RecyclerView.ViewHolder(rootView) {

        companion object {
            fun newInstance(
                parent: ViewGroup,
                configs: GalleryConfigs,
                display: Int,
                galleryCallback: IGalleryCallback
            ): PhotoViewHolder {
                val rootView = FrameLayout(parent.context).apply {
                    layoutParams = FrameLayout.LayoutParams(display, display)
                }
                val galleryContainer = FrameLayout(rootView.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    ).apply { setPadding(divider) }
                }
                val galleryCheckBox = AppCompatTextView(rootView.context).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setPadding(divider)
                        setMargins(divider)
                        gravity = Gravity.END
                    }
                }
                galleryCheckBox.hide()
                rootView.addView(galleryContainer)
                rootView.addView(galleryCheckBox)
                return PhotoViewHolder(
                    rootView,
                    galleryContainer,
                    galleryCheckBox,
                    configs,
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
            if (galleryConfigs.radio) {
                return
            }
            checkBox.setOnClickListener { clickItemView(position, scanEntity, selectList) }
            checkBox.setBackgroundResource(galleryConfigs.cameraConfig.selectIcon)
            checkBox.isSelected = scanEntity.isSelected
            checkBox.show()
        }

        private fun clickItemView(
            position: Int,
            scanEntity: ScanEntity,
            selectList: ArrayList<ScanEntity>
        ) {
            if (!scanEntity.uri.fileExists(itemView.context)) {
                if (selectList.contains(scanEntity)) {
                    selectList.remove(scanEntity)
                }
                checkBox.isSelected = false
                scanEntity.isSelected = false
                galleryCallback.onClickCheckBoxFileNotExist(itemView.context, scanEntity)
                return
            }
            if (!selectList.contains(scanEntity) && selectList.size >= galleryConfigs.maxCount) {
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
            galleryCallback.onSelectItemChanged(position, scanEntity)
        }
    }
}