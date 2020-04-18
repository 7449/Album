package com.gallery.sample

import android.content.Context
import android.widget.Toast
import com.gallery.scan.ScanEntity
import com.gallery.ui.OnGalleryListener

/**
 * @author y
 */
class GalleryListener constructor(private val context: Context, private val entities: ArrayList<ScanEntity>?) : OnGalleryListener {

    override fun onGalleryContainerFinish() {
        toast("onGalleryActivityFinish")
    }

    override fun onGalleryContainerBackPressed() {
        toast("onGalleryContainerBackPressed")
    }

    override fun onGalleryPreFileNotExist() {
        toast("preview image has been deleted")
    }

    override fun onGalleryContainerFinderEmpty() {
        toast("folder directory is empty")
    }

    override fun onGalleryPreEmpty() {
        toast("preview no image")
    }

    override fun onGallerySelectEmpty() {
        toast("select no image")
    }

    override fun onGalleryContainerPreSelectEmpty() {
        toast("PreviewActivity,  preview no image")
    }

    override fun onGalleryResources(entities: List<ScanEntity>) {
        toast("select count :" + entities.size)
        this.entities ?: return
        this.entities.clear()
        this.entities.addAll(entities)
    }

    private fun toast(s: String) {
        Toast.makeText(context.applicationContext, s, Toast.LENGTH_SHORT).show()
    }
}
