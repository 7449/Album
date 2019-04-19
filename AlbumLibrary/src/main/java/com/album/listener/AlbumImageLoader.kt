@file:Suppress("FunctionName")

package com.album.listener

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.album.BuildConfig
import com.album.R
import com.album.core.scan.AlbumEntity
import com.album.widget.AlbumImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumImageLoader {
    /**
     * 首页图片加载
     */
    fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View

    /**
     * 目录图片加载
     */
    fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View

    /**
     * 预览页图片加载
     */
    fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View
}

class SimpleAlbumImageLoaderKt {

    private lateinit var displayAlbum: ((width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout) -> View)
    private lateinit var displayAlbumThumbnails: ((finderEntity: AlbumEntity, container: FrameLayout) -> View)
    private lateinit var displayAlbumPreview: ((albumEntity: AlbumEntity, container: FrameLayout) -> View)

    fun displayAlbum(displayAlbum: (width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout) -> View) {
        this.displayAlbum = displayAlbum
    }

    fun displayAlbumThumbnails(displayAlbumThumbnails: (finderEntity: AlbumEntity, container: FrameLayout) -> View) {
        this.displayAlbumThumbnails = displayAlbumThumbnails
    }

    fun displayAlbumPreview(displayAlbumPreview: (albumEntity: AlbumEntity, container: FrameLayout) -> View) {
        this.displayAlbumPreview = displayAlbumPreview
    }

    internal fun build(): AlbumImageLoader {
        return object : AlbumImageLoader {
            override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View = displayAlbum.invoke(width, height, albumEntity, container)
            override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View = displayAlbumThumbnails.invoke(finderEntity, container)
            override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View = displayAlbumPreview.invoke(albumEntity, container)
        }
    }
}

class SimpleAlbumImageLoader : AlbumImageLoader {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_album_default_loading).error(R.drawable.ic_album_default_loading).centerCrop()

    override fun displayAlbum(width: Int, height: Int, albumEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.ImageView()
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(width, height)).into(imageView)
        return imageView
    }

    override fun displayAlbumThumbnails(finderEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.ImageView()
        Glide.with(container.context).load(finderEntity.path).apply(requestOptions).into(imageView)
        return imageView
    }

    override fun displayAlbumPreview(albumEntity: AlbumEntity, container: FrameLayout): View {
        val imageView = container.PhotoView()
        Glide.with(container.context).load(albumEntity.path).apply(requestOptions.override(albumEntity.width, albumEntity.height)).into(imageView)
        return imageView
    }
}

inline fun ViewGroup.addChildView(childView: View?) {
    if (indexOfChild(childView) == -1 && childView != null) {
        addView(childView)
    } else {
        if (BuildConfig.DEBUG) {
            Log.d("Album", "jump over addView:${childView?.javaClass?.simpleName
                    ?: "childView == null"}")
        }
    }
}

inline fun ViewGroup.addChildView(childView: View?, layoutParams: ViewGroup.LayoutParams) {
    if (indexOfChild(childView) == -1 && childView != null) {
        addView(childView, layoutParams)
    } else {
        if (BuildConfig.DEBUG) {
            Log.d("Album", "jump over addView:${childView?.javaClass?.simpleName
                    ?: "childView == null"}")
        }
    }
}

fun ViewGroup.ImageView() = if (childCount > 0) getChildAt(0) as AlbumImageView else AlbumImageView(context)

fun ViewGroup.PhotoView() = if (childCount > 0) getChildAt(0) as PhotoView else PhotoView(context)


