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
import com.album.Album
import com.album.AlbumBundle
import com.album.TYPE_RESULT_CAMERA
import com.album.TYPE_RESULT_CROP
import com.album.core.*
import com.album.core.AlbumCamera.CUSTOMIZE_CAMERA_REQUEST_CODE
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.scan.AlbumSingleMediaScanner
import com.album.core.ui.AlbumBaseFragment
import com.album.listener.AlbumImageLoader
import com.album.listener.OnAlbumCustomCameraListener
import com.album.listener.OnAlbumEmptyClickListener
import com.album.listener.SimpleAlbumImageLoader
import com.album.sample.camera.SimpleCameraActivity
import com.album.sample.imageloader.*
import com.album.ui.AlbumUiBundle
import com.album.ui.activity.AlbumActivity
import com.album.ui.dialog.dialog
import com.album.ui.ui
import com.album.ui.wechat.activity.AlbumWeChatUiActivity
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
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
            photoEmptyDrawable = R.drawable.ic_camera_drawable,
            photoEmptyDrawableColor = R.color.colorAlbumContentEmptyDrawableColorNight,
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
            previewBackground = R.color.colorAlbumPreviewBackgroundNight,
            previewBottomViewBackground = R.color.colorAlbumPreviewBottomViewBackgroundNight,
            previewBottomOkTextColor = R.color.colorAlbumPreviewBottomViewOkColorNight,
            previewBottomCountTextColor = R.color.colorAlbumPreviewBottomViewCountColorNight)
}

fun MainActivity.dayAlbum() {
    Album.instance.apply {
        albumImageLoader = SimpleAlbumImageLoader()
        albumListener = MainAlbumListener(applicationContext, list)
        initList = list
        options = dayOptions
        albumEmptyClickListener = SimpleOnAlbumEmptyClickListener()
    }.ui(this,
            AlbumBundle(scanCount = 200,
                    scanType = AlbumScan.IMAGE,
                    checkBoxDrawable = R.drawable.simple_selector_album_item_check),
            AlbumActivity::class.java)
}

fun MainActivity.nightAlbum() {
    Album.instance.apply {
        albumListener = MainAlbumListener(applicationContext, null)
        options = nightOptions
        albumImageLoader = SimpleAlbumImageLoader()
        albumEmptyClickListener = SimpleOnAlbumEmptyClickListener()
        customCameraListener = object : OnAlbumCustomCameraListener {
            override fun openCustomCamera(fragment: AlbumBaseFragment) {
                if (fragment.permissionStorage() && fragment.permissionCamera()) {
                    val activity = fragment.activity
                    Toast.makeText(activity, "camera", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, SimpleCameraActivity::class.java)
                    fragment.startActivityForResult(intent, CUSTOMIZE_CAMERA_REQUEST_CODE)
                }
            }
        }
    }.ui(this, NightAlbumBundle(), NightAlbumUIBundle(), AlbumActivity::class.java)
}

fun MainActivity.dialog() {
    Album.instance.apply {
        albumListener = MainAlbumListener(applicationContext, null)
        albumImageLoader = SimpleFrescoAlbumImageLoader()
        options = dayOptions
    }.dialog(AlbumBundle(
            cropFinish = false,
            selectImageFinish = false,
            cropErrorFinish = false), supportFragmentManager)
}

fun MainActivity.video() {
    Album.instance.apply {
        albumImageLoader = SimpleAlbumImageLoader()
        albumListener = MainAlbumListener(applicationContext, null)
        albumEmptyClickListener = SimpleOnAlbumEmptyClickListener()
    }.ui(this, AlbumBundle(
            scanType = AlbumScan.VIDEO,
            cameraText = R.string.video_tips),
            AlbumUiBundle(toolbarText = R.string.album_video_title),
            AlbumActivity::class.java)
}

fun MainActivity.wechat() {
    Album.instance.apply {
        albumImageLoader = SimpleAlbumWeChatImageLoader()
        albumListener = MainAlbumListener(applicationContext, list)
        albumEmptyClickListener = SimpleOnAlbumEmptyClickListener()
    }.ui(this, AlbumBundle(
            scanType = AlbumScan.MIXING,
            spanCount = 4,
            dividerWidth = 5,
            photoBackgroundColor = R.color.colorAlbumContentEmptyDrawableColor,
            hideCamera = true,
            checkBoxDrawable = R.drawable.simple_selector_wechat_item_check),
            AlbumWeChatUiActivity::class.java)
}

fun MainActivity.startCamera() {
    imagePath = Uri.fromFile(applicationContext.getCameraFile(null, false))
    val i = openCamera(imagePath, false)
    Log.d(javaClass.simpleName, i.toString())
}

fun MainActivity.imageLoader(imageLoader: AlbumImageLoader) {
    Album.instance.apply { albumImageLoader = imageLoader }.ui(this, AlbumActivity::class.java)
}

class SimpleOnAlbumEmptyClickListener : OnAlbumEmptyClickListener {
    override fun onAlbumClick(view: View): Boolean = true
}

class SimpleSingleScannerListener : AlbumSingleMediaScanner.SingleScannerListener {
    override fun onScanCompleted(type: Int, path: String) {}
    override fun onScanStart() {}
}

class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var dayOptions: UCrop.Options
    lateinit var nightOptions: UCrop.Options
    lateinit var list: ArrayList<AlbumEntity>
    lateinit var imagePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_day_album.setOnClickListener(this)
        btn_night_album.setOnClickListener(this)
        btn_open_camera.setOnClickListener(this)
        btn_dialog.setOnClickListener(this)
        btn_video.setOnClickListener(this)
        btn_imageloader.setOnClickListener(this)
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

    override fun onClick(v: View) {
        Album.destroy()
        when (v.id) {
            R.id.btn_day_album -> dayAlbum()
            R.id.btn_night_album -> nightAlbum()
            R.id.btn_open_camera -> startCamera()
            R.id.btn_dialog -> dialog()
            R.id.btn_video -> video()
            R.id.btn_wechat_ui -> wechat()
            R.id.btn_imageloader -> AlertDialog.Builder(this@MainActivity)
                    .setSingleChoiceItems(arrayOf("Glide", "ImageLoader", "Fresco", "Picasso", "SubsamplingScale"), -1
                    ) { dialog, which ->
                        when (which) {
                            0 -> imageLoader(SimpleAlbumImageLoader())
                            1 -> imageLoader(SimpleImageLoaderAlbumImageLoader())
                            2 -> imageLoader(SimpleFrescoAlbumImageLoader())
                            3 -> imageLoader(SimplePicassoAlbumImageLoader())
                            4 -> imageLoader(SimpleSubsamplingScaleImageLoader())
                        }
                        dialog.dismiss()
                    }.show()
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
                AlbumCamera.OPEN_CAMERA_REQUEST_CODE -> {
                    AlbumSingleMediaScanner(this, imagePath.path.orEmpty(), SimpleSingleScannerListener(), TYPE_RESULT_CAMERA)
                    UCrop.of(Uri.fromFile(File(imagePath.path)), imagePath)
                            .withOptions(UCrop.Options())
                            .start(this)
                }
                UCrop.REQUEST_CROP -> {
                    AlbumSingleMediaScanner(this, imagePath.path.orEmpty(), SimpleSingleScannerListener(), TYPE_RESULT_CROP)
                    Toast.makeText(applicationContext, imagePath.path, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
