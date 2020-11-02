package com.gallery.ui.wechat.extension

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.GalleryUiBundle
import com.gallery.core.GalleryBundle
import com.gallery.ui.Gallery
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.activity.GalleryWeChatActivity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
private val formatter = SimpleDateFormat("yyyy/MM")

internal fun Long.formatTimeVideo(): String {
    if (toInt() == 0) {
        return "--:--"
    }
    val format: String = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(this),
            TimeUnit.MILLISECONDS.toSeconds(this) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this)))
    if (!format.startsWith("0")) {
        return format
    }
    return format.substring(1)
}

internal fun Long.formatTime(): String {
    if (toInt() == 0) {
        return "--/--"
    }
    return formatter.format(this * 1000)
}

internal fun Long.toFileSize(): String {
    return when {
        this < 1024 -> DecimalFormat().format(this) + " B"
        this < 1048576 -> DecimalFormat().format(this / 1024) + " KB"
        this < 1073741824 -> DecimalFormat().format(this / 1048576) + " MB"
        else -> DecimalFormat().format(this / 1073741824) + " GB"
    }
}

internal val GalleryWeChatActivity.rotateAnimation: RotateAnimation by lazy {
    RotateAnimation(0.toFloat(), 180.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat()).apply {
        interpolator = LinearInterpolator()
        duration = 200
        fillAfter = true
    }
}

internal val GalleryWeChatActivity.rotateAnimationResult: RotateAnimation by lazy {
    RotateAnimation(180.toFloat(), 360.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat(), Animation.RELATIVE_TO_SELF, 0.5.toFloat()).apply {
        interpolator = LinearInterpolator()
        duration = 200
        fillAfter = true
    }
}

fun FragmentActivity.weChatUiGallery(galleryLauncher: ActivityResultLauncher<Intent>) {
    Gallery.newInstance(
            activity = this,
            galleryLauncher = galleryLauncher,
            galleryBundle = GalleryBundle(
                    allName = "图片和视频",
                    hideCamera = true,
                    scanType = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                    checkBoxDrawable = R.drawable.wechat_selector_gallery_item_check,
            ),
            galleryUiBundle = GalleryUiBundle(
                    prevPhotoBackgroundColor = Color.BLACK,
                    galleryRootBackground = Color.rgb(38, 38, 38),
                    statusBarColor = Color.rgb(38, 38, 38),
                    toolbarBackground = Color.rgb(38, 38, 38),
                    bottomViewBackground = Color.rgb(19, 19, 19),
                    preBottomViewBackground = Color.rgb(38, 38, 38),
                    preBottomOkText = "选择",
                    preBottomOkTextSize = 14.toFloat(),
                    preViewTextSize = 14.toFloat(),
                    selectText = "发送",
                    selectTextSize = 12.toFloat(),
                    finderItemBackground = Color.rgb(38, 38, 38),
                    finderItemTextColor = Color.WHITE,
                    finderItemTextCountColor = Color.parseColor("#767676"),
                    finderTextCompoundDrawable = R.drawable.ic_wechat_gallery_finder_action,
                    finderTextSize = 14.toFloat()
            ),
            galleryOption = Bundle().apply {
                putInt(WeChatConfig.GALLERY_WE_CHAT_VIDEO_DURATION, 500000)
                putString(WeChatConfig.GALLERY_WE_CHAT_VIDEO_DES, "全部视频")
            },
            clz = GalleryWeChatActivity::class.java
    )
}