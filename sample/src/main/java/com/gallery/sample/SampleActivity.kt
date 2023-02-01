package com.gallery.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gallery.compat.Gallery
import com.gallery.compat.internal.call.GalleryResultCallback
import com.gallery.core.entity.ScanEntity
import com.gallery.sample.callbacks.SimpleGalleryListener
import com.gallery.sample.camera.SimpleMaterialGalleryCameraActivity
import com.gallery.sample.databinding.SimpleActivityMainBinding
import com.gallery.ui.material.activity.MaterialGalleryActivity
import com.gallery.ui.wechat.result.WeChatGalleryResultCallback
import com.gallery.ui.wechat.weChatGallery

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
        viewBinding.galleryDefault.setOnClickListener {
            val isCustomCamera = viewBinding.settingConfigsView.customCamera
            val galleryConfigs = viewBinding.settingConfigsView.createGalleryConfigs(selectItems)
            val galleryUiConfig = viewBinding.settingUiConfigsView.createGalleryUiConfig()
            Gallery.newInstance(
                activity = this,
                clz = if (isCustomCamera) SimpleMaterialGalleryCameraActivity::class.java else MaterialGalleryActivity::class.java,
                configs = galleryConfigs,
                gap = galleryUiConfig,
                launcher = galleryLauncher
            )
        }
    }

}
