package com.gallery.ui.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.gallery.compat.GalleryCompatBundle
import com.gallery.compat.GalleryConfig
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.extensions.deleteExpand
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageOptions

class GalleryCompatCropper(
    private val activity: Activity,
    private val compatBundle: GalleryCompatBundle
) : ICrop {

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
            } ?: cropUri?.deleteExpand(activity)
            Activity.RESULT_CANCELED, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
            -> cropUri?.deleteExpand(activity)
        }
    }

    override fun openCrop(context: Context, bundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(activity, bundle)
        val intent = Intent().setClass(activity, CropImageActivity::class.java)
        val value = Bundle()
        value.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, inputUri)
        value.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, runCatching {
            val args = compatBundle.args
            args.classLoader = CropImageOptions::class.java.classLoader
            args.getParcelable<CropImageOptions>(GalleryConfig.CROP_ARGS)
        }.getOrElse { CropImageOptions() } ?: CropImageOptions())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, value)
        return intent
    }

    private fun onCropSuccess(delegate: IScanDelegate, uri: Uri) {
        delegate.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(GalleryConfig.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        activity.setResult(GalleryConfig.RESULT_CODE_CROP, intent)
        activity.finish()
    }

}