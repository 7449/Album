package com.gallery.ui.crop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.gallery.core.crop.ICrop
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.ui.UIResult
import com.theartofdev.edmodo.cropper.CropImage

open class CropperImpl(private val galleryFragment: ScanFragment) : ICrop {

    override fun onCropResult(intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                CropImage.getActivityResult(it).uri?.let { uri ->
                    onCropSuccess(uri)
                } ?: onCropError(null)
            } ?: onCropError(null)
            Activity.RESULT_CANCELED -> onCropCanceled()
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> onCropError(intent.data?.let { CropImage.getActivityResult(it).error })
        }
    }

    override fun openCrop(inputUri: Uri, outPutUri: Uri, outPutUri2: Uri): Intent {
        return CropImage
                .activity(inputUri)
                .setOutputUri(outPutUri)
                .getIntent(galleryFragment.requireActivity())
    }

    open fun onCropSuccess(uri: Uri) {
        galleryFragment.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        galleryFragment.requireActivity().setResult(UIResult.GALLERY_CROP_RESULT_CODE, intent)
        galleryFragment.requireActivity().finish()
    }

    open fun onCropCanceled() {
    }

    open fun onCropError(let: Throwable?) {
    }

}