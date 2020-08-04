package com.gallery.core.ui.adapter.vh

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.content.moveToNextToIdExpand
import androidx.kotlin.expand.view.showExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.expand.externalUri
import com.gallery.scan.ScanEntity
import com.xadapter.vh.XViewHolder

class PhotoViewHolder(itemView: View,
                      private val galleryBundle: GalleryBundle,
                      private val display: Int,
                      private val galleryInterceptor: IGalleryInterceptor,
                      private val galleryCallback: IGalleryCallback) : XViewHolder(itemView) {

    private val container: FrameLayout = frameLayout(R.id.galleryContainer)
    private val checkBox: TextView = textView(R.id.galleryCheckBox)

    fun photo(position: Int, galleryEntity: ScanEntity, selectList: ArrayList<ScanEntity>, imageLoader: IGalleryImageLoader) {
        imageLoader.onDisplayGallery(display, display, galleryEntity, container, checkBox)
        container.setBackgroundColor(galleryBundle.photoBackgroundColor)
        if (galleryBundle.radio) {
            return
        }
        checkBox.showExpand()
        checkBox.isSelected = galleryEntity.isCheck
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener { clickCheckBox(position, galleryEntity, selectList) }
    }

    private fun clickCheckBox(position: Int, galleryEntity: ScanEntity, selectList: ArrayList<ScanEntity>) {
        if (!checkBox.context.moveToNextToIdExpand(galleryEntity.externalUri())) {
            if (selectList.contains(galleryEntity)) {
                selectList.remove(galleryEntity)
            }
            checkBox.isSelected = false
            galleryEntity.isCheck = false
            galleryCallback.onClickCheckBoxFileNotExist(context, galleryBundle, galleryEntity)
            return
        }
        if (!selectList.contains(galleryEntity) && selectList.size >= galleryBundle.multipleMaxCount) {
            galleryCallback.onClickCheckBoxMaxCount(context, galleryBundle, galleryEntity)
            return
        }
        if (!galleryEntity.isCheck) {
            galleryEntity.isCheck = true
            checkBox.isSelected = true
            selectList.add(galleryEntity)
        } else {
            selectList.remove(galleryEntity)
            galleryEntity.isCheck = false
            checkBox.isSelected = false
        }
        galleryCallback.onChangedCheckBox(position, galleryEntity.isCheck, galleryBundle, galleryEntity)
    }
}