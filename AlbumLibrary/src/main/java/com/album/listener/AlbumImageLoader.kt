package com.album.listener

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.album.BuildConfig
import com.album.core.scan.AlbumEntity
import com.album.widget.AlbumImageView

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
