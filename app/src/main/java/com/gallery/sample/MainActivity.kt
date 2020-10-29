package com.gallery.sample

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.text.safeToastExpand
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.isVideoScanExpand
import com.gallery.sample.callback.GalleryCallback
import com.gallery.sample.callback.WeChatGalleryCallback
import com.gallery.sample.custom.CustomCameraActivity
import com.gallery.sample.custom.CustomDialog
import com.gallery.sample.custom.UCropGalleryActivity
import com.gallery.scan.types.Sort
import com.gallery.ui.Gallery
import com.gallery.ui.activity.GalleryActivity
import com.gallery.ui.fragment.ScanFragment
import com.gallery.ui.result.GalleryResultCallback
import com.gallery.ui.wechat.WeChatGalleryResultCallback
import com.gallery.ui.wechat.weChatUiGallery
import com.gallery.ui.widget.GalleryDivider
import com.gallery.ui.widget.GalleryImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_scan_rb.*
import kotlinx.android.synthetic.main.layout_setting_rb.*
import kotlinx.android.synthetic.main.layout_sort_rb.*
import kotlinx.android.synthetic.main.layout_theme_rb.*

class MainActivity : AppCompatActivity(R.layout.activity_main), IGalleryCallback, IGalleryImageLoader {

    private val galleryLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), GalleryResultCallback(GalleryCallback(this)))
    private val galleryWeChatLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), WeChatGalleryResultCallback(WeChatGalleryCallback(this)))

    private fun initGalleryFragment() {
        supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: supportFragmentManager
                .beginTransaction()
                .add(R.id.galleryFragment, ScanFragment.newInstance(GalleryBundle(
                        radio = true,
                        hideCamera = true,
                        crop = false,
                )), ScanFragment::class.java.simpleName)
                .commitAllowingStateLoss()
    }

    override fun onGalleryCreated(fragment: Fragment, recyclerView: RecyclerView, galleryBundle: GalleryBundle, savedInstanceState: Bundle?) {
        fragment.view?.setBackgroundColor(Color.BLACK)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, RecyclerView.HORIZONTAL, false)
        recyclerView.addItemDecoration(GalleryDivider(10))
    }

    companion object {
        val scanArrayList = arrayOf("文件", "音频", "视频", "图片", "音视图")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGalleryFragment()

        var isWeChat = false
        var isRadio = false
        var cls: Class<*>? = null
        var galleryBundle = GalleryTheme.themeGallery(this, Theme.DEFAULT)
        var galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.DEFAULT)
        var scanArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        var sortType = Sort.DESC

        themeRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.theme_default -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.DEFAULT)
                    galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.DEFAULT)
                }
                R.id.theme_app -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.APP)
                    galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.APP)
                }
                R.id.theme_blue -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.BLUE)
                    galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.BLUE)
                }
                R.id.theme_black -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.BLACK)
                    galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.BLACK)
                }
                R.id.theme_pink -> {
                    isWeChat = false
                    galleryBundle = GalleryTheme.themeGallery(this, Theme.PINK)
                    galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.PINK)
                }
                R.id.theme_wechat -> {
                    isWeChat = true
                    "其他选项失效".safeToastExpand(this)
                }
            }
        }
        scanRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.scan_image -> scanArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                R.id.scan_video -> scanArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                R.id.scan_mix -> scanArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            }
        }
        settingRg.setOnCheckedChangeListener { _, i ->
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
        sortRg.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.sort_desc -> sortType = Sort.DESC
                R.id.sort_asc -> sortType = Sort.ASC
            }
        }
        startConfig.setOnClickListener {
            if (isWeChat) {
                weChatUiGallery(galleryWeChatLauncher)
                return@setOnClickListener
            }
            Gallery.newInstance(activity = this,
                    clz = cls ?: GalleryActivity::class.java,
                    galleryBundle = galleryBundle.copy(
                            scanType = scanArray,
                            scanSort = sortType,
                            crop = crop_cropper.isChecked || crop_ucrop.isChecked,
                            radio = isRadio || crop_cropper.isChecked || crop_ucrop.isChecked,
                            cameraNameSuffix = if (scanArray.size == 1 && scanArray.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)) "mp4" else "jpg"
                    ),
                    galleryUiBundle = galleryUiBundle.copy(
                            toolbarText = if (galleryBundle.isVideoScanExpand) getString(R.string.gallery_video_title) else "图片选择",
                    ),
                    galleryLauncher = galleryLauncher)
        }

        scanSimple.setOnClickListener {
            showArray(scanArrayList) {
                when (it) {
                    0 -> startActivity(Intent(this, SimpleScanActivity::class.java).putExtra(SimpleScanActivity.args, ScanType.FILE))
                    1 -> startActivity(Intent(this, SimpleScanActivity::class.java).putExtra(SimpleScanActivity.args, ScanType.AUDIO))
                    2 -> startActivity(Intent(this, SimpleScanActivity::class.java).putExtra(SimpleScanActivity.args, ScanType.VIDEO))
                    3 -> startActivity(Intent(this, SimpleScanActivity::class.java).putExtra(SimpleScanActivity.args, ScanType.PICTURE))
                    4 -> startActivity(Intent(this, SimpleScanActivity::class.java).putExtra(SimpleScanActivity.args, ScanType.MIX))
                }
            }
        }
        customCamera.setOnClickListener {
            Gallery.newInstance(
                    activity = this,
                    galleryLauncher = galleryLauncher,
                    clz = CustomCameraActivity::class.java
            )
        }
        dialog.setOnClickListener {
            CustomDialog.newInstance().show(supportFragmentManager, CustomDialog::class.java.simpleName)
        }
    }

    override fun onDisplayGallery(width: Int, height: Int, scanEntity: ScanEntity, container: FrameLayout, checkBox: TextView) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context)
                .load(scanEntity.uri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_gallery_default_loading)
                        .error(R.drawable.ic_gallery_default_loading)
                        .centerCrop()
                        .override(width, height))
                .into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }
}
