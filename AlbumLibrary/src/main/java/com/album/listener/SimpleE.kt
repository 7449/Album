package com.album.listener

import android.view.View
import android.widget.FrameLayout
import com.album.R
import com.album.core.scan.AlbumEntity
import com.album.widget.AlbumImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ortiz.touchview.TouchImageView
import java.io.File

/**
 * @author y
 * @create 2019/2/27
 */


/**
 * 内置一个图片加载器,需要依赖Glide
 */
class SimpleAlbumImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height)).into(albumImageView)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        Glide.with(container.context).load(finderEntity.path).apply(requestOptions).into(albumImageView)
        return albumImageView
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = TouchImageView(container.context)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(albumImageView)
        return albumImageView
    }
}

/**
 * [OnAlbumListener]
 */
open class SimpleOnAlbumListener : OnAlbumListener {
    override fun onAlbumContainerFinish() {}
    override fun onAlbumContainerBackPressed() {}
    override fun onAlbumContainerFinderEmpty() {}
    override fun onAlbumContainerPreviewSelectEmpty() {}
    override fun onAlbumResources(list: List<AlbumEntity>) {}
    override fun onAlbumResultCameraError() {}
    override fun onAlbumPermissionsDenied(type: Int) {}
    override fun onAlbumPreviewFileNotExist() {}
    override fun onAlbumPreviewEmpty() {}
    override fun onAlbumSelectEmpty() {}
    override fun onAlbumFileNotExist() {}
    override fun onAlbumCheckFileNotExist() {}
    override fun onAlbumCropCanceled() {}
    override fun onAlbumCameraCanceled() {}
    override fun onAlbumUCropError(data: Throwable?) {}
    override fun onAlbumUCropResources(scannerFile: File) {}
    override fun onAlbumMaxCount() {}
    override fun onAlbumOpenCameraError() {}
    override fun onAlbumEmpty() {}
    override fun onAlbumNoMore() {}
    override fun onVideoPlayError() {}
    override fun onCheckBoxAlbum(count: Int, maxCount: Int) {}
}