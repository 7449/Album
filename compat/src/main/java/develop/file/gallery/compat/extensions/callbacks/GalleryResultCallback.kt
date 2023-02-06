package develop.file.gallery.compat.extensions.callbacks

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import develop.file.gallery.compat.GalleryConfig
import develop.file.gallery.compat.extensions.ResultCompat.parcelableArrayList
import develop.file.gallery.extensions.ResultCompat.parcelableVersionNotNull

open class GalleryResultCallback(private val listener: GalleryListener) :
    ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundle: Bundle = intent.data?.extras ?: return
        when (intent.resultCode) {
            GalleryConfig.Crop.RESULT_CODE_CROP -> onCropResult(bundle)
            GalleryConfig.RESULT_CODE_SINGLE_DATA -> onSingleDataResult(bundle)
            GalleryConfig.RESULT_CODE_MULTIPLE_DATA -> onMultipleDataResult(bundle)
            GalleryConfig.RESULT_CODE_TOOLBAR_BACK,
            Activity.RESULT_CANCELED -> onCancelResult(bundle)
        }
    }

    open fun onCropResult(bundle: Bundle) {
        listener.onGalleryCropResource(bundle.parcelableVersionNotNull(GalleryConfig.Crop.RESULT_CODE_CROP.toString()))
    }

    open fun onSingleDataResult(bundle: Bundle) {
        listener.onGalleryResource(bundle.parcelableVersionNotNull(GalleryConfig.RESULT_CODE_SINGLE_DATA.toString()))
    }

    open fun onMultipleDataResult(bundle: Bundle) {
        listener.onGalleryResources(bundle.parcelableArrayList(GalleryConfig.RESULT_CODE_MULTIPLE_DATA.toString()))
    }

    open fun onCancelResult(bundle: Bundle) {
        listener.onGalleryCancel()
    }

}