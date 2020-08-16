package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.toastExpand
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryListener

class GalleryCallback(private val activity: FragmentActivity) : GalleryListener {

    override fun onGalleryCropResource(uri: Uri) {
        uri.toString().toastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanEntity) {
        scanEntity.toString().toastExpand(activity)
    }

    override fun onGalleryResources(entities: List<ScanEntity>) {
        entities.toString().toastExpand(activity)
    }

    override fun onGalleryCancel() {
        "取消选择".toastExpand(activity)
    }

}