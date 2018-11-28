package com.album.sample

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
import com.album.*
import com.album.sample.camera.SimpleCameraActivity
import com.album.sample.imageloader.SimpleFrescoAlbumImageLoader
import com.album.sample.imageloader.SimpleSubsamplingScaleImageLoader
import com.album.sample.ui.SimpleDialogFragment
import com.album.ui.activity.AlbumActivity
import com.album.ui.fragment.AlbumBaseFragment
import com.album.util.*
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener, SingleScannerListener {
    override fun onScanCompleted(type: Int) {
    }

    override fun onScanStart() {
    }

    private lateinit var dayOptions: UCrop.Options
    private lateinit var nightOptions: UCrop.Options
    private lateinit var list: ArrayList<AlbumEntity>
    private lateinit var imagePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn_day_album).setOnClickListener(this)
        findViewById<View>(R.id.btn_night_album).setOnClickListener(this)
        findViewById<View>(R.id.btn_open_camera).setOnClickListener(this)
        findViewById<View>(R.id.btn_sample_ui).setOnClickListener(this)
        findViewById<View>(R.id.btn_customize_camera).setOnClickListener(this)
        findViewById<View>(R.id.btn_dialog).setOnClickListener(this)
        findViewById<View>(R.id.btn_video).setOnClickListener(this)
        findViewById<View>(R.id.btn_subsampling).setOnClickListener(this)

        dayOptions = UCrop.Options()
        dayOptions.apply {
            setToolbarTitle("DayTheme")
            setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorAlbumToolbarBackground))
            setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.colorAlbumStatusBarColor))
            setActiveWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.colorAlbumToolbarBackground))
        }

        nightOptions = UCrop.Options()
        nightOptions.setToolbarTitle("NightTheme")
        nightOptions.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarTextColorNight))
        nightOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight))
        nightOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight))
        nightOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAlbumStatusBarColorNight))
        list = ArrayList()
    }


    private fun onDayAlbumClick(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleFrescoAlbumImageLoader()
            albumListener = MainAlbumListener(this@MainActivity, list)
            albumEntityList = list
            options = dayOptions
            emptyClickListener = object : OnEmptyClickListener {
                override fun click(view: View): Boolean = true
            }
//            config = AlbumConfig().apply {
//                //                cameraCrop = false
////                filterImg = true
////                isPermissionsDeniedFinish = false
//                previewBackRefresh = true
//                previewFinishRefresh = true
////                albumCheckBoxDrawable = R.drawable.simple_selector_album_item_check
//            }
            albumCustomListener = null
        }
    }

    private fun onNightClick(): Album {
        return Album.instance.apply {
            albumListener = MainAlbumListener(applicationContext, null)
            options = nightOptions
            albumImageLoader = SimpleGlideAlbumImageLoader()
            albumCustomListener = null
            emptyClickListener = object : OnEmptyClickListener {
                override fun click(view: View): Boolean = true
            }
//            config = AlbumConfig(AlbumConstant.TYPE_NIGHT).apply {
//                //                isRadio = true
////                filterImg = false
////                isCrop = true
////                cameraPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM/Album"
////                uCropPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM" + "/" + "uCrop"
////                isPermissionsDeniedFinish = true
////                cameraCrop = true
//            }
        }
    }

    private fun onSimpleUi(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleGlideAlbumImageLoader()
            albumListener = MainAlbumListener(applicationContext, list)
//            albumClass = SimpleAlbumUI::class.java
//            previewClass = SimplePreviewUI::class.java
            albumCustomListener = null
            emptyClickListener = null
//            config = AlbumConfig().apply {
//                //                cameraCrop = false
//                previewBackRefresh = true
//            }
        }
    }

    private fun openCamera() {
        imagePath = Uri.fromFile(FileUtils.getCameraFile(this, null, false))
        val i = openCamera(this, imagePath, false)
        Log.d(javaClass.simpleName, i.toString())
    }

    private fun dialog() {
        SimpleDialogFragment().show(supportFragmentManager, "Dialog")
    }

    private fun customizeCamera(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleGlideAlbumImageLoader()
            albumListener = MainAlbumListener(applicationContext, list)
            albumEntityList = list
            options = dayOptions
            albumCustomListener = object : AlbumCustomListener {
                override fun startCamera(fragment: AlbumBaseFragment) {
                    if (PermissionUtils.storage(this@MainActivity) && PermissionUtils.camera(this@MainActivity)) {
                        val activity = fragment.activity
                        Toast.makeText(activity, "camera", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, SimpleCameraActivity::class.java)
                        fragment.startActivityForResult(intent, AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE)
                    }
                }
            }
            emptyClickListener = object : OnEmptyClickListener {
                override fun click(view: View): Boolean = true
            }
//            config = AlbumConfig().apply {
//                //                cameraCrop = false
////                isPermissionsDeniedFinish = false
//                previewFinishRefresh = true
//                albumBottomFinderTextBackground = R.drawable.selector_btn
//                previewBackRefresh = true
//            }
        }
    }

    private fun openVideo(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleGlideAlbumImageLoader()
            albumListener = MainAlbumListener(applicationContext, null)
            albumCustomListener = null
            emptyClickListener = object : OnEmptyClickListener {
                override fun click(view: View): Boolean = true
            }
//            config = AlbumConfig().apply {
//                //                scanType = ScanType.VIDEO
//                albumToolbarText = R.string.album_video_title
////                albumCameraText = R.string.video_tips
//            }
        }
    }


    private fun onSubsampling(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleSubsamplingScaleImageLoader()
            albumListener = MainAlbumListener(this@MainActivity, list)
            albumEntityList = list
            options = dayOptions
            emptyClickListener = object : OnEmptyClickListener {
                override fun click(view: View): Boolean = true
            }
//            config = AlbumConfig().apply {
//                //                cameraCrop = false
////                filterImg = true
////                isPermissionsDeniedFinish = false
//                previewBackRefresh = true
//                previewFinishRefresh = true
////                albumCheckBoxDrawable = R.drawable.simple_selector_album_item_check
//            }
            albumCustomListener = null
        }
    }

    override fun onClick(v: View) {
        Album.reset()
        when (v.id) {
            R.id.btn_day_album -> onDayAlbumClick().start(this, AlbumActivity::class.java)
            R.id.btn_night_album -> onNightClick().start(this, AlbumActivity::class.java)
            R.id.btn_sample_ui -> onSimpleUi().start(this, AlbumActivity::class.java)
            R.id.btn_open_camera -> openCamera()
            R.id.btn_dialog -> dialog()
            R.id.btn_customize_camera -> customizeCamera().start(this, AlbumActivity::class.java)
            R.id.btn_video -> openVideo().start(this, AlbumActivity::class.java)
            R.id.btn_subsampling -> onSubsampling().start(this, AlbumActivity::class.java)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED -> {
            }
            UCrop.RESULT_ERROR -> {
            }
            Activity.RESULT_OK -> when (requestCode) {
                AlbumConstant.ITEM_CAMERA -> {
                    SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.path ?: ""),
                            this, AlbumConstant.TYPE_RESULT_CAMERA)
                    UCrop.of(Uri.fromFile(File(imagePath.path)), imagePath)
                            .withOptions(UCrop.Options())
                            .start(this)
                }
                UCrop.REQUEST_CROP -> {
                    SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.path ?: ""),
                            this, AlbumConstant.TYPE_RESULT_CROP)
                    Toast.makeText(applicationContext, imagePath.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
