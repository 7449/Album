package com.gallery.sample.camera

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.kotlin.expand.net.orEmptyExpand
import com.gallery.sample.R
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.activity_camera.*


class CameraActivity : AppCompatActivity() {

    companion object {
        /**
         * 自定义相机路径
         */
        const val CUSTOM_CAMERA_OUT_PUT_URI = "customCameraOutPutUri"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        CameraLogger.setLogLevel(CameraLogger.LEVEL_VERBOSE)
        camera.setLifecycleOwner(this)
        camera.addCameraListener(Listener())
        cameraOk.setOnClickListener {
            if (camera.isTakingPicture) return@setOnClickListener
            camera.takePictureSnapshot()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    private inner class Listener : CameraListener() {

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            val fileUri: Uri = intent.extras?.getParcelable<Uri>(CUSTOM_CAMERA_OUT_PUT_URI).orEmptyExpand()
            Log.i("Camera", fileUri.toString())
            contentResolver.openOutputStream(fileUri)?.use { it.write(result.data) }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}