package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.safeToastExpand
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.ui.GalleryListener

class GalleryCallback(private val activity: FragmentActivity) : GalleryListener {

    override fun onGalleryCropResource(uri: Uri) {
        uri.toString().safeToastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanFileEntity) {
        scanEntity.toString().safeToastExpand(activity)
    }

    override fun onGalleryResources(entities: List<ScanFileEntity>) {
        entities.toString().safeToastExpand(activity)
    }

    override fun onGalleryCancel() {
        "取消选择".safeToastExpand(activity)
    }

}