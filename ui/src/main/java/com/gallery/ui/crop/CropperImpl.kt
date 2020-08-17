package com.gallery.ui.crop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.expand.reset
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.ui.UIResult
import com.theartofdev.edmodo.cropper.CropImage

open class CropperImpl : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(scanFragment: ScanFragment, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                CropImage.getActivityResult(it).uri?.let { uri -> onCropSuccess(scanFragment, uri) }
                        ?: onCropError(scanFragment, null)
            } ?: onCropError(scanFragment, null)
            Activity.RESULT_CANCELED -> onCropCanceled(scanFragment)
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> onCropError(scanFragment, intent.data?.let { CropImage.getActivityResult(it).error })
        }
    }

    override fun openCrop(scanFragment: ScanFragment, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(scanFragment.requireContext(), galleryBundle)
        return CropImage
                .activity(inputUri)
                .setOutputUri(cropUri)
                .getIntent(scanFragment.requireActivity())
    }

    open fun onCropSuccess(scanFragment: ScanFragment, uri: Uri) {
        scanFragment.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        scanFragment.requireActivity().setResult(UIResult.GALLERY_CROP_RESULT_CODE, intent)
        scanFragment.requireActivity().finish()
    }

    open fun onCropCanceled(scanFragment: ScanFragment) {
        cropUri?.reset(scanFragment.requireContext())
    }

    open fun onCropError(scanFragment: ScanFragment, throwable: Throwable?) {
        cropUri?.reset(scanFragment.requireContext())
    }

}