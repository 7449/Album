package com.album.simple

import com.album.core.scan.AlbumEntity
import com.album.listener.OnAlbumListener
import java.io.File

open class SimpleOnAlbumListener : OnAlbumListener {
    override fun onAlbumCameraSuccessCanceled() {}
    override fun onAlbumContainerFinish() {}
    override fun onAlbumContainerBackPressed() {}
    override fun onAlbumContainerFinderEmpty() {}
    override fun onAlbumContainerPreviewSelectEmpty() {}
    override fun onAlbumResources(list: List<AlbumEntity>) {}
    override fun onAlbumResultCameraError() {}
    override fun onAlbumPermissionsDenied(type: Int) {}
    override fun onAlbumPreFileNotExist() {}
    override fun onAlbumPreviewEmpty() {}
    override fun onAlbumSelectEmpty() {}
    override fun onAlbumFileNotExist() {}
    override fun onAlbumCheckFileNotExist() {}
    override fun onAlbumCropCanceled() {}
    override fun onAlbumCameraCanceled() {}
    override fun onAlbumUCropError(data: Throwable?) {}
    override fun onAlbumUCropResources(cropFile: File) {}
    override fun onAlbumMaxCount() {}
    override fun onAlbumOpenCameraError() {}
    override fun onAlbumEmpty() {}
    override fun onAlbumVideoPlayError() {}
    override fun onAlbumCheckBox(count: Int, maxCount: Int) {}
}