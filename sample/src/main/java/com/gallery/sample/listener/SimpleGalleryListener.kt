package com.gallery.sample.listener

import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.internal.call.GalleryListener
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.queryDataExpand

open class SimpleGalleryListener(private val activity: FragmentActivity) : GalleryListener {

    private fun showDialog(msg: String) {
        AlertDialog
                .Builder(activity)
                .setMessage(msg)
                .show()
    }

    override fun onGalleryCropResource(uri: Uri, vararg args: Any) {
        showDialog(uri.toString() + "\n" + activity.contentResolver.queryDataExpand(uri))
    }

    override fun onGalleryResource(scanEntity: ScanEntity, vararg args: Any) {
        showDialog(scanEntity.toString())
    }

    override fun onGalleryResources(entities: List<ScanEntity>, vararg args: Any) {
        showDialog(entities.toString() + "\n" + args.asList().toString())
    }

    override fun onGalleryCancel(vararg args: Any) {
    }

}