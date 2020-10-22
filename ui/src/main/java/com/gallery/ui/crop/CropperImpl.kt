package com.gallery.ui.crop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.kotlin.expand.net.deleteExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.result.UiConfig
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageOptions

class CropperImpl(private val activity: Activity, private val galleryUiBundle: GalleryUiBundle) : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(delegate: IScanDelegate, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> CropImage.getActivityResult(intent.data)?.uri?.let { uri -> onCropSuccess(delegate, uri) }
                    ?: cropUri?.deleteExpand(activity)
            Activity.RESULT_CANCELED -> cropUri?.deleteExpand(activity)
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> cropUri?.deleteExpand(activity)
        }
    }

    override fun openCrop(delegate: IScanDelegate, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(activity, galleryBundle)
        val intent = Intent().setClass(activity, CropImageActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, inputUri)
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, runCatching {
            val args = galleryUiBundle.args
            args.classLoader = CropImageOptions::class.java.classLoader
            args.getParcelable<CropImageOptions>(UiConfig.CROP_ARGS)
        }.getOrElse { CropImageOptions() } ?: CropImageOptions())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle)
        return intent
    }

    private fun onCropSuccess(delegate: IScanDelegate, uri: Uri) {
        delegate.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UiConfig.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        activity.setResult(UiConfig.RESULT_CODE_CROP, intent)
        activity.finish()
    }
}