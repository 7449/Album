package com.gallery.sample.custom

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.gallery.core.GalleryBundle
import com.gallery.core.ext.cropUriExpand
import com.gallery.ui.page.GalleryActivity
import com.theartofdev.edmodo.cropper.CropImage

class CustomCropActivity : GalleryActivity() {

    override fun onCustomPhotoCrop(activity: FragmentActivity, uri: Uri, galleryBundle: GalleryBundle): Intent {
        return CropImage
                .activity(uri)
                .setOutputUri(cropUriExpand(galleryBundle))
                .getIntent(this)
    }

    override fun onCropSuccessUriRule(intent: Intent?): Uri? {
        return CropImage.getActivityResult(intent).uri
    }

    override fun onCropErrorResultCode(): Int {
        return CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
    }

    override fun onCropErrorThrowable(intent: Intent?): Throwable? {
        return CropImage.getActivityResult(intent).error
    }
}