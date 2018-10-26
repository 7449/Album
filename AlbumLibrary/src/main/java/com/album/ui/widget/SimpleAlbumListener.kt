package com.album.ui.widget

import com.album.entity.AlbumEntity
import com.album.listener.AlbumListener
import java.io.File

/**
 * by y on 18/08/2017.
 */

open class SimpleAlbumListener : AlbumListener {

    override fun onAlbumResultCameraError() {
    }

    override fun onAlbumActivityFinish() {

    }

    override fun onAlbumPermissionsDenied(type: Int) {

    }

    override fun onAlbumFragmentNull() {

    }

    override fun onAlbumPreviewFileNull() {

    }

    override fun onAlbumFinderNull() {

    }

    override fun onAlbumBottomPreviewNull() {

    }

    override fun onAlbumBottomSelectNull() {

    }

    override fun onAlbumFragmentFileNull() {

    }

    override fun onAlbumPreviewSelectNull() {

    }

    override fun onAlbumCheckBoxFileNull() {

    }

    override fun onAlbumFragmentCropCanceled() {

    }

    override fun onAlbumFragmentCameraCanceled() {

    }

    override fun onAlbumFragmentUCropError(data: Throwable?) {

    }

    override fun onAlbumResources(list: List<AlbumEntity>) {

    }

    override fun onAlbumUCropResources(scannerFile: File) {

    }

    override fun onAlbumMaxCount() {

    }

    override fun onAlbumActivityBackPressed() {

    }

    override fun onAlbumOpenCameraError() {

    }

    override fun onAlbumEmpty() {

    }

    override fun onAlbumNoMore() {

    }

    override fun onVideoPlayError() {
    }
}
