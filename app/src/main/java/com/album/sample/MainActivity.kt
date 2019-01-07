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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.album.*
import com.album.sample.camera.SimpleCameraActivity
import com.album.sample.imageloader.SimpleFrescoAlbumImageLoader
import com.album.sample.imageloader.SimpleImageLoaderAlbumImageLoader
import com.album.sample.imageloader.SimplePicassoAlbumImageLoader
import com.album.sample.imageloader.SimpleSubsamplingScaleImageLoader
import com.album.sample.ui.SimpleAlbumUI
import com.album.sample.ui.SimpleDialogFragment
import com.album.ui.AlbumUiBundle
import com.album.ui.activity.AlbumActivity
import com.album.ui.fragment.AlbumBaseFragment
import com.album.ui.wechat.activity.AlbumWeChatUiActivity
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), OnClickListener, SingleScannerListener, OnEmptyClickListener {

    override fun click(view: View): Boolean = true

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
        btn_day_album.setOnClickListener(this)
        btn_night_album.setOnClickListener(this)
        btn_open_camera.setOnClickListener(this)
        btn_sample_ui.setOnClickListener(this)
        btn_customize_camera.setOnClickListener(this)
        btn_dialog.setOnClickListener(this)
        btn_video.setOnClickListener(this)
        btn_imageloader.setOnClickListener(this)
        btn_subsampling.setOnClickListener(this)
        btn_wechat_ui.setOnClickListener(this)

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
            initList = list
            options = dayOptions
            emptyClickListener = this@MainActivity
        }
    }

    private fun onNightClick(): Album {
        return Album.instance.apply {
            albumListener = MainAlbumListener(applicationContext, null)
            options = nightOptions
            albumImageLoader = SimpleAlbumImageLoader()
            emptyClickListener = this@MainActivity
        }
    }

    private fun onSimpleUi(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleAlbumImageLoader()
            albumListener = MainAlbumListener(applicationContext, list)
        }
    }

    private fun openCamera() {
        imagePath = Uri.fromFile(getCameraFile(this, null, false))
        val i = openCamera(this, imagePath, false)
        Log.d(javaClass.simpleName, i.toString())
    }

    private fun dialog() {
        SimpleDialogFragment().show(supportFragmentManager, "Dialog")
    }

    private fun customizeCamera(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleAlbumImageLoader()
            albumListener = MainAlbumListener(applicationContext, list)
            initList = list
            options = dayOptions
            customCameraListener = object : AlbumCustomCameraListener {
                override fun startCamera(fragment: AlbumBaseFragment) {
                    if (permissionStorage(this@MainActivity) && permissionCamera(this@MainActivity)) {
                        val activity = fragment.activity
                        Toast.makeText(activity, "camera", Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, SimpleCameraActivity::class.java)
                        fragment.startActivityForResult(intent, CUSTOMIZE_CAMERA_RESULT_CODE)
                    }
                }
            }
            emptyClickListener = this@MainActivity
        }
    }

    private fun openVideo(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleAlbumImageLoader()
            albumListener = MainAlbumListener(applicationContext, null)
            emptyClickListener = this@MainActivity
        }
    }


    private fun onSubsampling(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleSubsamplingScaleImageLoader()
            albumListener = MainAlbumListener(this@MainActivity, list)
            initList = list
            options = dayOptions
            emptyClickListener = this@MainActivity
        }
    }

    private fun onWeChatUI(): Album {
        return Album.instance.apply {
            albumImageLoader = SimpleSubsamplingScaleImageLoader()
            albumListener = MainAlbumListener(this@MainActivity, list)
            initList = list
            options = dayOptions
            emptyClickListener = this@MainActivity
        }
    }

    private fun onImageLoader(albumImageLoaders: AlbumImageLoader) {
        Album.instance.apply {
            albumImageLoader = albumImageLoaders
            albumListener = MainAlbumListener(this@MainActivity, list)
            initList = list
            options = dayOptions
            emptyClickListener = this@MainActivity
        }.start(this, AlbumActivity::class.java)
    }

    override fun onClick(v: View) {
        Album.destroy()
        when (v.id) {
            R.id.btn_day_album -> {
                onDayAlbumClick().start(this,
                        AlbumBundle(filterImg = true, checkBoxDrawable = R.drawable.simple_selector_album_item_check),
                        AlbumActivity::class.java)
            }
            R.id.btn_night_album -> {
                onNightClick().start(this,
                        AlbumBundle(
                                checkBoxDrawable = R.drawable.simple_selector_album_item_check, radio = true,
                                cameraPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM/Album",
                                uCropPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM" + "/" + "uCrop",
                                cameraTextColor = R.color.colorAlbumContentViewTipsColorNight,
                                cameraDrawable = R.drawable.ic_camera_drawable,
                                cameraDrawableColor = R.color.colorAlbumContentViewCameraDrawableColorNight,
                                cameraBackgroundColor = R.color.colorAlbumContentViewBackgroundColorNight,
                                rootViewBackground = R.color.colorAlbumContentViewBackgroundNight,
                                photoEmptyDrawable = R.drawable.ic_camera_drawable,
                                photoEmptyDrawableColor = R.color.colorAlbumContentEmptyDrawableColorNight,
                                cameraCrop = true),
                        AlbumUiBundle(
                                statusBarColor = R.color.colorAlbumStatusBarColorNight,
                                toolbarBackground = R.color.colorAlbumToolbarBackgroundNight,
                                toolbarIconColor = R.color.colorAlbumToolbarIconColorNight,
                                toolbarTextColor = R.color.colorAlbumToolbarTextColorNight,
                                bottomFinderTextBackground = R.color.colorAlbumBottomViewBackgroundNight,
                                bottomFinderTextColor = R.color.colorAlbumBottomFinderTextColorNight,
                                bottomFinderTextDrawableColor = R.color.colorAlbumBottomFinderTextDrawableColorNight,
                                bottomPreViewTextColor = R.color.colorAlbumBottomPreViewTextColorNight,
                                bottomSelectTextColor = R.color.colorAlbumBottomSelectTextColorNight,
                                listPopupBackground = R.color.colorAlbumListPopupBackgroundNight,
                                listPopupItemTextColor = R.color.colorAlbumListPopupItemTextColorNight,
                                previewBackground = R.color.colorAlbumPreviewBackgroundNight,
                                previewBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundNight,
                                previewBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorNight,
                                previewBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorNight),
                        AlbumActivity::class.java)
            }
            R.id.btn_sample_ui -> {
                onSimpleUi().start(this, SimpleAlbumUI::class.java)
            }
            R.id.btn_open_camera -> {
                openCamera()
            }
            R.id.btn_dialog -> {
                dialog()
            }
            R.id.btn_customize_camera -> {
                customizeCamera().start(this, AlbumActivity::class.java)
            }
            R.id.btn_video -> {
                openVideo().start(this, AlbumBundle(
                        scanType = VIDEO, cameraText = R.string.video_tips),
                        AlbumUiBundle(toolbarText = R.string.album_video_title), AlbumActivity::class.java)
            }
            R.id.btn_subsampling -> {
                onSubsampling().start(this, AlbumActivity::class.java)
            }
            R.id.btn_wechat_ui -> {
                onWeChatUI().start(this, AlbumWeChatUiActivity::class.java)
            }
            R.id.btn_imageloader -> {
                AlertDialog.Builder(this@MainActivity)
                        .setSingleChoiceItems(arrayOf("Glide", "ImageLoader", "Fresco", "Picasso"), 0
                        ) { dialog, which ->
                            when (which) {
                                0 -> onImageLoader(SimpleAlbumImageLoader())
                                1 -> onImageLoader(SimpleImageLoaderAlbumImageLoader())
                                2 -> onImageLoader(SimpleFrescoAlbumImageLoader())
                                3 -> onImageLoader(SimplePicassoAlbumImageLoader())
                            }
                            dialog.dismiss()
                        }.show()
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
                ITEM_CAMERA -> {
                    SingleMediaScanner(this, pathToFile(imagePath.path ?: ""),
                            this, TYPE_RESULT_CAMERA)
                    UCrop.of(Uri.fromFile(File(imagePath.path)), imagePath)
                            .withOptions(UCrop.Options())
                            .start(this)
                }
                UCrop.REQUEST_CROP -> {
                    SingleMediaScanner(this, pathToFile(imagePath.path ?: ""),
                            this, TYPE_RESULT_CROP)
                    Toast.makeText(applicationContext, imagePath.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
