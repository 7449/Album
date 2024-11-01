package develop.file.gallery.ui.material.crop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.core.os.bundleOf
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.parcelable
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.compat.GalleryConfig
import develop.file.gallery.crop.ICrop
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.extensions.UriCompat.delete

internal class MaterialGalleryCropper(private val cropImageOptions: CropImageOptions) : ICrop {

    private var cropUri: Uri? = null

    override fun onCropResult(delegate: IScanDelegate, intent: ActivityResult) {
        delegate.activity ?: return
        when (intent.resultCode) {
            Activity.RESULT_OK -> CropImage.getActivityResult(intent.data)?.uriContent?.let { uri ->
                onCropSuccess(delegate, uri)
            } ?: cropUri?.delete(delegate.requireActivity)

            Activity.RESULT_CANCELED, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
                -> cropUri?.delete(delegate.requireActivity)
        }
    }

    override fun openCrop(context: Context, configs: GalleryConfigs, inputUri: Uri): Intent {
        this.cropUri = cropOutPutUri(context, configs)
        val intent = Intent().setClass(context, CropImageActivity::class.java)
        intent.putExtra(
            CropImage.CROP_IMAGE_EXTRA_BUNDLE, bundleOf(
                CropImage.CROP_IMAGE_EXTRA_SOURCE to inputUri,
                CropImage.CROP_IMAGE_EXTRA_OPTIONS to cropImageOptions.apply {
                    customOutputUri = cropUri
                }
            )
        )
        return intent
    }

    private fun CropImage.getActivityResult(intent: Intent?): CropImage.ActivityResult? {
        return intent?.parcelable(CROP_IMAGE_EXTRA_RESULT)
    }

    private fun onCropSuccess(delegate: IScanDelegate, uri: Uri) {
        delegate.onScanResult(uri)
        val intent = Intent()
        intent.putExtras(bundleOf(GalleryConfig.Crop.RESULT_CODE_CROP.toString() to uri))
        delegate.requireActivity.setResult(GalleryConfig.Crop.RESULT_CODE_CROP, intent)
        delegate.requireActivity.finish()
    }

}