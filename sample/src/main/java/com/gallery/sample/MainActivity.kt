package com.gallery.sample

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.gallery.compat.Gallery
import com.gallery.compat.internal.call.GalleryResultCallback
import com.gallery.core.extensions.isVideoScanExpand
import com.gallery.core.extensions.safeToastExpand
import com.gallery.sample.callback.GalleryCallback
import com.gallery.sample.callback.WeChatGalleryCallback
import com.gallery.sample.custom.CustomCameraActivity
import com.gallery.sample.custom.CustomDialog
import com.gallery.sample.custom.UCropGalleryActivity
import com.gallery.sample.databinding.ActivityMainBinding
import com.gallery.scan.Types
import com.gallery.ui.material.activity.GalleryActivity
import com.gallery.ui.wechat.result.WeChatGalleryResultCallback
import com.gallery.ui.wechat.weChatGallery

class MainActivity : AppCompatActivity() {

    private val galleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            GalleryResultCallback(GalleryCallback(this))
        )
    private val galleryWeChatLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            WeChatGalleryResultCallback(WeChatGalleryCallback(this))
        )
    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        var isWeChat = false
        var isRadio = false
        var cls: Class<*>? = null
        var galleryBundle = GalleryTheme.themeGallery(this, Theme.DEFAULT)
        var galleryUiBundle = GalleryTheme.themeGalleryCompat(this, Theme.DEFAULT)
        var galleryArgsBundle = GalleryTheme.themeGalleryArgs(this, Theme.DEFAULT)
        var scanArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        var sortType = Types.Sort.DESC

        viewBinding.includeTheme.themeRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.theme_default -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.DEFAULT)
                    galleryUiBundle = GalleryTheme.themeGalleryCompat(this, Theme.DEFAULT)
                    galleryArgsBundle = GalleryTheme.themeGalleryArgs(this, Theme.DEFAULT)
                }
                R.id.theme_app -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.APP)
                    galleryUiBundle = GalleryTheme.themeGalleryCompat(this, Theme.APP)
                    galleryArgsBundle = GalleryTheme.themeGalleryArgs(this, Theme.APP)
                }
                R.id.theme_blue -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.BLUE)
                    galleryUiBundle = GalleryTheme.themeGalleryCompat(this, Theme.BLUE)
                    galleryArgsBundle = GalleryTheme.themeGalleryArgs(this, Theme.BLUE)
                }
                R.id.theme_black -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.BLACK)
                    galleryUiBundle = GalleryTheme.themeGalleryCompat(this, Theme.BLACK)
                    galleryArgsBundle = GalleryTheme.themeGalleryArgs(this, Theme.BLACK)
                }
                R.id.theme_pink -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.PINK)
                    galleryUiBundle = GalleryTheme.themeGalleryCompat(this, Theme.PINK)
                    galleryArgsBundle = GalleryTheme.themeGalleryArgs(this, Theme.PINK)
                }
                R.id.theme_wechat -> {
                    isWeChat = true
                    "其他选项失效".safeToastExpand(this)
                }
            }
        }
        viewBinding.includeScan.scanRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.scan_image -> scanArray =
                    intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                R.id.scan_video -> scanArray =
                    intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                R.id.scan_mix -> scanArray = intArrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                )
            }
        }
        viewBinding.includeSetting.settingRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.single_select -> {
                    cls = null
                    isRadio = true
                }
                R.id.crop_no -> {
                    cls = null
                    isRadio = false
                }
                R.id.crop_cropper -> {
                    cls = null
                    isRadio = false
                }
                R.id.crop_ucrop -> {
                    cls = UCropGalleryActivity::class.java
                    isRadio = false
                }
            }
        }
        viewBinding.includeSort.sortRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.sort_desc -> sortType = Types.Sort.DESC
                R.id.sort_asc -> sortType = Types.Sort.ASC
            }
        }
        viewBinding.startConfig.setOnClickListener {
            if (isWeChat) {
                weChatGallery(launcher = galleryWeChatLauncher)
                return@setOnClickListener
            }
            val newBundle = galleryBundle.copy(scanType = scanArray)
            Gallery.newInstance(
                activity = this,
                clz = cls ?: GalleryActivity::class.java,
                bundle = newBundle.copy(
                    scanType = scanArray,
                    cameraText = if (newBundle.isVideoScanExpand) "摄像" else "拍照",
                    scanSort = sortType,
                    crop = viewBinding.includeSetting.cropCropper.isChecked || viewBinding.includeSetting.cropUcrop.isChecked,
                    radio = isRadio || viewBinding.includeSetting.cropCropper.isChecked || viewBinding.includeSetting.cropUcrop.isChecked,
                    cameraNameSuffix = if (newBundle.isVideoScanExpand) "mp4" else "jpg"
                ),
                compatBundle = galleryUiBundle,
                customBundle = galleryArgsBundle.copy(
                    toolbarText = if (newBundle.isVideoScanExpand) "视频选择" else "图片选择"
                ),
                launcher = galleryLauncher
            )
        }
        viewBinding.customCamera.setOnClickListener {
            Gallery.newInstance(
                activity = this,
                launcher = galleryLauncher,
                clz = CustomCameraActivity::class.java
            )
        }
        viewBinding.dialog.setOnClickListener {
            CustomDialog.newInstance()
                .show(supportFragmentManager, CustomDialog::class.java.simpleName)
        }
    }

}
