@file:Suppress("FunctionName")

package com.gallery.sample

import android.app.Activity
import android.os.Bundle
import com.gallery.core.GalleryBundle
import com.gallery.ui.GalleryUiBundle
import com.yalantis.ucrop.UCrop.Options.*

object GalleryTheme {

    fun themeGallery(activity: Activity, theme: Theme): GalleryBundle {
        when (theme) {
            Theme.DEFAULT -> {
                return GalleryBundle()
            }
            Theme.BLUE -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.color(activity),
                        cameraBackgroundColor = R.color.colorBlue.color(activity)
                )
            }
            Theme.PINK -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.color(activity),
                        cameraBackgroundColor = R.color.colorPink.color(activity)
                )
            }
            Theme.BLACK -> {
                return GalleryBundle(
                        cameraDrawableColor = R.color.colorGray.color(activity),
                        cameraBackgroundColor = R.color.colorBlack.color(activity),
                        rootViewBackground = R.color.colorBlack.color(activity),
                        prevPhotoBackgroundColor = R.color.colorBlack.color(activity)
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
                        statusBarColor = R.color.colorBlue.color(activity),
                        toolbarBackground = R.color.colorBlue.color(activity),
                        bottomViewBackground = R.color.colorBlue.color(activity),
                        preBottomViewBackground = R.color.colorBlue.color(activity)
                )
            }
            Theme.PINK -> {
                return GalleryUiBundle(
                        statusBarColor = R.color.colorPink.color(activity),
                        toolbarBackground = R.color.colorPink.color(activity),
                        bottomViewBackground = R.color.colorPink.color(activity),
                        preBottomViewBackground = R.color.colorPink.color(activity)
                )
            }
            Theme.BLACK -> {
                return GalleryUiBundle(
                        statusBarColor = R.color.colorBlack.color(activity),
                        toolbarBackground = R.color.colorBlack.color(activity),
                        bottomViewBackground = R.color.colorBlack.color(activity),
                        finderItemBackground = R.color.colorBlack.color(activity),
                        finderItemTextColor = R.color.colorWhite.color(activity),
                        preBottomViewBackground = R.color.colorBlack.color(activity)
                )
            }
        }
    }

    fun cropThemeGallery(activity: Activity): GalleryBundle {
        return GalleryBundle(
                radio = true,
                cameraCrop = true,
                cameraDrawableColor = R.color.colorGray.color(activity),
                cameraBackgroundColor = R.color.colorBlack.color(activity),
                rootViewBackground = R.color.colorBlack.color(activity)
        )
    }

    fun cropThemeGalleryUi(activity: Activity): GalleryUiBundle {
        return GalleryUiBundle(
                statusBarColor = R.color.colorBlack.color(activity),
                toolbarBackground = R.color.colorBlack.color(activity),
                bottomViewBackground = R.color.colorBlack.color(activity),
                finderItemBackground = R.color.colorBlack.color(activity),
                finderItemTextColor = R.color.colorWhite.color(activity),
                uCropBundle = Bundle().apply {
                    putString(EXTRA_UCROP_TITLE_TEXT_TOOLBAR, "UCrop")
                    putInt(EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, R.color.colorWhite.color(activity))
                    putInt(EXTRA_TOOL_BAR_COLOR, R.color.colorBlack.color(activity))
                    putInt(EXTRA_UCROP_COLOR_WIDGET_ACTIVE, R.color.colorBlack.color(activity))
                    putInt(EXTRA_STATUS_BAR_COLOR, R.color.colorBlack.color(activity))
                    putBoolean(EXTRA_HIDE_BOTTOM_CONTROLS, true)
                }
        )
    }
}