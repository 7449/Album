@file:Suppress("FunctionName")

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
import com.album.core.Album
import com.album.core.AlbumBundle
import com.album.core.AlbumCameraConst
import com.album.core.AlbumConst
import com.album.core.action.AlbumImageLoader
import com.album.core.ext.albumPathFile
import com.album.core.ext.openCamera
import com.album.core.ext.permission.permissionCamera
import com.album.core.ext.permission.permissionStorage
import com.album.core.ext.uri
import com.album.sample.camera.SimpleCameraActivity
import com.album.sample.imageloader.SimpleGlideImageLoader
import com.album.sample.imageloader.SimplePicassoAlbumImageLoader
import com.album.sample.imageloader.SimpleSubsamplingScaleImageLoader
import com.gallery.scan.ScanEntity
import com.gallery.scan.SingleMediaScanner
import com.gallery.scan.args.ScanConst
import com.album.ui.AlbumUiBundle
import com.album.ui.ui
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

fun NightAlbumBundle(): AlbumBundle {
    return AlbumBundle(
            spanCount = 4,
            cropFinish = false,
            checkBoxDrawable = R.drawable.simple_selector_album_item_check,
            radio = true,
            cameraPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM/Album",
            uCropPath = Environment.getExternalStorageDirectory().path + "/" + "DCIM" + "/" + "uCrop",
            cameraTextColor = R.color.colorAlbumContentViewTipsColorNight,
            cameraDrawable = R.drawable.ic_camera_drawable,
            cameraDrawableColor = R.color.colorAlbumContentViewCameraDrawableColorNight,
            cameraBackgroundColor = R.color.colorAlbumToolbarBackgroundNight,
            rootViewBackground = R.color.colorAlbumContentViewBackgroundNight,
            cameraCrop = true)
}

fun NightAlbumUIBundle(): AlbumUiBundle {
    return AlbumUiBundle(
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
            preBackground = R.color.colorAlbumPreviewBackgroundNight,
            preBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundNight,
            preBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorNight,
            preBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorNight)
}

fun MainActivity.dayAlbum() {
    Album.instance.apply {
        albumImageLoader = SimplePicassoAlbumImageLoader()
        albumListener = MainAlbumListener(applicationContext, list)
        selectList = list
        options = dayOptions
    }.ui(this,
            AlbumBundle(scanType = ScanConst.IMAGE, checkBoxDrawable = R.drawable.simple_selector_album_item_check))
}

fun MainActivity.nightAlbum() {
    Album.instance.apply {
        albumListener = MainAlbumListener(applicationContext, null)
        options = nightOptions
        albumImageLoader = SimplePicassoAlbumImageLoader()
        customCameraListener = {
            if (it.permissionStorage() && it.permissionCamera()) {
                Toast.makeText(it.activity, "camera", Toast.LENGTH_SHORT).show()
                val intent = Intent(it.activity, SimpleCameraActivity::class.java)
                it.startActivityForResult(intent, AlbumCameraConst.CUSTOM_CAMERA_REQUEST_CODE)
            }
        }
    }.ui(this, NightAlbumBundle(), NightAlbumUIBundle())
}

fun MainActivity.video() {
    Album.instance.apply {
        albumImageLoader = SimplePicassoAlbumImageLoader()
        albumListener = MainAlbumListener(applicationContext, null)
    }.ui(this, AlbumBundle(
            scanType = ScanConst.VIDEO,
            cameraText = R.string.video_tips),
            AlbumUiBundle(toolbarText = R.string.album_video_title))
}

fun MainActivity.startCamera() {
    imagePath = uri(applicationContext.albumPathFile(null, System.currentTimeMillis().toString(), "jpg"))
    val i = openCamera(imagePath, false)
    Log.d(javaClass.simpleName, i.toString())
}

fun MainActivity.imageLoader(imageLoader: AlbumImageLoader) {
    Album.instance.apply { albumImageLoader = imageLoader }.ui(this)
}

class SimpleSingleScannerListener : SingleMediaScanner.SingleScannerListener {
    override fun onScanCompleted(type: Int, path: String) {}
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

        btn_day_album.setOnClickListener(this)
        btn_night_album.setOnClickListener(this)
        btn_open_camera.setOnClickListener(this)
        btn_video.setOnClickListener(this)
        btn_imageloader.setOnClickListener(this)

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

    override fun onClick(v: View) {
        Album.destroy()
        when (v.id) {
            R.id.btn_day_album -> dayAlbum()
            R.id.btn_night_album -> nightAlbum()
            R.id.btn_open_camera -> startCamera()
            R.id.btn_video -> video()
            R.id.btn_imageloader -> AlertDialog.Builder(this@MainActivity)
                    .setSingleChoiceItems(arrayOf("Glide", "Picasso", "SubsamplingScale"), -1
                    ) { dialog, which ->
                        when (which) {
                            0 -> imageLoader(SimpleGlideImageLoader())
                            1 -> imageLoader(SimplePicassoAlbumImageLoader())
                            2 -> imageLoader(SimpleSubsamplingScaleImageLoader())
                        }
                        dialog.dismiss()
                    }.show()
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
                AlbumCameraConst.CAMERA_REQUEST_CODE -> {
                    SingleMediaScanner(this, imagePath.path
                            ?: "", AlbumConst.TYPE_RESULT_CAMERA, SimpleSingleScannerListener())
                }
                UCrop.REQUEST_CROP -> {
                    SingleMediaScanner(this, imagePath.path
                            ?: "", AlbumConst.TYPE_RESULT_CROP, SimpleSingleScannerListener())
                    Toast.makeText(applicationContext, imagePath.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
