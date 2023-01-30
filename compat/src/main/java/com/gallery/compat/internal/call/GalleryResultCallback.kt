package com.gallery.compat.internal.call

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.gallery.compat.GalleryConfig
import com.gallery.compat.extensions.parcelable
import com.gallery.compat.extensions.parcelableArrayList
import com.gallery.core.extensions.orEmpty

open class GalleryResultCallback(private val galleryListener: GalleryListener) :
    ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundle: Bundle = intent.data?.extras.orEmpty()
        when (intent.resultCode) {
            GalleryConfig.Crop.RESULT_CODE_CROP -> onCropResult(bundle)
            GalleryConfig.RESULT_CODE_SINGLE_DATA -> onSingleDataResult(bundle)
            GalleryConfig.RESULT_CODE_MULTIPLE_DATA -> onMultipleDataResult(bundle)
            GalleryConfig.RESULT_CODE_TOOLBAR_BACK,
            Activity.RESULT_CANCELED -> onCancelResult(bundle)
        }
    }

    protected open fun onCropResult(bundle: Bundle) {
        galleryListener.onGalleryCropResource(bundle.parcelable(GalleryConfig.Crop.GALLERY_RESULT_CROP))
    }

    protected open fun onSingleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResource(bundle.parcelable(GalleryConfig.GALLERY_SINGLE_DATA))
    }

    protected open fun onMultipleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResources(bundle.parcelableArrayList(GalleryConfig.GALLERY_MULTIPLE_DATA))
    }

    protected open fun onCancelResult(bundle: Bundle) {
        galleryListener.onGalleryCancel()
    }

}