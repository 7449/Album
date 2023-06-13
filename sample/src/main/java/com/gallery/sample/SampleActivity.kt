package com.gallery.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gallery.sample.callbacks.SimpleGalleryListener
import com.gallery.sample.camera.SimpleGalleryCameraActivity
import com.gallery.sample.databinding.SimpleActivityMainBinding
import com.gallery.sample.layout.LayoutActivity
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.compat.Gallery.Companion.startGallery
import develop.file.gallery.compat.extensions.callbacks.GalleryResultCallback
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.material.activity.MaterialGalleryActivity
import develop.file.gallery.ui.material.args.MaterialGalleryConfig
import develop.file.gallery.ui.wechat.result.WeChatGalleryResultCallback
import develop.file.gallery.ui.wechat.weChatGallery

class SampleActivity : GalleryListActivity() {

    private val selectItems = arrayListOf<ScanEntity>()

    private val galleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            GalleryResultCallback(SimpleGalleryListener(this) {
                selectItems.clear()
                selectItems.addAll(it)
                viewBinding.settingConfigsView.updateDefaultSelectItems(selectItems)
            })
        )
    private val galleryWeChatLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            WeChatGalleryResultCallback(SimpleGalleryListener(this))
        )
    private val viewBinding: SimpleActivityMainBinding by lazy {
        SimpleActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewBinding.galleryWechat.setOnClickListener {
            weChatGallery(launcher = galleryWeChatLauncher)
        }
        viewBinding.galleryLayout.setOnClickListener {
            startGallery(
                GalleryConfigs(),
                MaterialGalleryConfig(),
                LayoutActivity::class.java,
                galleryLauncher
            )
        }
        viewBinding.galleryDefault.setOnClickListener {
            val isCustomCamera = viewBinding.settingConfigsView.customCamera
            val galleryConfigs = viewBinding.settingConfigsView.createGalleryConfigs(selectItems)
            val galleryUiConfig = viewBinding.settingUiConfigsView.createGalleryUiConfig()
            startGallery(
                galleryConfigs,
                galleryUiConfig,
                if (isCustomCamera) SimpleGalleryCameraActivity::class.java else MaterialGalleryActivity::class.java,
                galleryLauncher
            )
        }
    }

}
