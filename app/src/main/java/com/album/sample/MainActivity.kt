package com.album.sample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.AlbumConfig
import com.album.AlbumConstant
import com.album.entity.AlbumEntity
import com.album.listener.AlbumCameraListener
import com.album.listener.OnEmptyClickListener
import com.album.sample.camera.SimpleCameraActivity
import com.album.sample.ui.SimpleAlbumUI
import com.album.sample.ui.SimplePreviewUI
import com.album.ui.fragment.AlbumBaseFragment
import com.album.ui.widget.SimpleGlideAlbumImageLoader
import com.album.util.AlbumTool
import com.album.util.FileUtils
import com.album.util.PermissionUtils
import com.album.util.scanner.SingleMediaScanner
import com.album.util.scanner.SingleScannerListener
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
        findViewById<View>(R.id.btn_video).setOnClickListener(this)

        dayOptions = UCrop.Options()
        dayOptions.apply {
            setToolbarTitle("DayTheme")
            setToolbarTitle("DayTheme")
            setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.colorAlbumToolbarBackgroundDay))
            setStatusBarColor(ContextCompat.getColor(this@MainActivity, R.color.colorAlbumStatusBarColorDay))
            setActiveWidgetColor(ContextCompat.getColor(this@MainActivity, R.color.colorAlbumToolbarBackgroundDay))
        }
        nightOptions = UCrop.Options()
        nightOptions.setToolbarTitle("NightTheme")
        nightOptions.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarTextColorNight))
        nightOptions.setToolbarColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight))
        nightOptions.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAlbumToolbarBackgroundNight))
        nightOptions.setStatusBarColor(ContextCompat.getColor(this, R.color.colorAlbumStatusBarColorNight))
        list = ArrayList()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_day_album -> {
                Album
                        .instance
                        .apply {
                            albumImageLoader = SimpleFrescoAlbumImageLoader()
                            albumListener = MainAlbumListener(this@MainActivity, list)
                            albumEntityList = list
                            options = dayOptions
                            emptyClickListener = object : OnEmptyClickListener {
                                override fun click(view: View): Boolean {
                                    return true
                                }
                            }
                            config = AlbumConfig().apply {
                                cameraCrop = false
                                isFrescoImageLoader = true
                                filterImg = true
                                isPermissionsDeniedFinish = false
                                previewBackRefresh = true
                                previewFinishRefresh = true
                            }
                            albumCameraListener = null
                            albumClass = null
                            previewClass = null
                        }
                        .start(this)
            }
            R.id.btn_night_album -> {
                Album
                        .instance
                        .apply {
                            albumListener = MainAlbumListener(applicationContext, null)
                            options = nightOptions
                            albumImageLoader = SimpleGlideAlbumImageLoader()
                            albumCameraListener = null
                            albumClass = null
                            previewClass = null
                            emptyClickListener = object : OnEmptyClickListener {
                                override fun click(view: View): Boolean {
                                    return true
                                }
                            }
                            config = AlbumConfig(AlbumConstant.TYPE_NIGHT).apply {
                                isRadio = true
                                filterImg = false
                                isCrop = true
                                cameraPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM/Album"
                                uCropPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM" + "/" + "uCrop"
                                isPermissionsDeniedFinish = true
                                cameraCrop = true
                            }
                        }.start(this)
            }

            R.id.btn_sample_ui -> {
                Album
                        .instance
                        .apply {
                            albumImageLoader = SimpleGlideAlbumImageLoader()
                            albumListener = MainAlbumListener(applicationContext, list)
                            albumClass = SimpleAlbumUI::class.java
                            previewClass = SimplePreviewUI::class.java
                            albumCameraListener = null
                            emptyClickListener = null
                            config = AlbumConfig().apply {
                                cameraCrop = false
                                previewBackRefresh = true
                            }
                        }.start(this)
            }

            R.id.btn_open_camera -> {
                imagePath = Uri.fromFile(FileUtils.getCameraFile(this, null, false))
                val i = AlbumTool.openCamera(this, imagePath, false)
                Log.d(javaClass.simpleName,i.toString())
            }

            R.id.btn_customize_camera -> {
                Album
                        .instance
                        .apply {
                            albumImageLoader = SimpleGlideAlbumImageLoader()
                            albumListener = MainAlbumListener(applicationContext, list)
                            albumEntityList = list
                            options = dayOptions
                            albumClass = null
                            previewClass = null
                            albumCameraListener = object : AlbumCameraListener {
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
                                override fun click(view: View): Boolean {
                                    return true
                                }
                            }
                            config = AlbumConfig().apply {
                                cameraCrop = false
                                isPermissionsDeniedFinish = false
                                previewFinishRefresh = true
                                albumBottomFinderTextBackground = R.drawable.selector_btn
                                previewBackRefresh = true
                            }
                        }.start(this)
            }

            R.id.btn_video -> {
                Album
                        .instance
                        .apply {
                            albumImageLoader = SimpleGlideAlbumImageLoader()
                            albumListener = MainAlbumListener(applicationContext, null)
                            albumCameraListener = null
                            albumClass = null
                            previewClass = null
                            emptyClickListener = object : OnEmptyClickListener {
                                override fun click(view: View): Boolean {
                                    return true
                                }
                            }
//                            albumVideoListener = object : AlbumVideoListener {
//                                override fun startVideo(fragment: Fragment) {
//                                    Toast.makeText(this@MainActivity, "startVideo", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                            config = AlbumConfig().apply {
                                isVideo = true
                                albumToolbarText = R.string.album_video_title
                                albumContentViewCameraTips = R.string.video_tips
                            }
                        }.start(this)
            }
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
                    SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.path),
                            this, AlbumConstant.TYPE_RESULT_CAMERA)
                    UCrop.of(Uri.fromFile(File(imagePath.path)), imagePath)
                            .withOptions(UCrop.Options())
                            .start(this)
                }
                UCrop.REQUEST_CROP -> {
                    SingleMediaScanner(this, FileUtils.getScannerFile(imagePath.path),
                            this, AlbumConstant.TYPE_RESULT_CROP)
                    Toast.makeText(applicationContext, imagePath.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
