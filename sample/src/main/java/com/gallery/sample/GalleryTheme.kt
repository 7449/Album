package com.gallery.sample

import android.app.Activity
import com.gallery.compat.GalleryCompatBundle
import com.gallery.core.GalleryBundle
import com.gallery.ui.material.args.GalleryMaterialBundle
import com.gallery.ui.wechat.extension.colorExpand

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

    fun themeGalleryArgs(
        activity: Activity,
        theme: Theme,
        action: () -> Boolean
    ): GalleryMaterialBundle {
        val toolbarText =
            if (action.invoke()) activity.getString(R.string.gallery_video_title) else "图片选择"
        return when (theme) {
            Theme.DEFAULT -> GalleryMaterialBundle(toolbarText = toolbarText)
            Theme.BLUE -> GalleryMaterialBundle(
                toolbarText = toolbarText,
                statusBarColor = R.color.colorBlue.colorExpand(activity),
                toolbarBackground = R.color.colorBlue.colorExpand(activity),
                bottomViewBackground = R.color.colorBlue.colorExpand(activity),
                preBottomViewBackground = R.color.colorBlue.colorExpand(activity)
            )
            Theme.PINK -> GalleryMaterialBundle(
                toolbarText = toolbarText,
                statusBarColor = R.color.colorPink.colorExpand(activity),
                toolbarBackground = R.color.colorPink.colorExpand(activity),
                bottomViewBackground = R.color.colorPink.colorExpand(activity),
                preBottomViewBackground = R.color.colorPink.colorExpand(activity)
            )
            Theme.BLACK -> GalleryMaterialBundle(
                toolbarText = toolbarText,
                statusBarColor = R.color.colorBlack.colorExpand(activity),
                toolbarBackground = R.color.colorBlack.colorExpand(activity),
                bottomViewBackground = R.color.colorBlack.colorExpand(activity),
                finderItemBackground = R.color.colorBlack.colorExpand(activity),
                finderItemTextColor = R.color.colorWhite.colorExpand(activity),
                preBottomViewBackground = R.color.colorBlack.colorExpand(activity)
            )
            Theme.APP -> GalleryMaterialBundle(
                toolbarText = toolbarText,
                statusBarColor = R.color.colorAccent.colorExpand(activity),
                toolbarBackground = R.color.colorAccent.colorExpand(activity),
                bottomViewBackground = R.color.colorAccent.colorExpand(activity),
                preBottomViewBackground = R.color.colorAccent.colorExpand(activity)
            )
        }
    }

    fun themeGalleryCompat(activity: Activity, theme: Theme): GalleryCompatBundle {
        return when (theme) {
            Theme.DEFAULT -> GalleryCompatBundle()
            Theme.BLUE -> GalleryCompatBundle()
            Theme.PINK -> GalleryCompatBundle()
            Theme.BLACK -> GalleryCompatBundle(
                prevRootBackground = R.color.colorBlack.colorExpand(activity),
                galleryRootBackground = R.color.colorBlack.colorExpand(activity),
            )
            Theme.APP -> GalleryCompatBundle()
        }
    }

}