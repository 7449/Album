package com.gallery.sample

import android.net.Uri
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryCallback
import com.gallery.ui.activity.GalleryActivity
import com.kotlin.x.toast

class SimpleGalleryCallback : GalleryCallback {

    override fun onGalleryCropResource(activity: GalleryActivity, uri: Uri) {
        uri.toString().toast(activity)
    }

    override fun onGalleryResource(activity: GalleryActivity, scanEntity: ScanEntity) {
        scanEntity.toString().toast(activity)
    }

    override fun onGalleryResources(activity: GalleryActivity, entities: List<ScanEntity>) {
        entities.toString().toast(activity)
    }

}