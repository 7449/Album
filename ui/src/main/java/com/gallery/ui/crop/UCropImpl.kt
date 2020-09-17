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

    override fun onCropResult(delegate: ScanDelegate, galleryBundle: GalleryBundle, intent: ActivityResult) {
        when (intent.resultCode) {
            Activity.RESULT_OK -> intent.data?.let {
                UCrop.getOutput(it)?.let { uri ->
                    onCropSuccess(delegate, galleryBundle, uri)
                }
            }
        }
    }

    override fun openCrop(delegate: ScanDelegate, galleryBundle: GalleryBundle, inputUri: Uri): Intent {
        return UCrop.of(inputUri, cropOutPutUri2(delegate.activityNotNull, galleryBundle))
                .withOptions(UCrop.Options().apply { optionBundle.putAll(galleryUiBundle.args.getBundle(UIResult.CROP_ARGS).orEmptyExpand()) })
                .getIntent(delegate.activityNotNull)
    }

    private fun onCropSuccess(delegate: ScanDelegate, galleryBundle: GalleryBundle, uri: Uri) {
        val currentUri: Uri = if (!hasQExpand()) {
            uri
        } else {
            val contentUri = delegate.activityNotNull.copyImageExpand(uri, galleryBundle.cropNameExpand).orEmptyExpand()
            val filePath: String? = delegate.activityNotNull.findPathByUriExpand(contentUri)
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
        bundle.putParcelable(UIResult.GALLERY_RESULT_CROP, currentUri)
        intent.putExtras(bundle)
        delegate.activityNotNull.setResult(UIResult.RESULT_CODE_CROP, intent)
        delegate.activityNotNull.finish()
    }
}