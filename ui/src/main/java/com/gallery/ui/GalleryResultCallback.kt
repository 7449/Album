package com.gallery.ui

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
            UIResult.GALLERY_CROP_RESULT_CODE -> {
                galleryListener.onGalleryCropResource(bundleExpand.getParcelableExpand(UIResult.GALLERY_RESULT_CROP))
            }
            UIResult.GALLERY_SINGLE_RESULT_CODE -> {
                galleryListener.onGalleryResource(bundleExpand.getParcelableExpand(UIResult.GALLERY_SINGLE_DATA))
            }
            UIResult.GALLERY_MULTIPLE_RESULT_CODE -> {
                galleryListener.onGalleryResources(bundleExpand.getParcelableArrayListExpand(UIResult.GALLERY_MULTIPLE_DATA))
            }
            UIResult.UI_TOOLBAR_BACK_RESULT_CODE -> {
                galleryListener.onGalleryCancel()
            }
            Activity.RESULT_CANCELED -> {
                galleryListener.onGalleryCancel()
            }
        }
    }
}