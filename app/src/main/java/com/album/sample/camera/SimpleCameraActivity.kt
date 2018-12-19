package com.album.sample.camera

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.album.sample.R
import com.album.finishCamera
import com.album.getCameraFile
import com.google.android.cameraview.AspectRatio
import com.google.android.cameraview.CameraView
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * by y on 28/08/2017.
 */

class SimpleCameraActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback, SimpleAspectRatioFragment.Listener {

    private var mCurrentFlash: Int = 0
    private lateinit var mCameraView: CameraView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_camera)
        mCameraView = findViewById(R.id.camera)
        findViewById<View>(R.id.take_picture).setOnClickListener { mCameraView.takePicture() }
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        initCamera()
    }

    override fun onResume() {
        super.onResume()
        mCameraView.start()
    }

    override fun onPause() {
        mCameraView.stop()
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
                    val ratios = mCameraView.supportedAspectRatios
                    val currentRatio = mCameraView.aspectRatio
                    SimpleAspectRatioFragment.newInstance(ratios, currentRatio!!)
                            .show(fragmentManager, FRAGMENT_DIALOG)
                }
                return true
            }
            R.id.switch_flash -> {
                mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.size
                item.setTitle(FLASH_TITLES[mCurrentFlash])
                item.setIcon(FLASH_ICONS[mCurrentFlash])
                mCameraView.flash = FLASH_OPTIONS[mCurrentFlash]
                return true
            }
            R.id.switch_camera -> {
                val facing = mCameraView.facing
                mCameraView.facing = if (facing == CameraView.FACING_FRONT) CameraView.FACING_BACK else CameraView.FACING_FRONT
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAspectRatioSelected(ratio: AspectRatio) {
        Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show()
        mCameraView.setAspectRatio(ratio)
    }

    private fun initCamera() {
        mCameraView.addCallback(object : CameraView.Callback() {
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
                    val cameraFile = getCameraFile(cameraView.context, null, false)
                    val os: OutputStream
                    try {
                        os = FileOutputStream(cameraFile)
                        os.write(data)
                        os.close()
                        finishCamera(this@SimpleCameraActivity, cameraFile.path)
                    } catch (e: IOException) {
                        Log.w(TAG, "Cannot write to " + cameraFile.path, e)
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
