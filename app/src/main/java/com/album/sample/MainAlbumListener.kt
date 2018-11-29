package com.album.sample

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.album.AlbumEntity
import com.album.AlbumListener
import java.io.File

/**
 * @author y
 */
class MainAlbumListener internal constructor(context: Context, private val list: ArrayList<AlbumEntity>?) : AlbumListener {

    override fun onCheckBoxAlbum(count: Int, maxCount: Int) {
        toast("onCheckBoxAlbum:$count")
    }

    override fun onAlbumActivityFinish() {
        toast("onAlbumActivityFinish")
    }

    override fun onAlbumResultCameraError() {
        toast("onAlbumResultCameraError")
    }

    private val context: Context = context.applicationContext

    private fun toast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onAlbumPermissionsDenied(type: Int) {
        toast("permissions error")
    }

    override fun onAlbumPreviewFileNotExist() {
        toast("preview image has been deleted")
    }

    override fun onAlbumFinderEmpty() {
        toast("folder directory is empty")
    }

    override fun onAlbumPreviewEmpty() {
        toast("preview no image")
    }

    override fun onAlbumSelectEmpty() {
        toast("select no image")
    }

    override fun onAlbumFileNotExist() {
        toast("album image has been deleted")
    }

    override fun onAlbumPreviewSelectEmpty() {
        toast("PreviewActivity,  preview no image")
    }

    override fun onAlbumCheckFileNotExist() {
        toast("check box  image has been deleted")
    }

    override fun onAlbumCropCanceled() {
        toast("cancel crop")
    }

    override fun onAlbumCameraCanceled() {
        toast("cancel camera")
    }

    override fun onAlbumUCropError(data: Throwable?) {
        Log.i("ALBUM", data!!.message)
        toast("crop error:$data")
    }

    override fun onAlbumUCropResources(scannerFile: File) {
        toast("crop file:$scannerFile")
    }

    override fun onAlbumMaxCount() {
        toast("select max count")
    }

    override fun onAlbumActivityBackPressed() {
        toast("AlbumActivity Back")
    }

    override fun onAlbumOpenCameraError() {
        toast("camera error")
    }

    override fun onAlbumEmpty() {
        toast("no image")
    }

    override fun onAlbumNoMore() {
        toast("album no more")
    }

    override fun onVideoPlayError() {
        toast("play video error : checked video app")
    }

    override fun onAlbumResources(list: List<AlbumEntity>) {
        toast("select count :" + list.size)
        if (this.list == null) return
        this.list.clear()
        this.list.addAll(list)
    }
}
