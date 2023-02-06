package com.gallery.sample.callbacks

import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import develop.file.gallery.compat.extensions.callbacks.GalleryListener
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.UriCompat.data

open class SimpleGalleryListener(
    private val activity: FragmentActivity,
    private val action: (items: List<ScanEntity>) -> Unit = {}
) :
    GalleryListener {

    private fun showDialog(msg: String) {
        AlertDialog
            .Builder(activity)
            .setMessage(msg)
            .show()
    }

    override fun onGalleryCropResource(uri: Uri, vararg args: Any) {
        showDialog(uri.toString() + "\n" + uri.data(activity))
    }

    override fun onGalleryResource(entity: ScanEntity, vararg args: Any) {
        showDialog(entity.toString())
    }

    override fun onGalleryResources(entities: List<ScanEntity>, vararg args: Any) {
        showDialog(entities.toString() + "\n" + args.asList().toString())
        action.invoke(entities)
    }

    override fun onGalleryCancel(vararg args: Any) {
    }

}