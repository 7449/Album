package com.gallery.sample.crop

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
import com.gallery.core.extensions.cropNameExpand
import com.gallery.core.extensions.hasQExpand
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.core.extensions.queryDataExpand
import com.gallery.sample.extensions.copyImageExpand
import com.yalantis.ucrop.UCrop
import java.io.File

open class SimpleUCropImpl : ICrop {

    override fun onCropResult(
        delegate: IScanDelegate,
        galleryBundle: GalleryBundle,
        intent: ActivityResult
    ) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                UCrop.getOutput(it)?.let { uri ->
                    onCropSuccess(delegate, galleryBundle, uri)
                }
            }
        }
    }

    override fun openCrop(context: Context, bundle: GalleryBundle, inputUri: Uri): Intent {
        return UCrop.of(inputUri, cropOutPutUri2(context, bundle))
            .withOptions(UCrop.Options())
            .getIntent(context)
    }

    private fun onCropSuccess(delegate: IScanDelegate, galleryBundle: GalleryBundle, uri: Uri) {
        val currentUri: Uri = if (!hasQExpand()) {
            uri
        } else {
            val contentUri =
                delegate.requireActivity.copyImageExpand(uri, galleryBundle.cropNameExpand)
                    .orEmptyExpand()
            val filePath: String? =
                delegate.requireActivity.contentResolver.queryDataExpand(contentUri)
            if (filePath.isNullOrEmpty()) {
                uri
            } else {
                File(uri.path.orEmpty()).delete()
                Uri.fromFile(File(filePath))
            }
        }
        delegate.onScanResult(currentUri)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(GalleryConfig.Crop.GALLERY_RESULT_CROP, currentUri)
        intent.putExtras(bundle)
        delegate.requireActivity.setResult(GalleryConfig.Crop.RESULT_CODE_CROP, intent)
        delegate.requireActivity.finish()
    }

}