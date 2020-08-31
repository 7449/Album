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

open class CropperImpl(private val galleryUiBundle: GalleryUiBundle) : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(scanFragment: ScanDelegate, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> CropImage.getActivityResult(intent.data)?.uri?.let { uri -> onCropSuccess(scanFragment, uri) }
                    ?: onCropError(scanFragment, null)
            Activity.RESULT_CANCELED -> onCropCanceled(scanFragment)
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> onCropError(scanFragment, CropImage.getActivityResult(intent.data)?.error)
        }
    }

    override fun openCrop(scanFragment: ScanDelegate, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(scanFragment.activityNotNull, galleryBundle)
        val options = runCatching {
            val args = galleryUiBundle.args
            args.classLoader = CropImageOptions::class.java.classLoader
            args.getParcelable<CropImageOptions>(UIResult.UI_CROP_ARGS)
        }.getOrElse { CropImageOptions() }
        val intent = Intent()
        intent.setClass(scanFragment.activityNotNull, CropImageActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, inputUri)
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, options ?: CropImageOptions())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle)
        return intent
    }

    open fun onCropSuccess(scanFragment: ScanDelegate, uri: Uri) {
        scanFragment.onScanResult(uri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_CROP, uri)
        intent.putExtras(bundle)
        scanFragment.activityNotNull.setResult(UIResult.GALLERY_CROP_RESULT_CODE, intent)
        scanFragment.activityNotNull.finish()
    }

    open fun onCropCanceled(scanFragment: ScanDelegate) {
        cropUri?.deleteExpand(scanFragment.activityNotNull)
    }

    open fun onCropError(scanFragment: ScanDelegate, throwable: Throwable?) {
        cropUri?.deleteExpand(scanFragment.activityNotNull)
    }

}