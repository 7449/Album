package com.gallery.ui.result

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableExpand
import androidx.kotlin.expand.os.orEmptyExpand

class GalleryResultCallback(private val galleryListener: GalleryListener) : ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundleExpand: Bundle = intent.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            UiConfig.RESULT_CODE_CROP -> {
                galleryListener.onGalleryCropResource(bundleExpand.getParcelableExpand(UiConfig.GALLERY_RESULT_CROP))
            }
            UiConfig.RESULT_CODE_SINGLE_DATA -> {
                galleryListener.onGalleryResource(bundleExpand.getParcelableExpand(UiConfig.GALLERY_SINGLE_DATA))
            }
            UiConfig.RESULT_CODE_MULTIPLE_DATA -> {
                galleryListener.onGalleryResources(bundleExpand.getParcelableArrayListExpand(UiConfig.GALLERY_MULTIPLE_DATA))
            }
            UiConfig.RESULT_CODE_TOOLBAR_BACK, Activity.RESULT_CANCELED -> {
                galleryListener.onGalleryCancel()
            }
        }
    }
}