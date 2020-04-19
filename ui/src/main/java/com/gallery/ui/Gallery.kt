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

    fun ui(context: Context) = ui(context, GalleryBundle())

    fun ui(context: Context, galleryBundle: GalleryBundle) = ui(context, galleryBundle, GalleryUiBundle())

    fun ui(context: Context, galleryBundle: GalleryBundle, uiBundle: GalleryUiBundle) = apply {
        context.startActivity(Intent(context, GalleryActivity::class.java).apply {
            putExtras(Bundle().apply {
                putParcelable(IGallery.GALLERY_START_CONFIG, galleryBundle)
                putParcelable(UIResult.UI_CONFIG, uiBundle)
            })
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

}



