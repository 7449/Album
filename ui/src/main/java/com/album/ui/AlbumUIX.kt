package com.album.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.album.ui.activity.AlbumActivity
import com.gallery.core.GalleryBundle
import com.gallery.core.Gallery
import com.gallery.core.GalleryConst

/**
 * @author y
 * @create 2019/3/1
 */

fun Gallery.ui(context: Context) = ui(context, GalleryBundle())

fun Gallery.ui(context: Context, albumBundle: GalleryBundle) = ui(context, albumBundle, AlbumUiBundle())

fun Gallery.ui(context: Context, albumBundle: GalleryBundle, uiBundle: AlbumUiBundle) = apply {
    context.startActivity(Intent(context, AlbumActivity::class.java).apply {
        putExtras(Bundle().apply {
            putParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS, albumBundle)
            putParcelable(GalleryConst.EXTRA_GALLERY_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

