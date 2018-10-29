package com.album

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

class SimpleGlideAlbumImageLoader : AlbumImageLoader {
    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher).centerCrop()
    override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {
        Glide.with(view.context).load(albumEntity.path).apply(requestOptions.override(width, height)).into(view)
    }

    override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {
        Glide.with(view.context).load(finderEntity.thumbnailsPath).apply(requestOptions).into(view)
    }

    override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {
        Glide.with(view.context).load(albumEntity.path).apply(requestOptions).into(view)
    }

    override fun frescoView(context: Context, @FrescoType type: Int): ImageView? = null
}

open class SimpleAlbumListener : AlbumListener {
    override fun onAlbumActivityFinish() {}
    override fun onAlbumResultCameraError() {}
    override fun onAlbumPermissionsDenied(type: Int) {}
    override fun onAlbumPreviewFileNotExist() {}
    override fun onAlbumFinderEmpty() {}
    override fun onAlbumPreviewEmpty() {}
    override fun onAlbumSelectEmpty() {}
    override fun onAlbumFileNotExist() {}
    override fun onAlbumPreviewSelectEmpty() {}
    override fun onAlbumCheckFileNotExist() {}
    override fun onAlbumCropCanceled() {}
    override fun onAlbumCameraCanceled() {}
    override fun onAlbumUCropError(data: Throwable?) {}
    override fun onAlbumResources(list: List<AlbumEntity>) {}
    override fun onAlbumUCropResources(scannerFile: File) {}
    override fun onAlbumMaxCount() {}
    override fun onAlbumActivityBackPressed() {}
    override fun onAlbumOpenCameraError() {}
    override fun onAlbumEmpty() {}
    override fun onAlbumNoMore() {}
    override fun onVideoPlayError() {}
}
