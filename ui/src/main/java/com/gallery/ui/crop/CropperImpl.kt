package com.gallery.ui.crop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.ScanDelegate
import com.gallery.scan.types.deleteExpand
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageOptions

class CropperImpl(private val galleryUiBundle: GalleryUiBundle) : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(delegate: ScanDelegate, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> CropImage.getActivityResult(intent.data)?.uri?.let { uri -> onCropSuccess(delegate, uri) }
                    ?: cropUri?.deleteExpand(delegate.activityNotNull)
            Activity.RESULT_CANCELED -> cropUri?.deleteExpand(delegate.activityNotNull)
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> cropUri?.deleteExpand(delegate.activityNotNull)
        }
    }

    override fun openCrop(delegate: ScanDelegate, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(delegate.activityNotNull, galleryBundle)
        val intent = Intent().setClass(delegate.activityNotNull, CropImageActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, inputUri)
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, runCatching {
            val args = galleryUiBundle.args
            args.classLoader = CropImageOptions::class.java.classLoader
            args.getParcelable<CropImageOptions>(UIResult.CROP_ARGS)
        }.getOrElse { CropImageOptions() } ?: CropImageOptions())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle)
        return intent
    }

    private fun onCropSuccess(delegate: ScanDelegate, uri: Uri) {
        delegate.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        delegate.activityNotNull.setResult(UIResult.RESULT_CODE_CROP, intent)
        delegate.activityNotNull.finish()
    }
}