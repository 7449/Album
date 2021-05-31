package com.gallery.ui.material.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.gallery.compat.GalleryConfig
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.extensions.deleteExpand
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageOptions

class GalleryMaterialCropper(private val cropImageOptions: CropImageOptions) : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(
        delegate: IScanDelegate,
        galleryBundle: GalleryBundle,
        intent: ActivityResult
    ) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> CropImage.getActivityResult(intent.data)?.uri?.let { uri ->
                onCropSuccess(
                    delegate,
                    uri
                )
            } ?: cropUri?.deleteExpand(delegate.requireActivity)
            Activity.RESULT_CANCELED, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
            -> cropUri?.deleteExpand(delegate.requireActivity)
        }
    }

    override fun openCrop(context: Context, bundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(context, bundle)
        val intent = Intent().setClass(context, CropImageActivity::class.java)
        val value = Bundle()
        value.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, inputUri)
        value.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, cropImageOptions)
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, value)
        return intent
    }

    private fun onCropSuccess(delegate: IScanDelegate, uri: Uri) {
        delegate.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(GalleryConfig.Crop.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        delegate.requireActivity.setResult(GalleryConfig.Crop.RESULT_CODE_CROP, intent)
        delegate.requireActivity.finish()
    }

}