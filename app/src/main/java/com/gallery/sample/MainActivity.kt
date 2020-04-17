@file:Suppress("FunctionName")

package com.gallery.sample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gallery.ui.Gallery
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGallery
import com.gallery.core.ext.galleryPathFile
import com.gallery.core.ext.findUriByFile
import com.gallery.core.ext.openCamera
import com.gallery.glide.GlideImageLoader
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanType
import com.gallery.scan.SingleMediaScanner
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.ui
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

fun MainActivity.dayGallery() {
    Gallery.instance.apply {
        galleryImageLoader = GlideImageLoader()
        galleryListener = GalleryListener(applicationContext, list)
    }.ui(this,
            GalleryBundle(scanType = ScanType.IMAGE, checkBoxDrawable = R.drawable.simple_selector_gallery_item_check))
}

fun MainActivity.nightGallery() {
    Gallery.instance.apply {
        galleryListener = GalleryListener(applicationContext, null)
        galleryImageLoader = GlideImageLoader()
    }.ui(this, GalleryTheme.NightGalleryBundle(), GalleryTheme.NightGalleryUIBundle())
}

fun MainActivity.video() {
    Gallery.instance.apply {
        galleryImageLoader = GlideImageLoader()
        galleryListener = GalleryListener(applicationContext, null)
    }.ui(this, GalleryBundle(
            scanType = ScanType.VIDEO,
            cameraText = getString(R.string.video_tips)),
            GalleryUiBundle(toolbarText = R.string.gallery_video_title))
}

fun MainActivity.startCamera() {
    imagePath = findUriByFile(applicationContext.galleryPathFile(null, System.currentTimeMillis().toString()))
    val i = openCamera(imagePath, false)
    Log.d(javaClass.simpleName, i.toString())
}

class SimpleSingleScannerListener : SingleMediaScanner.SingleScannerListener {
    override fun onScanCompleted(type: Int, path: String?, uri: Uri?) {
    }

    override fun onScanStart() {}
}

class MainActivity : AppCompatActivity(), OnClickListener, UCropFragmentCallback {

    lateinit var dayOptions: UCrop.Options
    lateinit var nightOptions: UCrop.Options
    lateinit var list: ArrayList<ScanEntity>
    lateinit var imagePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_day_gallery.setOnClickListener(this)
        btn_night_gallery.setOnClickListener(this)
        btn_open_camera.setOnClickListener(this)
        btn_video.setOnClickListener(this)

        dayOptions = UCrop.Options()
        dayOptions.apply {
            setToolbarTitle("DayTheme")
            setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorGalleryToolbarBackground))
            setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.colorGalleryStatusBarColor))
            setActiveWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.colorGalleryToolbarBackground))
        }

        nightOptions = UCrop.Options()
        nightOptions.setToolbarTitle("NightTheme")
        nightOptions.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorGalleryToolbarTextColorNight))
        nightOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorGalleryToolbarBackgroundNight))
        nightOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorGalleryToolbarBackgroundNight))
        nightOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorGalleryStatusBarColorNight))

        list = ArrayList()
    }

    override fun onClick(v: View) {
        Gallery.destroy()
        when (v.id) {
            R.id.btn_day_gallery -> dayGallery()
            R.id.btn_night_gallery -> nightGallery()
            R.id.btn_open_camera -> startCamera()
            R.id.btn_video -> video()
        }
    }

    override fun onCropFinish(result: UCropFragment.UCropResult) {
    }

    override fun loadingProgress(showLoader: Boolean) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> {
            }
            UCrop.RESULT_ERROR -> {
            }
            Activity.RESULT_OK -> when (requestCode) {
                IGallery.CAMERA_REQUEST_CODE -> {
                    SingleMediaScanner(this, imagePath.path
                            ?: "", 1, SimpleSingleScannerListener())
                }
                UCrop.REQUEST_CROP -> {
                    SingleMediaScanner(this, imagePath.path
                            ?: "", 2, SimpleSingleScannerListener())
                    Toast.makeText(applicationContext, imagePath.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
