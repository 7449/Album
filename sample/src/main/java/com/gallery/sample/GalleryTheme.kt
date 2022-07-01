package com.gallery.sample

import android.app.Activity
import com.gallery.core.GalleryBundle
import com.gallery.ui.material.args.MaterialGalleryBundle
import com.gallery.ui.wechat.extension.colorExpand

object GalleryTheme {

    fun themeGallery(activity: Activity, theme: Theme): GalleryBundle {
        when (theme) {
            Theme.DEFAULT -> {
                return GalleryBundle(checkBoxDrawable = R.drawable.simple_default_selector_gallery_item_check)
            }
            Theme.BLUE -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                        cameraBackgroundColor = R.color.colorBlue.colorExpand(activity),
                        checkBoxDrawable = R.drawable.simple_blue_selector_gallery_item_check
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
                )
            }
            Theme.APP -> {
                return GalleryBundle(
                        checkBoxDrawable = R.drawable.simple_app_selector_gallery_item_check,
                        cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                        cameraBackgroundColor = R.color.colorAccent.colorExpand(activity)
                )
            }
        }
    }

    fun themeGalleryArgs(activity: Activity, theme: Theme): MaterialGalleryBundle {
        return when (theme) {
            Theme.DEFAULT -> MaterialGalleryBundle()
            Theme.BLUE -> MaterialGalleryBundle(
                    statusBarColor = R.color.colorBlue.colorExpand(activity),
                    toolbarBackground = R.color.colorBlue.colorExpand(activity),
                    bottomViewBackground = R.color.colorBlue.colorExpand(activity),
                    preBottomViewBackground = R.color.colorBlue.colorExpand(activity)
            )
            Theme.PINK -> MaterialGalleryBundle(
                    statusBarColor = R.color.colorPink.colorExpand(activity),
                    toolbarBackground = R.color.colorPink.colorExpand(activity),
                    bottomViewBackground = R.color.colorPink.colorExpand(activity),
                    preBottomViewBackground = R.color.colorPink.colorExpand(activity)
            )
            Theme.BLACK -> MaterialGalleryBundle(
                    statusBarColor = R.color.colorBlack.colorExpand(activity),
                    toolbarBackground = R.color.colorBlack.colorExpand(activity),
                    bottomViewBackground = R.color.colorBlack.colorExpand(activity),
                    finderItemBackground = R.color.colorBlack.colorExpand(activity),
                    finderItemTextColor = R.color.colorWhite.colorExpand(activity),
                    preBottomViewBackground = R.color.colorBlack.colorExpand(activity),
                    prevRootBackground = R.color.colorBlack.colorExpand(activity),
                    galleryRootBackground = R.color.colorBlack.colorExpand(activity),
            )
            Theme.APP -> MaterialGalleryBundle(
                    statusBarColor = R.color.colorAccent.colorExpand(activity),
                    toolbarBackground = R.color.colorAccent.colorExpand(activity),
                    bottomViewBackground = R.color.colorAccent.colorExpand(activity),
                    preBottomViewBackground = R.color.colorAccent.colorExpand(activity)
            )
        }
    }

}