package com.gallery.sample

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.toastExpand
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryListener

class SimpleGalleryCallback : GalleryListener {

    override fun onGalleryCropResource(activity: FragmentActivity, uri: Uri) {
        uri.toString().toastExpand(activity)
    }

    override fun onGalleryResource(activity: FragmentActivity, scanEntity: ScanEntity) {
        scanEntity.toString().toastExpand(activity)
    }

    override fun onGalleryResources(activity: FragmentActivity, entities: List<ScanEntity>) {
        entities.toString().toastExpand(activity)
    }

    override fun onGalleryCancel(activity: FragmentActivity) {
        "取消选择".toastExpand(activity)
    }

}