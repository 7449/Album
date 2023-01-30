package com.gallery.ui.wechat

import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.Gallery
import com.gallery.core.CameraConfig
import com.gallery.core.GalleryConfigs
import com.gallery.ui.wechat.activity.WeChatGalleryActivity
import com.gallery.ui.wechat.args.WeChatGalleryBundle

internal val scanType = intArrayOf(
    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
)

internal val checkBoxResource = R.drawable.wechat_gallery_selector_gallery_item_check

internal val rgb19 = Color.rgb(19, 19, 19)

internal val rgb38 = Color.rgb(38, 38, 38)

fun FragmentActivity.weChatGallery(launcher: ActivityResultLauncher<Intent>) {
    Gallery.newInstance(
        activity = this,
        launcher = launcher,
        gap = WeChatGalleryBundle(
            prevRootBackground = Color.BLACK,
            galleryRootBackground = rgb38,
            videoMaxDuration = 500000,
            videoAllFinderName = "全部视频",
            statusBarColor = rgb38,
            toolbarBackground = rgb38,
            bottomViewBackground = rgb19,
            preBottomViewBackground = rgb38,
            preBottomOkText = "选择",
            preBottomOkTextSize = 14.toFloat(),
            preViewTextSize = 14.toFloat(),
            selectText = "发送",
            selectTextSize = 12.toFloat(),
            finderItemBackground = rgb38,
            finderItemTextColor = Color.WHITE,
            finderItemTextCountColor = Color.parseColor("#767676"),
            finderTextCompoundDrawable = R.drawable.ic_wechat_gallery_finder_action,
            finderTextSize = 14.toFloat()
        ),
        bundle = GalleryConfigs(
            sdNameAndAllName = "根目录" to "图片和视频",
            hideCamera = true,
            type = scanType,
            cameraConfig = CameraConfig(selectIcon = checkBoxResource),
        ),
        clz = WeChatGalleryActivity::class.java
    )
}