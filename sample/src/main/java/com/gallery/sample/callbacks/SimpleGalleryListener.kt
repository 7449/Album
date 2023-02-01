package com.gallery.sample.callbacks

import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.internal.call.GalleryListener
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.queryData

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
        showDialog(uri.toString() + "\n" + activity.contentResolver.queryData(uri))
    }

    override fun onGalleryResource(scanEntity: ScanEntity, vararg args: Any) {
        showDialog(scanEntity.toString())
    }

    override fun onGalleryResources(entities: List<ScanEntity>, vararg args: Any) {
        showDialog(entities.toString() + "\n" + args.asList().toString())
        action.invoke(entities)
    }

    override fun onGalleryCancel(vararg args: Any) {
    }

}