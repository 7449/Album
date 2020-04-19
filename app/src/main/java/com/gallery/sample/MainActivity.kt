package com.gallery.sample

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection.scanFile
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.CameraStatus
import com.gallery.core.GalleryBundle
import com.gallery.core.PermissionCode
import com.gallery.core.callback.IGallery
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.ext.*
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
                    .add(R.id.galleryFragment, ScanFragment.newInstance(GalleryBundle(radio = true, hideCamera = true)), ScanFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().show(supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment).commitAllowingStateLoss()
        }

        dialog.setOnClickListener {
            GalleryDialogFragment.newInstance().show(supportFragmentManager, GalleryDialogFragment::class.java.simpleName)
        }
        openCamera.setOnClickListener {
            fileUri = findUriByFile(applicationContext.galleryPathFile(null, System.currentTimeMillis().toString()))
            openCamera(fileUri, false)
        }
        video.setOnClickListener {
            Gallery.ui(this,
                    GalleryBundle(
                            scanType = ScanType.VIDEO,
                            cameraText = getString(R.string.video_tips)
                    ),
                    GalleryUiBundle(
                            toolbarText = getString(R.string.gallery_video_title)
                    ))
        }
        selectCrop.setOnClickListener {
            Gallery.ui(this,
                    GalleryTheme.cropThemeGallery(this),
                    GalleryTheme.cropThemeGalleryUi(this))
        }
        selectTheme.setOnClickListener {
            AlertDialog.Builder(this).setSingleChoiceItems(arrayOf("默认", "主题色", "蓝色", "黑色", "粉红色"), View.NO_ID) { dialog, which ->
                when (which) {
                    0 -> Gallery.ui(this, GalleryTheme.themeGallery(this, Theme.DEFAULT), GalleryTheme.themeGalleryUi(this, Theme.DEFAULT))
                    1 -> Gallery.ui(this, GalleryTheme.themeGallery(this, Theme.APP), GalleryTheme.themeGalleryUi(this, Theme.APP))
                    2 -> Gallery.ui(this, GalleryTheme.themeGallery(this, Theme.BLUE), GalleryTheme.themeGalleryUi(this, Theme.BLUE))
                    3 -> Gallery.ui(this, GalleryTheme.themeGallery(this, Theme.BLACK), GalleryTheme.themeGalleryUi(this, Theme.BLACK))
                    4 -> Gallery.ui(this, GalleryTheme.themeGallery(this, Theme.PINK), GalleryTheme.themeGalleryUi(this, Theme.PINK))
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
                    UCrop.REQUEST_CROP -> "取消裁剪".show(this)
                    IGallery.CAMERA_REQUEST_CODE -> "取消拍照".show(this)
                }
            UCrop.RESULT_ERROR -> "裁剪异常".show(this)
            Activity.RESULT_OK ->
                when (requestCode) {
                    IGallery.CAMERA_REQUEST_CODE -> {
                        uriToFilePath(fileUri)?.let {
                            scanFile(this, arrayOf(it), null) { path: String?, _: Uri? ->
                                runOnUiThread {
                                    path?.show(this)
                                }
                            }
                        }
//                        openCrop(fileUri)
                    }
                    UCrop.REQUEST_CROP -> {
                        scanFile(this, arrayOf(data?.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path), null) { _: String?, uri: Uri? ->
                            runOnUiThread {
                                data?.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path?.show(this)
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

    override fun onGalleryResource(scanEntities: ArrayList<ScanEntity>) {
    }

    override fun onClickCheckBoxFileNotExist() {
    }

    override fun onClickCheckBoxMaxCount() {
    }

    override fun onClickItemFileNotExist() {
    }

    override fun onChangedCheckBox(isSelect: Boolean, scanEntity: ScanEntity) {
    }

    override fun onChangedScreen(selectCount: Int) {
    }

    override fun onChangedPrevCount(selectCount: Int) {
    }

    override fun onPhotoItemClick(selectEntities: ArrayList<ScanEntity>, position: Int, parentId: Long) {
    }

    override fun onScanResultSuccess(scanEntity: ScanEntity) {
    }

    override fun onCameraCanceled() {
    }

    override fun onCameraResultError() {
    }

    override fun onCameraOpenStatus(status: CameraStatus) {
    }

    override fun onScanSuccessEmpty() {
    }

    override fun onOpenVideoPlayError() {
    }

    override fun onPermissionsDenied(type: PermissionCode) {
    }

}
