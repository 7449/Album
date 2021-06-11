package com.gallery.sample.camera

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.sample.databinding.SimpleActivityCameraBinding
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.PictureResult

class SimpleCameraActivity : AppCompatActivity() {

    companion object {
        /**
         * 自定义相机路径
         */
        const val CUSTOM_CAMERA_OUT_PUT_URI = "customCameraOutPutUri"
    }

    private val viewBinding: SimpleActivityCameraBinding by lazy {
        SimpleActivityCameraBinding.inflate(
            layoutInflater
        )
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
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    private inner class Listener : CameraListener() {
        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            val fileUri: Uri =
                intent.extras?.getParcelable<Uri>(CUSTOM_CAMERA_OUT_PUT_URI).orEmptyExpand()
            Log.i("Camera", fileUri.toString())
            contentResolver.openOutputStream(fileUri)?.use { it.write(result.data) }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

}