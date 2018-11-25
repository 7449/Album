package com.album.sample.imageloader

import android.graphics.Bitmap
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity
import com.album.sample.R
import com.album.ui.AlbumImageView
import com.album.ui.widget.TouchImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware

/**
 * by y on 20/08/2017.
 */

class SimpleImageLoaderAlbumImageLoader : AlbumImageLoader {

    private var displayImageOptions: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_album_default_loading)
            .showImageForEmptyUri(R.drawable.ic_album_default_loading)
            .showImageOnFail(R.drawable.ic_album_default_loading)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()


    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        albumImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoader.getInstance().displayImage("file:///" + albumEntity.path, albumImageView, displayImageOptions, null)
        return albumImageView
    }

    override fun displayAlbumThumbnails(finderEntity: FinderEntity, container: FrameLayout): View {
        val albumImageView = AlbumImageView(container.context)
        albumImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoader.getInstance().displayImage("file:///" + finderEntity.thumbnailsPath, ImageViewAware(albumImageView), displayImageOptions, null, null, null)
        return albumImageView
    }

    override fun displayPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val albumImageView = TouchImageView(container.context)
        albumImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoader.getInstance().displayImage("file:///" + albumEntity.path, ImageViewAware(albumImageView), displayImageOptions, null, null, null)
        return albumImageView
    }
}
