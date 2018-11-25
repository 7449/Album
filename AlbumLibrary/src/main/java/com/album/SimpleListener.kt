package com.album

import android.view.View
import android.widget.FrameLayout
import com.album.ui.AlbumImageView
import com.album.ui.widget.TouchImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

class SimpleGlideAlbumImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height)).into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: FinderEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Glide.with(container.context).load(finderEntity.thumbnailsPath).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = TouchImageView(container.context)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(albumImageView)
        return albumImageView
    }
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
