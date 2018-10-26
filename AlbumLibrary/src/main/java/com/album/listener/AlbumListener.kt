package com.album.listener

import com.album.annotation.PermissionsType
import com.album.entity.AlbumEntity
import java.io.File

/**
 * by y on 18/08/2017.
 */

interface AlbumListener {
    fun onAlbumActivityFinish()

    fun onAlbumPermissionsDenied(@PermissionsType type: Int)

    fun onAlbumFragmentNull()

    fun onAlbumPreviewFileNull()

    fun onAlbumFinderNull()

    fun onAlbumBottomPreviewNull()

    fun onAlbumBottomSelectNull()

    fun onAlbumFragmentFileNull()

    fun onAlbumPreviewSelectNull()

    fun onAlbumCheckBoxFileNull()

    fun onAlbumFragmentCropCanceled()

    fun onAlbumFragmentCameraCanceled()

    fun onAlbumFragmentUCropError(data: Throwable?)

    fun onAlbumResources(list: List<AlbumEntity>)

    fun onAlbumUCropResources(scannerFile: File)

    fun onAlbumMaxCount()

    fun onAlbumActivityBackPressed()

    fun onAlbumOpenCameraError()

    fun onAlbumEmpty()

    fun onAlbumNoMore()

    fun onAlbumResultCameraError()

    fun onVideoPlayError()
}
