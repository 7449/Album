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
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageActivity
import com.theartofdev.edmodo.cropper.CropImageOptions

open class CropperImpl(private val galleryUiBundle: GalleryUiBundle) : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(scanFragment: ScanFragment, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> CropImage.getActivityResult(intent.data)?.uri?.let { uri -> onCropSuccess(scanFragment, uri) }
                    ?: onCropError(scanFragment, null)
            Activity.RESULT_CANCELED -> onCropCanceled(scanFragment)
            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> onCropError(scanFragment, CropImage.getActivityResult(intent.data)?.error)
        }
    }

    override fun openCrop(scanFragment: ScanFragment, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(scanFragment.requireContext(), galleryBundle)
        val options = runCatching {
            val args = galleryUiBundle.args
            args.classLoader = CropImageOptions::class.java.classLoader
            args.getParcelable<CropImageOptions>(UIResult.UI_CROP_ARGS)
        }.getOrElse { CropImageOptions() }
        val intent = Intent()
        intent.setClass(scanFragment.requireContext(), CropImageActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE, inputUri)
        bundle.putParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS, options)
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundle)
        return intent
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