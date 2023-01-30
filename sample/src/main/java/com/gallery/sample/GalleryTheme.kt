package com.gallery.sample

import android.app.Activity
import com.gallery.core.CameraConfig
import com.gallery.core.GalleryConfigs
import com.gallery.ui.material.args.MaterialGalleryBundle
import com.gallery.ui.wechat.extension.colorExpand

object GalleryTheme {

    fun themeGallery(activity: Activity, theme: Theme): GalleryConfigs {
        when (theme) {
            Theme.DEFAULT -> {
                return GalleryConfigs(cameraConfig = CameraConfig(selectIcon = R.drawable.simple_default_selector_gallery_item_check))
            }

            Theme.BLUE -> {
                return GalleryConfigs(
                    cameraConfig = CameraConfig(
                        bg = R.color.colorBlue.colorExpand(activity),
                        iconColor = R.color.colorGray.colorExpand(activity),
                        selectIcon = R.drawable.simple_blue_selector_gallery_item_check
                    ),
                )
            }

            Theme.PINK -> {
                return GalleryConfigs(
                    cameraConfig = CameraConfig(
                        bg = R.color.colorPink.colorExpand(activity),
                        iconColor = R.color.colorGray.colorExpand(activity),
                    ),
                )
            }

            Theme.BLACK -> {
                return GalleryConfigs(
                    cameraConfig = CameraConfig(
                        bg = R.color.colorBlack.colorExpand(activity),
                        iconColor = R.color.colorGray.colorExpand(activity),
                    ),
                )
            }

            Theme.APP -> {
                return GalleryConfigs(
                    cameraConfig = CameraConfig(
                        bg = R.color.colorAccent.colorExpand(activity),
                        iconColor = R.color.colorGray.colorExpand(activity),
                        selectIcon = R.drawable.simple_app_selector_gallery_item_check,
                    ),
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