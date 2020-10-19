package com.gallery.sample.custom

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gallery.ui.compat.galleryFragment
import com.gallery.sample.camera.CameraActivity
import com.gallery.ui.page.GalleryActivity

class CustomCameraActivity : GalleryActivity() {

    private val cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        when (intent.resultCode) {
            Activity.RESULT_CANCELED -> galleryFragment.onCameraResultCanceled()
            Activity.RESULT_OK -> galleryFragment.onCameraResultOk()
        }
    }

    override fun onCustomCamera(uri: Uri): Boolean {
        cameraLauncher.launch(Intent(this, CameraActivity::class.java).apply {
            putExtras(Bundle().apply {
                this.putParcelable(CameraActivity.CUSTOM_CAMERA_OUT_PUT_URI, uri)
            })
        })
        return true
    }

}