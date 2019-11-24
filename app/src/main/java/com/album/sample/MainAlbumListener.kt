package com.album.sample

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.album.core.action.OnAlbumListener
import com.album.scan.scan.AlbumEntity
import java.io.File

/**
 * @author y
 */
class MainAlbumListener internal constructor(context: Context, private val list: ArrayList<AlbumEntity>?) : OnAlbumListener {

    override fun onAlbumCheckBox(selectCount: Int, maxCount: Int) {
        toast("onCheckBoxAlbum:$selectCount")
    }

    override fun onAlbumContainerFinish() {
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

    override fun onAlbumPreFileNotExist() {
        toast("preview image has been deleted")
    }

    override fun onAlbumContainerFinderEmpty() {
        toast("folder directory is empty")
    }

    override fun onAlbumPreEmpty() {
        toast("preview no image")
    }

    override fun onAlbumSelectEmpty() {
        toast("select no image")
    }

    override fun onAlbumFileNotExist() {
        toast("album image has been deleted")
    }

    override fun onAlbumContainerPreSelectEmpty() {
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

    override fun onAlbumUCropResources(cropFile: File) {
        toast("crop file:$cropFile")
    }

    override fun onAlbumMaxCount() {
        toast("select max count")
    }

    override fun onAlbumContainerBackPressed() {
        toast("AlbumActivity Back")
    }

    override fun onAlbumOpenCameraError() {
        toast("camera error")
    }

    override fun onAlbumEmpty() {
        toast("no image")
    }

    override fun onAlbumVideoPlayError() {
        toast("play video error : checked video app")
    }

    override fun onAlbumResources(list: List<AlbumEntity>) {
        toast("select count :" + list.size)
        if (this.list == null) return
        this.list.clear()
        this.list.addAll(list)
    }
}
