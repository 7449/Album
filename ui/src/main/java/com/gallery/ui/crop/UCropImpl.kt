package com.gallery.ui.crop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.kotlin.expand.content.findPathByUriExpand
import androidx.kotlin.expand.net.orEmptyExpand
import androidx.kotlin.expand.os.orEmptyExpand
import androidx.kotlin.expand.util.copyImageExpand
import androidx.kotlin.expand.version.hasQExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.ScanDelegate
import com.gallery.core.expand.cropNameExpand
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.yalantis.ucrop.UCrop
import java.io.File

@Deprecated("annoying version support", replaceWith = ReplaceWith("CropperImpl(galleryUiBundle)"))
open class UCropImpl(private val galleryUiBundle: GalleryUiBundle) : ICrop {

    override fun onCropResult(scanFragment: ScanDelegate, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                UCrop.getOutput(it)?.let { uri ->
                    onCropSuccess(scanFragment, galleryBundle, uri)
                } ?: onCropError(null)
            } ?: onCropError(null)
            Activity.RESULT_CANCELED -> onCropCanceled()
            UCrop.RESULT_ERROR -> onCropError(intent.data?.let { UCrop.getError(it) })
        }
    }

    override fun openCrop(scanFragment: ScanDelegate, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        return UCrop.of(inputUri, cropOutPutUri2(scanFragment.activityNotNull, galleryBundle))
                .withOptions(UCrop.Options().apply { optionBundle.putAll(galleryUiBundle.args.getBundle(UIResult.UI_CROP_ARGS).orEmptyExpand()) })
                .getIntent(scanFragment.activityNotNull)
    }

    open fun onCropSuccess(scanFragment: ScanDelegate, galleryBundle: GalleryBundle, uri: Uri) {
        val currentUri: Uri = if (!hasQExpand()) {
            uri
        } else {
            val contentUri = scanFragment.activityNotNull.copyImageExpand(uri, galleryBundle.cropNameExpand).orEmptyExpand()
            val filePath: String? = scanFragment.activityNotNull.findPathByUriExpand(contentUri)
            if (filePath.isNullOrEmpty()) {
                uri
            } else {
                File(uri.path.orEmpty()).delete()
                Uri.fromFile(File(filePath))
            }
        }
        scanFragment.onScanResult(currentUri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_CROP, currentUri)
        intent.putExtras(bundle)
        scanFragment.activityNotNull.setResult(UIResult.GALLERY_CROP_RESULT_CODE, intent)
        scanFragment.activityNotNull.finish()
    }

    open fun onCropCanceled() {
    }

    open fun onCropError(let: Throwable?) {
    }

}