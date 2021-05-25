package com.gallery.ui.wechat

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.GalleryUiBundle
import com.gallery.core.GalleryBundle
import com.gallery.ui.Gallery
import com.gallery.ui.wechat.activity.GalleryWeChatActivity

val scanType = intArrayOf(
    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
)

val checkBoxResource = R.drawable.wechat_selector_gallery_item_check

fun FragmentActivity.weChatGallery(
    allName: String = "图片和视频",
    prevSelectText: String = "选择",
    sendText: String = "发送",
    videoMaxDuration: Int = 500000,
    allVideoFinderName: String = "全部视频",
    launcher: ActivityResultLauncher<Intent>
) {
    Gallery.newInstance(
        activity = this,
        launcher = launcher,
        option = Bundle().apply {
            putInt(WeChatConfig.VIDEO_MAX_DURATION, videoMaxDuration)
            putString(WeChatConfig.VIDEO_ALL_FINDER_NAME, allVideoFinderName)
        },
        bundle = GalleryBundle(
            allName = allName,
            hideCamera = true,
            scanType = scanType,
            checkBoxDrawable = checkBoxResource,
        ),
        uiBundle = GalleryUiBundle(
            prevPhotoBackgroundColor = Color.BLACK,
            galleryRootBackground = Color.rgb(38, 38, 38),
            statusBarColor = Color.rgb(38, 38, 38),
            toolbarBackground = Color.rgb(38, 38, 38),
            bottomViewBackground = Color.rgb(19, 19, 19),
            preBottomViewBackground = Color.rgb(38, 38, 38),
            preBottomOkText = prevSelectText,
            preBottomOkTextSize = 14.toFloat(),
            preViewTextSize = 14.toFloat(),
            selectText = sendText,
            selectTextSize = 12.toFloat(),
            finderItemBackground = Color.rgb(38, 38, 38),
            finderItemTextColor = Color.WHITE,
            finderItemTextCountColor = Color.parseColor("#767676"),
            finderTextCompoundDrawable = R.drawable.ic_wechat_gallery_finder_action,
            finderTextSize = 14.toFloat()
        ),
        clz = GalleryWeChatActivity::class.java
    )
}