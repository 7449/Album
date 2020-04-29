package com.gallery.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaScannerConnection.scanFile
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.kotlin.expand.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.galleryPathFile
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanType
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryUiBundle
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IGalleryCallback, IGalleryImageLoader {

    private var fileUri = Uri.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.galleryFragment, ScanFragment.newInstance(GalleryBundle(radio = true, hideCamera = true, crop = false, galleryRootBackground = Color.BLACK)), ScanFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment).commitAllowingStateLoss()
        }

        customActivity.setOnClickListener {
            Gallery.open(this, GalleryBundle(radio = true, crop = true), GalleryUiBundle(), SimpleGalleryActivity::class.java)
        }
        dialog.setOnClickListener {
            GalleryDialogFragment.newInstance().show(supportFragmentManager, GalleryDialogFragment::class.java.simpleName)
        }
        openCamera.setOnClickListener {
            fileUri = findUriByFileExpand(applicationContext.galleryPathFile(null, System.currentTimeMillis().toString()))
            openCameraExpand(fileUri, false)
        }
        video.setOnClickListener {
            Gallery.open(this,
                    GalleryBundle(
                            scanType = ScanType.VIDEO,
                            cameraText = getString(R.string.video_tips)
                    ),
                    GalleryUiBundle(
                            toolbarText = getString(R.string.gallery_video_title)
                    ))
                    .callback(SimpleGalleryCallback())
        }
        selectCrop.setOnClickListener {
            Gallery.open(this,
                    GalleryTheme.cropThemeGallery(this),
                    GalleryTheme.cropThemeGalleryUi(this))
                    .callback(SimpleGalleryCallback())
        }
        selectTheme.setOnClickListener {
            AlertDialog.Builder(this).setSingleChoiceItems(arrayOf("默认", "主题色", "蓝色", "黑色", "粉红色"), View.NO_ID) { dialog, which ->
                when (which) {
                    0 -> Gallery.open(this, GalleryTheme.themeGallery(this, Theme.DEFAULT), GalleryTheme.themeGalleryUi(this, Theme.DEFAULT)).callback(SimpleGalleryCallback())
                    1 -> Gallery.open(this, GalleryTheme.themeGallery(this, Theme.APP), GalleryTheme.themeGalleryUi(this, Theme.APP)).callback(SimpleGalleryCallback())
                    2 -> Gallery.open(this, GalleryTheme.themeGallery(this, Theme.BLUE), GalleryTheme.themeGalleryUi(this, Theme.BLUE)).callback(SimpleGalleryCallback())
                    3 -> Gallery.open(this, GalleryTheme.themeGallery(this, Theme.BLACK), GalleryTheme.themeGalleryUi(this, Theme.BLACK)).callback(SimpleGalleryCallback())
                    4 -> Gallery.open(this, GalleryTheme.themeGallery(this, Theme.PINK), GalleryTheme.themeGalleryUi(this, Theme.PINK)).callback(SimpleGalleryCallback())
                }
                dialog.dismiss()
            }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_CANCELED ->
                when (requestCode) {
                    UCrop.REQUEST_CROP -> "取消裁剪".toastExpand(this)
                    CameraX.CAMERA_REQUEST_CODE -> "取消拍照".toastExpand(this)
                }
            UCrop.RESULT_ERROR -> "裁剪异常".toastExpand(this)
            Activity.RESULT_OK ->
                when (requestCode) {
                    CameraX.CAMERA_REQUEST_CODE -> {
                        findPathByUriExpand(fileUri)?.let {
                            scanFile(this, arrayOf(it), null) { path: String?, _: Uri? ->
                                runOnUiThread {
                                    path?.toastExpand(this)
                                }
                            }
                        }
//                        openCrop(fileUri)
                    }
                    UCrop.REQUEST_CROP -> {
                        scanFile(this, arrayOf(data?.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path), null) { _: String?, uri: Uri? ->
                            runOnUiThread {
                                data?.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path?.toastExpand(this)
                            }
                        }
                    }
                }
        }
    }

    override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context)
                .load(galleryEntity.externalUri())
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_gallery_default_loading)
                        .error(R.drawable.ic_gallery_default_loading)
                        .centerCrop()
                        .override(width, height))
                .into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
    }

    override fun onPhotoItemClick(context: Context, scanEntity: ScanEntity, position: Int, parentId: Long) {
        scanEntity.toString().toastExpand(context)
    }
}
