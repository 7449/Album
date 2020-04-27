package com.gallery.sample

import android.net.Uri
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryCallback
import com.gallery.ui.activity.GalleryActivity
import com.kotlin.x.toastExpand

class SimpleGalleryCallback : GalleryCallback {

    override fun onGalleryCropResource(activity: GalleryActivity, uri: Uri): Boolean {
        uri.toString().toastExpand(activity)
        return true
    }

    override fun onGalleryResource(activity: GalleryActivity, scanEntity: ScanEntity): Boolean {
        scanEntity.toString().toastExpand(activity)
        return true
    }

    override fun onGalleryResources(activity: GalleryActivity, entities: List<ScanEntity>): Boolean {
        entities.toString().toastExpand(activity)
        return true
    }

}