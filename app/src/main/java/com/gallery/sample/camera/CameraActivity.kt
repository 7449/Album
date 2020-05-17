package com.gallery.sample.camera

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.kotlin.expand.content.findPathByUriExpand
import androidx.kotlin.expand.net.orEmptyExpand
import androidx.kotlin.expand.version.hasQExpand
import com.gallery.core.GalleryConfig
import com.gallery.core.ext.saveToGalleryLegacy
import com.gallery.sample.R
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraLogger
import com.otaliastudios.cameraview.PictureResult
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File


class CameraActivity : AppCompatActivity() {

    companion object {
        private val LOG: CameraLogger = CameraLogger.create("CameraActivity")
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

    private fun message(content: String, important: Boolean) {
        if (important) {
            LOG.w(content)
            Toast.makeText(this, content, Toast.LENGTH_LONG).show()
        } else {
            LOG.i(content)
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    private inner class Listener : CameraListener() {

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)
            message("Got CameraException #" + exception.reason, true)
        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            val fileUri: Uri = intent.extras?.getParcelable<Uri>(GalleryConfig.CUSTOM_CAMERA_OUT_PUT_URI).orEmptyExpand()
            if (hasQExpand()) {
                result.toFile(File(externalCacheDir, "${System.currentTimeMillis()}.jpg")) {
                    val saveCropToGalleryLegacy = saveToGalleryLegacy(
                            Uri.fromFile(it),
                            fileUri,
                            it?.name.toString(),
                            "jpg",
                            Environment.DIRECTORY_DCIM
                    )
                    message(saveCropToGalleryLegacy?.path ?: it?.path.toString(), false)
                    it?.delete()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            } else {
                result.toFile(File(findPathByUriExpand(fileUri).toString())) {
                    message(it?.path.toString(), false)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }
}