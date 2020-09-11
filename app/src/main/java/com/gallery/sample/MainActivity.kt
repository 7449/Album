package com.gallery.sample

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.sample.callback.GalleryCallback
import com.gallery.sample.callback.WeChatGalleryCallback
import com.gallery.sample.custom.CustomCameraActivity
import com.gallery.sample.custom.CustomDialog
import com.gallery.sample.custom.CustomPageActivity
import com.gallery.sample.viewmodel.ScanViewModelTest
import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.scan.types.Sort
import com.gallery.scan.types.externalUriExpand
import com.gallery.ui.FinderType
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryResultCallback
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.crop.CropType
import com.gallery.ui.wechat.WeChatGalleryResultCallback
import com.gallery.ui.wechat.weChatUiGallery
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IGalleryCallback, IGalleryImageLoader {

    private val galleryLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), GalleryResultCallback(GalleryCallback(this)))
    private val galleryWeChatLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(), WeChatGalleryResultCallback(WeChatGalleryCallback(this)))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({ ScanViewModelTest.test(this) }, 2000)
        Handler().postDelayed({ ScanViewModelTest.testAudio(this) }, 2000)
        supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: supportFragmentManager
                .beginTransaction()
                .add(R.id.galleryFragment, ScanFragment.newInstance(GalleryBundle(radio = true, hideCamera = true, crop = false, galleryRootBackground = Color.BLACK)), ScanFragment::class.java.simpleName)
                .commitAllowingStateLoss()

        selectTheme.setOnClickListener {
            AlertDialog.Builder(this).setSingleChoiceItems(arrayOf("默认", "主题色", "蓝色", "黑色", "粉红色", "WeChat"), View.NO_ID) { dialog, which ->
                when (which) {
                    0 -> Gallery.newInstance(this, galleryBundle = GalleryTheme.themeGallery(this, Theme.DEFAULT), galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.DEFAULT), galleryLauncher = galleryLauncher)
                    1 -> Gallery.newInstance(this, galleryBundle = GalleryTheme.themeGallery(this, Theme.APP), galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.APP), galleryLauncher = galleryLauncher)
                    2 -> Gallery.newInstance(this, galleryBundle = GalleryTheme.themeGallery(this, Theme.BLUE), galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.BLUE), galleryLauncher = galleryLauncher)
                    3 -> Gallery.newInstance(this, galleryBundle = GalleryTheme.themeGallery(this, Theme.BLACK), galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.BLACK), galleryLauncher = galleryLauncher)
                    4 -> Gallery.newInstance(this, galleryBundle = GalleryTheme.themeGallery(this, Theme.PINK), galleryUiBundle = GalleryTheme.themeGalleryUi(this, Theme.PINK), galleryLauncher = galleryLauncher)
                    5 -> weChatUiGallery(galleryWeChatLauncher)
                }
                dialog.dismiss()
            }.show()
        }
        selectSort.setOnClickListener {
            AlertDialog.Builder(this).setSingleChoiceItems(arrayOf("DESC", "ASC"), View.NO_ID) { dialog, which ->
                when (which) {
                    0 -> Gallery.newInstance(activity = this, galleryBundle = GalleryBundle(scanSort = Sort.DESC), galleryLauncher = galleryLauncher)
                    1 -> Gallery.newInstance(activity = this, galleryBundle = GalleryBundle(scanSort = Sort.ASC), galleryLauncher = galleryLauncher)
                }
                dialog.dismiss()
            }.show()
        }
        selectCrop.setOnClickListener {
            AlertDialog.Builder(this).setSingleChoiceItems(arrayOf("CROPPER", "UCROP"), View.NO_ID) { dialog, which ->
                when (which) {
                    0 -> Gallery.newInstance(
                            activity = this,
                            galleryBundle = GalleryTheme.cropThemeGallery(this),
                            galleryUiBundle = GalleryTheme.cropThemeGalleryUi(this, CropType.CROPPER),
                            galleryLauncher = galleryLauncher
                    )
                    1 -> Gallery.newInstance(
                            activity = this,
                            galleryBundle = GalleryTheme.cropThemeGallery(this),
                            galleryUiBundle = GalleryTheme.cropThemeGalleryUi(this, CropType.UCROP),
                            galleryLauncher = galleryLauncher
                    )
                }
                dialog.dismiss()
            }.show()
        }
        customCamera.setOnClickListener {
            Gallery.newInstance(
                    activity = this,
                    galleryLauncher = galleryLauncher,
                    clz = CustomCameraActivity::class.java
            )
        }
        selectFinderStyle.setOnClickListener {
            AlertDialog.Builder(this).setSingleChoiceItems(arrayOf("popup", "bottom"), View.NO_ID) { dialog, which ->
                when (which) {
                    0 -> Gallery.newInstance(this, galleryUiBundle = GalleryUiBundle(finderType = FinderType.POPUP), galleryLauncher = galleryLauncher)
                    1 -> Gallery.newInstance(this, galleryUiBundle = GalleryUiBundle(finderType = FinderType.BOTTOM), galleryLauncher = galleryLauncher)
                }
                dialog.dismiss()
            }.show()
        }
        video.setOnClickListener {
            Gallery.newInstance(
                    activity = this,
                    galleryBundle = GalleryBundle(scanType = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO), cameraText = getString(R.string.video_tips)),
                    galleryUiBundle = GalleryUiBundle(toolbarText = getString(R.string.gallery_video_title)),
                    galleryLauncher = galleryLauncher
            )
        }
        dialog.setOnClickListener {
            CustomDialog.newInstance().show(supportFragmentManager, CustomDialog::class.java.simpleName)
        }
        customActivity.setOnClickListener {
            Gallery.newInstance(
                    activity = this,
                    galleryLauncher = galleryLauncher,
                    clz = CustomPageActivity::class.java,
                    galleryBundle = GalleryBundle(radio = true, crop = true)
            )
        }
    }

    override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanMinimumEntity, container: FrameLayout, selectView: TextView) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context)
                .load(galleryEntity.externalUriExpand)
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_gallery_default_loading)
                        .error(R.drawable.ic_gallery_default_loading)
                        .centerCrop()
                        .override(width, height))
                .into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanMinimumEntity) {
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanMinimumEntity, position: Int, parentId: Long) {
    }
}
