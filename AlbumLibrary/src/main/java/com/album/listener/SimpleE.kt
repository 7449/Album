package com.album.listener

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import com.album.R
import com.album.core.scan.AlbumEntity
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

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View? {
        val imageView = AlbumImageView(container)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height)).into(imageView)
        return DisplayView(container, imageView)
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View? {
        val imageView = AlbumImageView(container)
        Glide.with(container.context).load(finderEntity.path).apply(requestOptions).into(imageView)
        return DisplayView(container, imageView)
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View? {
        val imageView = AlbumImageView(container)
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions).into(imageView)
        return DisplayView(container, imageView)
    }
}

fun AlbumImageLoader.DisplayView(container: FrameLayout, newView: View): View? {
    return if (container.childCount > 0) null else newView
}

fun AlbumImageLoader.AlbumImageView(container: FrameLayout): AppCompatImageView {
    return if (container.childCount > 0) {
        container.getChildAt(0) as AppCompatImageView
    } else {
        AppCompatImageView(container.context)
    }
}

fun AlbumImageLoader.AlbumTouchImageView(container: FrameLayout): TouchImageView {
    return if (container.childCount > 0) {
        container.getChildAt(0) as TouchImageView
    } else {
        TouchImageView(container.context)
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
    override fun onAlbumUCropResources(cropFile: File) {}
    override fun onAlbumMaxCount() {}
    override fun onAlbumOpenCameraError() {}
    override fun onAlbumEmpty() {}
    override fun onAlbumNoMore() {}
    override fun onVideoPlayError() {}
    override fun onCheckBoxAlbum(count: Int, maxCount: Int) {}
}