package com.gallery.core.ui.adapter.vh

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.Gallery
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.action.GalleryAction
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.fileExists
import com.gallery.core.ext.show
import com.gallery.scan.ScanEntity

class PhotoViewHolder(itemView: View,
                      private val galleryBundle: GalleryBundle,
                      private val display: Int,
                      private val galleryAction: GalleryAction?) : RecyclerView.ViewHolder(itemView) {

    private val container: FrameLayout = itemView.findViewById(R.id.galleryContainer)
    private val checkBox: AppCompatCheckBox = itemView.findViewById(R.id.galleryCheckBox)

    fun photo(position: Int, galleryEntity: ScanEntity, multipleList: ArrayList<ScanEntity>) {
        Gallery.instance.galleryImageLoader?.displayGallery(display, display, galleryEntity, container)
        container.setBackgroundColor(ContextCompat.getColor(itemView.context, galleryBundle.photoBackgroundColor))
        if (galleryBundle.radio) {
            return
        }
        checkBox.show()
        checkBox.isChecked = galleryEntity.isCheck
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener {
            if (!checkBox.context.fileExists(galleryEntity.externalUri())) {
                checkBox.isChecked = false
                if (multipleList.contains(galleryEntity)) {
                    multipleList.remove(galleryEntity)
                }
                Gallery.instance.galleryListener?.onGalleryCheckFileNotExist()
                return@setOnClickListener
            }
            if (galleryAction?.onGalleryCheckBoxFilter(itemView, position, galleryEntity) == true) {
                return@setOnClickListener
            }
            if (!multipleList.contains(galleryEntity) && multipleList.size >= galleryBundle.multipleMaxCount) {
                checkBox.isChecked = false
                Gallery.instance.galleryListener?.onGalleryMaxCount()
                return@setOnClickListener
            }
            if (!galleryEntity.isCheck) {
                galleryEntity.isCheck = true
                multipleList.add(galleryEntity)
            } else {
                multipleList.remove(galleryEntity)
                galleryEntity.isCheck = false
            }
            galleryAction?.onChangedCheckBoxCount(itemView, multipleList.size, galleryEntity)
            Gallery.instance.galleryListener?.onGalleryCheckBox(multipleList.size, galleryBundle.multipleMaxCount)
        }
    }
}