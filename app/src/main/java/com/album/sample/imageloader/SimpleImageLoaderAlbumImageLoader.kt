package com.album.sample.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.album.AlbumEntity
import com.album.AlbumImageLoader
import com.album.FinderEntity
import com.album.FrescoType
import com.album.sample.R
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware

/**
 * by y on 20/08/2017.
 */

class SimpleImageLoaderAlbumImageLoader : AlbumImageLoader {

    private var displayImageOptions: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ic_launcher)
            .showImageForEmptyUri(R.drawable.ic_launcher)
            .showImageOnFail(R.drawable.ic_launcher)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build()

    override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoader.getInstance().displayImage("file:///" + albumEntity.path, view, displayImageOptions, null)
    }


    override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoader.getInstance().displayImage("file:///" + finderEntity.thumbnailsPath, ImageViewAware(view), displayImageOptions, null, null, null)
    }

    override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageLoader.getInstance().displayImage("file:///" + albumEntity.path, ImageViewAware(view), displayImageOptions, null, null, null)
    }

    override fun frescoView(context: Context, @FrescoType type: Int): ImageView? {
        return null
    }
}
