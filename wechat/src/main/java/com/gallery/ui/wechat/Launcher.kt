package com.gallery.ui.wechat

import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.Gallery
import com.gallery.compat.GalleryCompatBundle
import com.gallery.core.GalleryBundle
import com.gallery.ui.wechat.activity.GalleryWeChatActivity
import com.gallery.ui.wechat.args.GalleryWeChatBundle

internal val scanType = intArrayOf(
    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
)

internal val checkBoxResource = R.drawable.gallery_wechat_selector_gallery_item_check

internal val rgb19 = Color.rgb(19, 19, 19)

internal val rgb38 = Color.rgb(38, 38, 38)

fun FragmentActivity.weChatGallery(launcher: ActivityResultLauncher<Intent>) {
    Gallery.newInstance(
        activity = this,
        launcher = launcher,
        customBundle = GalleryWeChatBundle(
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
            finderTextCompoundDrawable = R.drawable.ic_gallery_wechat_finder_action,
            finderTextSize = 14.toFloat()
        ),
        bundle = GalleryBundle(
            allName = "图片和视频",
            hideCamera = true,
            scanType = scanType,
            checkBoxDrawable = checkBoxResource,
        ),
        compatBundle = GalleryCompatBundle(
            prevRootBackground = Color.BLACK,
            galleryRootBackground = rgb38
        ),
        clz = GalleryWeChatActivity::class.java
    )
}