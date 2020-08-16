package com.gallery.ui.crop

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.kotlin.expand.app.findPathByUriExpand
import androidx.kotlin.expand.net.orEmptyExpand
import androidx.kotlin.expand.util.copyImageExpand
import androidx.kotlin.expand.version.hasQExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.expand.cropNameExpand
import com.gallery.core.expand.reset
import com.gallery.core.expand.scanFile
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.yalantis.ucrop.UCrop
import java.io.File

open class UCropImpl(
        private val galleryFragment: ScanFragment,
        private val galleryBundle: GalleryBundle,
        private val galleryUiBundle: GalleryUiBundle
) : ICrop {

    override val cropImpl: ICrop?
        get() = null

    override fun cropCallback(intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                UCrop.getOutput(it)?.let { uri ->
                    onCropSuccess(uri)
                } ?: onCropError(null)
            } ?: onCropError(null)
            Activity.RESULT_CANCELED -> onCropCanceled()
            UCrop.RESULT_ERROR -> onCropError(intent.data?.let { UCrop.getError(it) })
        }
    }

    override fun openCrop(inputUri: Uri, outPutUri: Uri, outPutUri2: Uri): Intent {
        outPutUri.reset(galleryFragment.requireActivity())
        return UCrop.of(inputUri, outPutUri2)
                .withOptions(UCrop.Options().apply { optionBundle.putAll(onUCropOptions()) })
                .getIntent(galleryFragment.requireActivity())
    }

    open fun onUCropOptions(): Bundle {
        return galleryUiBundle.args
    }

    open fun onCropSuccess(uri: Uri) {
        val currentUri: Uri = if (!hasQExpand() && uri.scheme == ContentResolver.SCHEME_FILE) {
            galleryFragment.scanFile(uri.path.orEmpty()) { galleryFragment.onScanResult(it) }
            uri
        } else if (hasQExpand() && uri.scheme == ContentResolver.SCHEME_FILE && uri.path.orEmpty().contains(galleryFragment.requireActivity().packageName)) {
            val filePath: String? = galleryFragment.findPathByUriExpand(galleryFragment.requireActivity().copyImageExpand(uri, galleryBundle.cropNameExpand).orEmptyExpand())
            if (filePath.isNullOrEmpty()) {
                uri
            } else {
                galleryFragment.scanFile(filePath) {
                    galleryFragment.onScanResult(it)
                    File(uri.path.orEmpty()).delete()
                }
                Uri.fromFile(File(filePath))
            }
        } else {
            uri
        }
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_URI, currentUri)
        intent.putExtras(bundle)
        galleryFragment.requireActivity().setResult(UIResult.GALLERY_RESULT_CROP, intent)
        galleryFragment.requireActivity().finish()
    }

    open fun onCropCanceled() {
    }

    open fun onCropError(let: Throwable?) {
    }


}