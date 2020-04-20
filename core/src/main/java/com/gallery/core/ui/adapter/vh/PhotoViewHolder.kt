package com.gallery.core.ui.adapter.vh

import android.view.View
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.ext.externalUri
import com.gallery.scan.ScanEntity
import com.kotlin.x.show
import com.kotlin.x.uriExists
import com.xadapter.vh.XViewHolder
import com.xadapter.vh.checkBox
import com.xadapter.vh.context
import com.xadapter.vh.frameLayout

class PhotoViewHolder(itemView: View,
                      private val galleryBundle: GalleryBundle,
                      private val display: Int,
                      private val galleryCallback: IGalleryCallback) : XViewHolder(itemView) {

    private val container = frameLayout(R.id.galleryContainer)
    private val checkBox = checkBox(R.id.galleryCheckBox)

    fun photo(galleryEntity: ScanEntity, multipleList: ArrayList<ScanEntity>, imageLoader: IGalleryImageLoader) {
        imageLoader.onDisplayGallery(display, display, galleryEntity, container)
        container.setBackgroundColor(galleryBundle.photoBackgroundColor)
        if (galleryBundle.radio) {
            return
        }
        checkBox.show()
        checkBox.isChecked = galleryEntity.isCheck
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener {
            if (!checkBox.context.uriExists(galleryEntity.externalUri())) {
                checkBox.isChecked = false
                if (multipleList.contains(galleryEntity)) {
                    multipleList.remove(galleryEntity)
                }
                galleryCallback.onClickCheckBoxFileNotExist(context, galleryEntity)
                return@setOnClickListener
            }
            if (!multipleList.contains(galleryEntity) && multipleList.size >= galleryBundle.multipleMaxCount) {
                checkBox.isChecked = false
                galleryCallback.onClickCheckBoxMaxCount(context, galleryEntity)
                return@setOnClickListener
            }
            if (!galleryEntity.isCheck) {
                galleryEntity.isCheck = true
                multipleList.add(galleryEntity)
            } else {
                multipleList.remove(galleryEntity)
                galleryEntity.isCheck = false
            }
            galleryCallback.onChangedCheckBox(galleryEntity.isCheck, galleryEntity)
        }
    }
}