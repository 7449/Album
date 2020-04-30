package com.gallery.sample

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.toastExpand
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryListener

class SimpleGalleryCallback : GalleryListener {

    override fun onGalleryCropResource(activity: FragmentActivity, uri: Uri): Boolean {
        uri.toString().toastExpand(activity)
        return true
    }

    override fun onGalleryResource(activity: FragmentActivity, scanEntity: ScanEntity): Boolean {
        scanEntity.toString().toastExpand(activity)
        return true
    }

    override fun onGalleryResources(activity: FragmentActivity, entities: List<ScanEntity>): Boolean {
        entities.toString().toastExpand(activity)
        return true
    }

}