package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.gallery.core.expand.safeToastExpand
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryListener

class GalleryCallback(private val activity: FragmentActivity) : GalleryListener {

    override fun onGalleryCropResource(uri: Uri) {
        uri.toString().safeToastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanEntity) {
        scanEntity.toString().safeToastExpand(activity)
    }

    override fun onGalleryResources(entities: List<ScanEntity>) {
        entities.toString().safeToastExpand(activity)
    }

    override fun onGalleryCancel() {
        "取消选择".safeToastExpand(activity)
    }

}