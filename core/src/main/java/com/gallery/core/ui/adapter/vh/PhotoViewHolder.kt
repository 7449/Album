package com.gallery.core.ui.adapter.vh

import android.view.View
import androidx.kotlin.expand.content.moveToNextToIdExpand
import androidx.kotlin.expand.view.showExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.ext.externalUri
import com.gallery.scan.ScanEntity
import com.xadapter.vh.XViewHolder
import com.xadapter.vh.context
import com.xadapter.vh.frameLayout
import com.xadapter.vh.textView

class PhotoViewHolder(itemView: View,
                      private val galleryBundle: GalleryBundle,
                      private val display: Int,
                      private val galleryInterceptor: IGalleryInterceptor,
                      private val galleryCallback: IGalleryCallback) : XViewHolder(itemView) {

    private val container = frameLayout(R.id.galleryContainer)
    private val checkBox = textView(R.id.galleryCheckBox)

    fun photo(position: Int, galleryEntity: ScanEntity, multipleList: ArrayList<ScanEntity>, imageLoader: IGalleryImageLoader) {
        imageLoader.onDisplayGallery(display, display, galleryEntity, container, checkBox)
        container.setBackgroundColor(galleryBundle.photoBackgroundColor)
        if (galleryBundle.radio) {
            return
        }
        checkBox.showExpand()
        checkBox.isSelected = galleryEntity.isCheck
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener {
            clickCheckBox(position, galleryEntity, multipleList)
        }
    }

    private fun clickCheckBox(position: Int, galleryEntity: ScanEntity, multipleList: ArrayList<ScanEntity>) {
        if (!checkBox.context.moveToNextToIdExpand(galleryEntity.externalUri())) {
            checkBox.isSelected = false
            if (multipleList.contains(galleryEntity)) {
                multipleList.remove(galleryEntity)
            }
            galleryCallback.onClickCheckBoxFileNotExist(context, galleryBundle, galleryEntity)
            return
        }
        if (!multipleList.contains(galleryEntity) && multipleList.size >= galleryBundle.multipleMaxCount) {
            galleryCallback.onClickCheckBoxMaxCount(context, galleryBundle, galleryEntity)
            return
        }
        if (!galleryEntity.isCheck) {
            galleryEntity.isCheck = true
            checkBox.isSelected = true
            multipleList.add(galleryEntity)
        } else {
            multipleList.remove(galleryEntity)
            galleryEntity.isCheck = false
            checkBox.isSelected = false
        }
        galleryCallback.onChangedCheckBox(position, galleryEntity.isCheck, galleryBundle, galleryEntity)
    }
}