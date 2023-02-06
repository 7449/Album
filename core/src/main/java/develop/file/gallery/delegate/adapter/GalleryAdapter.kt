package develop.file.gallery.delegate.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.callback.IGalleryCallback
import develop.file.gallery.callback.IGalleryImageLoader
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.ContextCompat.drawable
import develop.file.gallery.extensions.UriCompat.exists
import develop.file.gallery.extensions.ViewCompat.checkBox
import develop.file.gallery.extensions.ViewCompat.frame
import develop.file.gallery.extensions.ViewCompat.imageView
import develop.file.gallery.extensions.ViewCompat.setOnClick
import develop.file.gallery.extensions.ViewCompat.show
import develop.file.gallery.extensions.ViewCompat.textView
import develop.file.gallery.extensions.ViewCompat.verticalLinear

internal class GalleryAdapter(
    private val configs: GalleryConfigs,
    private val galleryCallback: IGalleryCallback,
    private val imageLoader: IGalleryImageLoader,
    private val itemClickListener: OnGalleryItemClickListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnGalleryItemClickListener {
        fun onCameraItemClick()
        fun onPhotoItemClick(position: Int, scanEntity: ScanEntity)
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
        val display = configs.display(parent.context)
        return when (viewType) {
            TYPE_CAMERA -> parent.newCameraHolder(display, configs)
                .setOnClick { itemClickListener.onCameraItemClick() }

            else -> parent.newPictureHolder(display, configs, galleryCallback)
                .setOnClick { itemClickListener.onPhotoItemClick(it, galleryList[it]) }
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

    private fun ViewGroup.newCameraHolder(display: Int, configs: GalleryConfigs): CameraViewHolder {
        val rootView = verticalLinear(display)
        val imageCamera = rootView.imageView(divider)
        val imageCameraTv = rootView.textView(divider)
        rootView.addView(imageCamera)
        rootView.addView(imageCameraTv)
        return CameraViewHolder(rootView, imageCamera, imageCameraTv, configs)
    }

    private fun ViewGroup.newPictureHolder(
        display: Int,
        configs: GalleryConfigs,
        callback: IGalleryCallback
    ): PhotoViewHolder {
        val rootView = frame(divider, display)
        val checkBox = rootView.checkBox(divider, display)
        rootView.addView(checkBox)
        return PhotoViewHolder(rootView, checkBox, display, configs, callback)
    }

    private class CameraViewHolder(
        rootView: View,
        private val imageCamera: ImageView,
        private val imageCameraTv: TextView,
        private val galleryConfigs: GalleryConfigs
    ) : RecyclerView.ViewHolder(rootView) {

        fun cameraSetting() {
            imageCameraTv.text = galleryConfigs.cameraConfig.text
            imageCameraTv.textSize = galleryConfigs.cameraConfig.textSize
            imageCameraTv.setTextColor(galleryConfigs.cameraConfig.textColor)
            imageCamera.setImageDrawable(itemView.context.drawable(galleryConfigs.cameraConfig.icon))
            imageCamera.setBackgroundColor(galleryConfigs.cameraConfig.background)
            imageCameraTv.setBackgroundColor(galleryConfigs.cameraConfig.background)
        }

    }

    private class PhotoViewHolder(
        private val rootView: FrameLayout,
        private val checkBox: AppCompatTextView,
        private val display: Int,
        private val configs: GalleryConfigs,
        private val galleryCallback: IGalleryCallback,
    ) : RecyclerView.ViewHolder(rootView) {

        fun photo(
            position: Int,
            entity: ScanEntity,
            selectList: ArrayList<ScanEntity>,
            imageLoader: IGalleryImageLoader
        ) {
            imageLoader.onDisplayHomeGallery(display, display, entity, rootView)
            checkBox.setOnClickListener(null)
            if (configs.radio) return
            checkBox.setOnClickListener { clickItemView(position, entity, selectList) }
            checkBox.setBackgroundResource(configs.cameraConfig.checkBoxIcon)
            checkBox.isSelected = entity.isSelected
            checkBox.show()
        }

        private fun clickItemView(
            position: Int,
            entity: ScanEntity,
            selectList: ArrayList<ScanEntity>
        ) {
            if (!entity.uri.exists(itemView.context)) {
                if (selectList.contains(entity)) {
                    selectList.remove(entity)
                }
                checkBox.isSelected = false
                entity.isSelected = false
                galleryCallback.onSelectMultipleFileNotExist(entity)
                return
            }
            if (!selectList.contains(entity) && selectList.size >= configs.maxCount) {
                galleryCallback.onSelectMultipleMaxCount()
                return
            }
            if (!entity.isSelected) {
                entity.isSelected = true
                checkBox.isSelected = true
                selectList.add(entity)
            } else {
                selectList.remove(entity)
                entity.isSelected = false
                checkBox.isSelected = false
            }
            galleryCallback.onSelectMultipleFileChanged(position, entity)
        }

    }
}