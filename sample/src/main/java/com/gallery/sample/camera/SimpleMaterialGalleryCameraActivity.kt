package com.gallery.sample.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gallery.compat.extensions.requireGalleryFragment
import com.gallery.ui.material.activity.MaterialGalleryActivity

class SimpleMaterialGalleryCameraActivity : MaterialGalleryActivity() {

    private val cameraLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
                when (intent.resultCode) {
                    Activity.RESULT_CANCELED -> requireGalleryFragment.onCameraResultCanceled()
                    Activity.RESULT_OK -> requireGalleryFragment.onCameraResultOk()
                }
            }

    override fun onCustomCamera(uri: Uri): Boolean {
        cameraLauncher.launch(Intent(this, SimpleCameraActivity::class.java).apply {
            putExtras(Bundle().apply {
                this.putParcelable(SimpleCameraActivity.CUSTOM_CAMERA_OUT_PUT_URI, uri)
            })
        })
        return true
    }

}