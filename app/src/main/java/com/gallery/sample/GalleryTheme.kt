@file:Suppress("FunctionName")

package com.gallery.sample

import android.os.Environment
import com.gallery.core.GalleryBundle
import com.gallery.ui.GalleryUiBundle

object GalleryTheme {

    fun NightGalleryBundle(): GalleryBundle {
        return GalleryBundle(
                spanCount = 4,
                checkBoxDrawable = R.drawable.simple_selector_gallery_item_check,
                radio = true,
                cameraPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM/Gallery",
                uCropPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM" + "/" + "uCrop",
                cameraTextColor = R.color.colorGalleryContentViewTipsColorNight,
                cameraDrawable = R.drawable.ic_camera_drawable,
                cameraDrawableColor = R.color.colorGalleryContentViewCameraDrawableColorNight,
                cameraBackgroundColor = R.color.colorGalleryToolbarBackgroundNight,
                rootViewBackground = R.color.colorGalleryContentViewBackgroundNight,
                cameraCrop = true)
    }

    fun NightGalleryUIBundle(): GalleryUiBundle {
        return GalleryUiBundle(
                statusBarColor = R.color.colorGalleryStatusBarColorNight,
                toolbarBackground = R.color.colorGalleryToolbarBackgroundNight,
                toolbarIconColor = R.color.colorGalleryToolbarIconColorNight,
                toolbarTextColor = R.color.colorGalleryToolbarTextColorNight,
                bottomFinderTextBackground = R.color.colorGalleryBottomViewBackgroundNight,
                bottomFinderTextColor = R.color.colorGalleryBottomFinderTextColorNight,
                bottomFinderTextDrawableColor = R.color.colorGalleryBottomFinderTextDrawableColorNight,
                bottomPreViewTextColor = R.color.colorGalleryBottomPreViewTextColorNight,
                bottomSelectTextColor = R.color.colorGalleryBottomSelectTextColorNight,
                listPopupBackground = R.color.colorGalleryListPopupBackgroundNight,
                listPopupItemTextColor = R.color.colorGalleryListPopupItemTextColorNight,
                preBackground = R.color.colorGalleryPreviewBackgroundNight,
                preBottomViewBackground = R.color.colorGalleryPreviewBottomViewBackgroundNight,
                preBottomOkTextColor = R.color.colorGalleryPreviewBottomViewOkColorNight,
                preBottomCountTextColor = R.color.colorGalleryPreviewBottomViewCountColorNight)
    }

}