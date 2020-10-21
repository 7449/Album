package com.gallery.sample

import android.app.Activity
import androidx.kotlin.expand.graphics.colorExpand
import com.gallery.core.GalleryBundle
import com.gallery.ui.GalleryUiBundle

object GalleryTheme {

    fun themeGallery(activity: Activity, theme: Theme): GalleryBundle {
        when (theme) {
            Theme.DEFAULT -> {
                return GalleryBundle(checkBoxDrawable = R.drawable.default_selector_gallery_item_check)
            }
            Theme.BLUE -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                        cameraBackgroundColor = R.color.colorBlue.colorExpand(activity),
                        checkBoxDrawable = R.drawable.blue_selector_gallery_item_check
                )
            }
            Theme.PINK -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                        cameraBackgroundColor = R.color.colorPink.colorExpand(activity)
                )
            }
            Theme.BLACK -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                        cameraBackgroundColor = R.color.colorBlack.colorExpand(activity),
                        galleryRootBackground = R.color.colorBlack.colorExpand(activity),
                        prevPhotoBackgroundColor = R.color.colorBlack.colorExpand(activity)
                )
            }
            Theme.APP -> {
                return GalleryBundle(
                        checkBoxDrawable = R.drawable.app_selector_gallery_item_check,
                        cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                        cameraBackgroundColor = R.color.colorAccent.colorExpand(activity)
                )
            }
        }
    }

    fun themeGalleryUi(activity: Activity, theme: Theme): GalleryUiBundle {
        when (theme) {
            Theme.DEFAULT -> {
                return GalleryUiBundle()
            }
            Theme.BLUE -> {
                return GalleryUiBundle(
                        statusBarColor = R.color.colorBlue.colorExpand(activity),
                        toolbarBackground = R.color.colorBlue.colorExpand(activity),
                        bottomViewBackground = R.color.colorBlue.colorExpand(activity),
                        preBottomViewBackground = R.color.colorBlue.colorExpand(activity)
                )
            }
            Theme.PINK -> {
                return GalleryUiBundle(
                        statusBarColor = R.color.colorPink.colorExpand(activity),
                        toolbarBackground = R.color.colorPink.colorExpand(activity),
                        bottomViewBackground = R.color.colorPink.colorExpand(activity),
                        preBottomViewBackground = R.color.colorPink.colorExpand(activity)
                )
            }
            Theme.BLACK -> {
                return GalleryUiBundle(
                        statusBarColor = R.color.colorBlack.colorExpand(activity),
                        toolbarBackground = R.color.colorBlack.colorExpand(activity),
                        bottomViewBackground = R.color.colorBlack.colorExpand(activity),
                        finderItemBackground = R.color.colorBlack.colorExpand(activity),
                        finderItemTextColor = R.color.colorWhite.colorExpand(activity),
                        preBottomViewBackground = R.color.colorBlack.colorExpand(activity)
                )
            }
            Theme.APP -> {
                return GalleryUiBundle(
                        statusBarColor = R.color.colorAccent.colorExpand(activity),
                        toolbarBackground = R.color.colorAccent.colorExpand(activity),
                        bottomViewBackground = R.color.colorAccent.colorExpand(activity),
                        preBottomViewBackground = R.color.colorAccent.colorExpand(activity)
                )
            }
        }
    }
}