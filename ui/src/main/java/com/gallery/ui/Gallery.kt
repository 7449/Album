package com.gallery.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGallery
import com.gallery.ui.activity.GalleryActivity

/**
 * @author y
 * @create 2019/3/1
 */
object Gallery {

    fun open(context: Context) = open(context, GalleryBundle())

    fun open(context: Context, galleryBundle: GalleryBundle) = open(context, galleryBundle, GalleryUiBundle())

    fun open(context: Context, galleryBundle: GalleryBundle, uiBundle: GalleryUiBundle) = open(context, galleryBundle, uiBundle, GalleryActivity::class.java)

    fun open(context: Context, galleryBundle: GalleryBundle, uiBundle: GalleryUiBundle, clazz: Class<out GalleryActivity>) {
        context.startActivity(Intent(context, clazz).apply {
            putExtras(Bundle().apply {
                putParcelable(IGallery.GALLERY_START_CONFIG, galleryBundle)
                putParcelable(UIResult.UI_CONFIG, uiBundle)
            })
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

}



