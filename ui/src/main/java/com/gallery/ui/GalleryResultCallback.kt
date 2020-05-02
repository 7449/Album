package com.gallery.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableExpand
import androidx.kotlin.expand.os.orEmptyExpand

class GalleryResultCallback(
        private val fragmentActivity: FragmentActivity,
        private val galleryListener: GalleryListener
) : ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundleExpand: Bundle = intent.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            UIResult.GALLERY_RESULT_CROP -> {
                galleryListener.onGalleryCropResource(fragmentActivity, bundleExpand.getParcelableExpand(UIResult.GALLERY_RESULT_URI))
            }
            UIResult.GALLERY_RESULT_RESOURCE -> {
                galleryListener.onGalleryResource(fragmentActivity, bundleExpand.getParcelableExpand(UIResult.GALLERY_RESULT_ENTITY))
            }
            UIResult.GALLERY_RESULT_RESOURCES -> {
                galleryListener.onGalleryResources(fragmentActivity, bundleExpand.getParcelableArrayListExpand(UIResult.GALLERY_RESULT_ENTITIES))
            }
            UIResult.GALLERY_FINISH_RESULT_CODE -> {
                galleryListener.onGalleryCancel(fragmentActivity)
            }
            Activity.RESULT_CANCELED -> {
                galleryListener.onGalleryCancel(fragmentActivity)
            }
        }
    }
}