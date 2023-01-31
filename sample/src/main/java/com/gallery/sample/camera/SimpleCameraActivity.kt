package com.gallery.sample.camera

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.gallery.core.extensions.parcelable
import com.gallery.sample.databinding.SimpleActivityCameraBinding
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.PictureResult

class SimpleCameraActivity : AppCompatActivity() {

    companion object {
        const val CUSTOM_CAMERA_OUT_PUT_URI = "customCameraOutPutUri"
    }

    private val viewBinding by lazy {
        SimpleActivityCameraBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        viewBinding.camera.setLifecycleOwner(this)
        viewBinding.camera.addCameraListener(Listener())
        viewBinding.cameraOk.setOnClickListener {
            if (viewBinding.camera.isTakingPicture) return@setOnClickListener
            viewBinding.camera.takePictureSnapshot()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
    }

    private inner class Listener : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            val fileUri: Uri? = intent.extras?.parcelable(CUSTOM_CAMERA_OUT_PUT_URI)
            contentResolver.openOutputStream(requireNotNull(fileUri))?.use { it.write(result.data) }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}