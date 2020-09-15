package com.gallery.core.ui.adapter.vh

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.net.isFileExistsExpand
import androidx.kotlin.expand.view.showExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.delegate.ScanEntity
import com.gallery.scan.args.file.externalUriExpand
import com.xadapter.vh.XViewHolder

class PhotoViewHolder(itemView: View,
                      private val galleryBundle: GalleryBundle,
                      private val display: Int,
                      private val galleryCallback: IGalleryCallback) : XViewHolder(itemView) {

    private val container: FrameLayout = frameLayout(R.id.galleryContainer)
    private val checkBox: TextView = textView(R.id.galleryCheckBox)

    fun photo(position: Int, scanEntity: ScanEntity, selectList: ArrayList<ScanEntity>, imageLoader: IGalleryImageLoader) {
        imageLoader.onDisplayGallery(display, display, scanEntity, container, checkBox)
        container.setBackgroundColor(galleryBundle.photoBackgroundColor)
        if (galleryBundle.radio) {
            return
        }
        checkBox.showExpand()
        checkBox.isSelected = scanEntity.isSelected
        checkBox.setBackgroundResource(galleryBundle.checkBoxDrawable)
        checkBox.setOnClickListener { clickCheckBox(position, scanEntity, selectList) }
    }

    private fun clickCheckBox(position: Int, scanEntity: ScanEntity, selectList: ArrayList<ScanEntity>) {
        if (!scanEntity.delegate.externalUriExpand.isFileExistsExpand(context)) {
            if (selectList.contains(scanEntity)) {
                selectList.remove(scanEntity)
            }
            checkBox.isSelected = false
            scanEntity.isSelected = false
            galleryCallback.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
            return
        }
        if (!selectList.contains(scanEntity) && selectList.size >= galleryBundle.multipleMaxCount) {
            galleryCallback.onClickCheckBoxMaxCount(context, galleryBundle, scanEntity)
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
        galleryCallback.onChangedCheckBox(position, scanEntity.isSelected, galleryBundle, scanEntity)
    }
}