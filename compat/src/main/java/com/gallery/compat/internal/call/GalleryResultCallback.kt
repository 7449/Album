package com.gallery.compat.internal.call

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.gallery.compat.GalleryConfig
import com.gallery.compat.extensions.parcelableArrayListExpand
import com.gallery.compat.extensions.parcelableExpand
import com.gallery.core.extensions.orEmptyExpand

open class GalleryResultCallback(private val galleryListener: GalleryListener) :
    ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundleExpand: Bundle = intent.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            GalleryConfig.Crop.RESULT_CODE_CROP -> onCropResult(bundleExpand)
            GalleryConfig.RESULT_CODE_SINGLE_DATA -> onSingleDataResult(bundleExpand)
            GalleryConfig.RESULT_CODE_MULTIPLE_DATA -> onMultipleDataResult(bundleExpand)
            GalleryConfig.RESULT_CODE_TOOLBAR_BACK,
            Activity.RESULT_CANCELED -> onCancelResult(bundleExpand)
        }
    }

    protected open fun onCropResult(bundle: Bundle) {
        galleryListener.onGalleryCropResource(bundle.parcelableExpand(GalleryConfig.Crop.GALLERY_RESULT_CROP))
    }

    protected open fun onSingleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResource(bundle.parcelableExpand(GalleryConfig.GALLERY_SINGLE_DATA))
    }

    protected open fun onMultipleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResources(bundle.parcelableArrayListExpand(GalleryConfig.GALLERY_MULTIPLE_DATA))
    }

    protected open fun onCancelResult(bundle: Bundle) {
        galleryListener.onGalleryCancel()
    }

}