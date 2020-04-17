package com.gallery.sample

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.gallery.ui.OnGalleryListener
import com.gallery.scan.ScanEntity

/**
 * @author y
 */
class GalleryListener internal constructor(context: Context, private val list: ArrayList<ScanEntity>?) : OnGalleryListener {

    override fun onGalleryCheckBox(selectCount: Int, maxCount: Int) {
        toast("onCheckBoxGallery:$selectCount")
    }

    override fun onGalleryContainerFinish() {
        toast("onGalleryActivityFinish")
    }

    override fun onGalleryResultCameraError() {
        toast("onGalleryResultCameraError")
    }

    private val context: Context = context.applicationContext

    private fun toast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onGalleryPermissionsDenied(type: Int) {
        toast("permissions error")
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

    override fun onGalleryFileNotExist() {
        toast("gallery image has been deleted")
    }

    override fun onGalleryContainerPreSelectEmpty() {
        toast("PreviewActivity,  preview no image")
    }

    override fun onGalleryCheckFileNotExist() {
        toast("check box  image has been deleted")
    }

    override fun onGalleryCropCanceled() {
        toast("cancel crop")
    }

    override fun onGalleryCameraCanceled() {
        toast("cancel camera")
    }

    override fun onGalleryUCropError(data: Throwable?) {
        Log.i("GALLERY", data?.message ?: "")
        toast("crop error:$data")
    }

    override fun onGalleryUCropResources(uri: Uri) {
        toast("crop file:$uri")
    }

    override fun onGalleryMaxCount() {
        toast("select max count")
    }

    override fun onGalleryContainerBackPressed() {
        toast("GalleryActivity Back")
    }

    override fun onGalleryOpenCameraError() {
        toast("camera error")
    }

    override fun onGalleryEmpty() {
        toast("no image")
    }

    override fun onGalleryVideoPlayError() {
        toast("play video error : checked video app")
    }

    override fun onGalleryResources(list: List<ScanEntity>) {
        toast("select count :" + list.size)
        if (this.list == null) return
        this.list.clear()
        this.list.addAll(list)
    }
}
