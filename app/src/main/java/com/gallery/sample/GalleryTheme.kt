package com.gallery.sample

import android.app.Activity
import android.os.Bundle
import androidx.kotlin.expand.graphics.colorExpand
import com.gallery.core.GalleryBundle
import com.gallery.sample.enums.Theme
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.gallery.ui.crop.CropType
import com.theartofdev.edmodo.cropper.CropImageOptions
import com.yalantis.ucrop.UCrop

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

    fun cropThemeGallery(activity: Activity): GalleryBundle {
        return GalleryBundle(
                radio = true,
                cameraCrop = true,
                cameraDrawableColor = R.color.colorGray.colorExpand(activity),
                cameraBackgroundColor = R.color.colorBlack.colorExpand(activity),
                galleryRootBackground = R.color.colorBlack.colorExpand(activity)
        )
    }

    fun cropThemeGalleryUi(activity: Activity, cropType: CropType): GalleryUiBundle {
        return GalleryUiBundle(
                cropType = cropType,
                statusBarColor = R.color.colorBlack.colorExpand(activity),
                toolbarBackground = R.color.colorBlack.colorExpand(activity),
                bottomViewBackground = R.color.colorBlack.colorExpand(activity),
                finderItemBackground = R.color.colorBlack.colorExpand(activity),
                finderItemTextColor = R.color.colorWhite.colorExpand(activity),
                args = Bundle().apply {
                    if (cropType == CropType.CROPPER) {
                        putParcelable(UIResult.CROP_ARGS, CropImageOptions().apply {
                            activityTitle = "Cropper"
                        })
                    } else {
                        putBundle(UIResult.CROP_ARGS, Bundle().apply {
                            putString(UCrop.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR, "UCrop")
                            putInt(UCrop.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, R.color.colorWhite.colorExpand(activity))
                            putInt(UCrop.Options.EXTRA_TOOL_BAR_COLOR, R.color.colorBlack.colorExpand(activity))
                            putInt(UCrop.Options.EXTRA_STATUS_BAR_COLOR, R.color.colorBlack.colorExpand(activity))
                            putBoolean(UCrop.Options.EXTRA_HIDE_BOTTOM_CONTROLS, true)
                        })
                    }
                }
        )
    }
}