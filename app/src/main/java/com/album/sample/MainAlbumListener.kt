package com.album.sample

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.album.annotation.PermissionsType
import com.album.entity.AlbumEntity
import com.album.listener.AlbumListener
import java.io.File

/**
 * @author y
 */
class MainAlbumListener internal constructor(context: Context, private val list: ArrayList<AlbumEntity>?) : AlbumListener {
    override fun onAlbumResultCameraError() {
        toast("onAlbumResultCameraError")
    }

    private val context: Context = context.applicationContext

    private fun toast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onAlbumActivityFinish() {
        toast("album activity finish")
    }

    override fun onAlbumPermissionsDenied(@PermissionsType type: Int) {
        toast("permissions error")
    }

    override fun onAlbumFragmentNull() {
        toast("album fragment null")
    }

    override fun onAlbumPreviewFileNull() {
        toast("preview image has been deleted")
    }

    override fun onAlbumFinderNull() {
        toast("folder directory is empty")
    }

    override fun onAlbumBottomPreviewNull() {
        toast("preview no image")
    }

    override fun onAlbumBottomSelectNull() {
        toast("select no image")
    }

    override fun onAlbumFragmentFileNull() {
        toast("album image has been deleted")
    }

    override fun onAlbumPreviewSelectNull() {
        toast("PreviewActivity,  preview no image")
    }

    override fun onAlbumCheckBoxFileNull() {
        toast("check box  image has been deleted")
    }

    override fun onAlbumFragmentCropCanceled() {
        toast("cancel crop")
    }

    override fun onAlbumFragmentCameraCanceled() {
        toast("cancel camera")
    }

    override fun onAlbumFragmentUCropError(data: Throwable?) {
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
