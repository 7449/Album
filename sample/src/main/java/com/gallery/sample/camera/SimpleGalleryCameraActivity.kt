package com.gallery.sample.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import develop.file.gallery.compat.extensions.ActivityCompat.requireGalleryFragment
import develop.file.gallery.ui.material.activity.MaterialGalleryActivity

class SimpleGalleryCameraActivity : MaterialGalleryActivity() {

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
            when (intent.resultCode) {
                Activity.RESULT_CANCELED -> requireGalleryFragment.onCameraResultCanceled()
                Activity.RESULT_OK -> requireGalleryFragment.onCameraResultOk()
            }
        }

    override fun onCustomCamera(uri: Uri): Boolean {
        cameraLauncher.launch(Intent(this, SimpleCameraActivity::class.java).apply {
            putExtras(bundleOf(SimpleCameraActivity.CUSTOM_CAMERA_OUT_PUT_URI to uri))
        })
        return true
    }

}