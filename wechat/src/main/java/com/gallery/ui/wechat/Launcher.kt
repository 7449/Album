package com.gallery.ui.wechat

import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.Gallery
import com.gallery.core.args.CameraConfig
import com.gallery.core.args.GalleryConfigs
import com.gallery.ui.wechat.activity.WeChatGalleryActivity
import com.gallery.ui.wechat.args.WeChatGalleryConfig

internal val scanType = arrayOf(
    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
)

internal val checkBoxResource = R.drawable.wechat_gallery_selector_gallery_item_check

internal val rgb19 = Color.rgb(19, 19, 19)

internal val rgb38 = Color.rgb(38, 38, 38)

fun FragmentActivity.weChatGallery(launcher: ActivityResultLauncher<Intent>) {
    Gallery.newInstance(
        activity = this,
        launcher = launcher,
        gap = WeChatGalleryConfig(),
        configs = GalleryConfigs(
            sdNameAndAllName = "根目录" to "图片和视频",
            hideCamera = true,
            type = scanType,
            cameraConfig = CameraConfig(checkBoxIcon = checkBoxResource),
        ),
        clz = WeChatGalleryActivity::class.java
    )
}