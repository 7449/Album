package com.gallery.ui.util

import android.content.Intent
import android.graphics.Color
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.core.GalleryBundle
import com.gallery.scan.ScanType
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.page.wechat.GalleryWeChatActivity

fun FragmentActivity.weChatUiGallery(galleryLauncher: ActivityResultLauncher<Intent>) {
    Gallery(
            this,
            null,
            galleryLauncher,
            GalleryBundle(
                    allName = "图片/视频",
                    hideCamera = true,
                    spanCount = 4,
                    scanType = ScanType.MIX,
                    checkBoxDrawable = R.drawable.wechat_selector_gallery_item_check,
                    galleryRootBackground = Color.rgb(38, 38, 38)
            ),
            GalleryUiBundle(
                    statusBarColor = Color.rgb(38, 38, 38),
                    toolbarBackground = Color.rgb(38, 38, 38),
                    bottomViewBackground = Color.rgb(19, 19, 19),
                    preViewText = "预览",
                    preViewTextSize = 14.toFloat(),
                    selectText = "发送",
                    selectTextSize = 12.toFloat(),
                    finderItemBackground = Color.rgb(38, 38, 38),
                    finderItemTextColor = Color.WHITE,
                    finderItemTextCountColor = Color.parseColor("#767676"),
                    finderTextCompoundDrawable = R.drawable.ic_wechat_gallery_finder_action,
                    finderTextSize = 14.toFloat()
            ),
            GalleryWeChatActivity::class.java
    )
}

val GalleryWeChatActivity.rotateAnimation: RotateAnimation by lazy {
    RotateAnimation(0.toFloat(), 180.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat()).apply {
        interpolator = LinearInterpolator()
        duration = 200
        fillAfter = true
    }
}

val GalleryWeChatActivity.rotateAnimationResult: RotateAnimation by lazy {
    RotateAnimation(180.toFloat(), 360.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat()).apply {
        interpolator = LinearInterpolator()
        duration = 200
        fillAfter = true
    }
}
