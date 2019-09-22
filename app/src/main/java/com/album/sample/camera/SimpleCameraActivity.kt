package com.album.sample.camera

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.album.core.albumPathFile
import com.album.core.finishCamera
import com.album.core.ui.AlbumBaseActivity
import com.album.sample.R
import com.google.android.cameraview.AspectRatio
import com.google.android.cameraview.CameraView
import kotlinx.android.synthetic.main.activity_simple_camera.*
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * by y on 28/08/2017.
 */

class SimpleCameraActivity : AlbumBaseActivity(), ActivityCompat.OnRequestPermissionsResultCallback, SimpleAspectRatioFragment.Listener {
    override fun initView() {
        findViewById<View>(R.id.take_picture).setOnClickListener { camera.takePicture() }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        initCamera()
    }

    override val layoutId: Int = R.layout.activity_simple_camera

    private var mCurrentFlash: Int = 0

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.simple_camera_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.aspect_ratio -> {
                val fragmentManager = supportFragmentManager
                if (fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
                    val ratios = camera.supportedAspectRatios
                    val currentRatio = camera.aspectRatio
                    SimpleAspectRatioFragment.newInstance(ratios, currentRatio!!)
                            .show(fragmentManager, FRAGMENT_DIALOG)
                }
                return true
            }
            R.id.switch_flash -> {
                mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.size
                item.setTitle(FLASH_TITLES[mCurrentFlash])
                item.setIcon(FLASH_ICONS[mCurrentFlash])
                camera.flash = FLASH_OPTIONS[mCurrentFlash]
                return true
            }
            R.id.switch_camera -> {
                val facing = camera.facing
                camera.facing = if (facing == CameraView.FACING_FRONT) CameraView.FACING_BACK else CameraView.FACING_FRONT
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAspectRatioSelected(ratio: AspectRatio) {
        Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show()
        camera.setAspectRatio(ratio)
    }

    private fun initCamera() {
        camera.addCallback(object : CameraView.Callback() {
            override fun onCameraOpened(cameraView: CameraView) {
                super.onCameraOpened(cameraView)
                Log.d(TAG, "onCameraOpened")
            }

            override fun onCameraClosed(cameraView: CameraView) {
                super.onCameraClosed(cameraView)
                Log.d(TAG, "onCameraClosed")
            }

            override fun onPictureTaken(cameraView: CameraView, data: ByteArray) {
                super.onPictureTaken(cameraView, data)
                Toast.makeText(cameraView.context, R.string.picture_taken, Toast.LENGTH_SHORT).show()
                cameraView.post {
                    val cameraFile = albumPathFile(null, System.currentTimeMillis().toString(), "jpg")
                    val os: OutputStream
                    try {
                        os = FileOutputStream(cameraFile)
                        os.write(data)
                        os.close()
                        finishCamera(cameraFile)
                    } catch (e: IOException) {
                        Log.w(TAG, "Cannot write to $cameraFile", e)
                    }
                }
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "cancel camera", Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val TAG = "MainActivity"

        private const val FRAGMENT_DIALOG = "dialog"

        private val FLASH_OPTIONS = intArrayOf(CameraView.FLASH_AUTO, CameraView.FLASH_OFF, CameraView.FLASH_ON)

        private val FLASH_ICONS = intArrayOf(R.drawable.ic_flash_auto, R.drawable.ic_flash_off, R.drawable.ic_flash_on)

        private val FLASH_TITLES = intArrayOf(R.string.flash_auto, R.string.flash_off, R.string.flash_on)
    }


}
