package com.gallery.ui.wechat

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.version.hasLExpand
import androidx.kotlin.expand.view.statusBarColorExpand
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.GalleryBundle
import com.gallery.scan.ScanType
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.wechat.activity.GalleryWeChatActivity
import com.gallery.ui.wechat.activity.GalleryWeChatPrevActivity
import kotlinx.android.synthetic.main.gallery_activity_wechat_gallery.*
import kotlinx.android.synthetic.main.gallery_activity_wechat_prev.*

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
                    prevPhotoBackgroundColor = Color.BLACK,
                    checkBoxDrawable = R.drawable.wechat_selector_gallery_item_check,
                    galleryRootBackground = Color.rgb(38, 38, 38)
            ),
            GalleryUiBundle(
                    statusBarColor = Color.rgb(38, 38, 38),
                    toolbarBackground = Color.rgb(38, 38, 38),
                    bottomViewBackground = Color.rgb(19, 19, 19),
                    preBottomViewBackground = Color.rgb(38, 38, 38),
                    preViewText = "预览",
                    preBottomOkText = "选择",
                    preBottomOkTextSize = 14.toFloat(),
                    preBottomOkTextColor = Color.WHITE,
                    preViewTextSize = 14.toFloat(),
                    selectText = "发送",
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

@Suppress("NOTHING_TO_INLINE")
@SuppressLint("NewApi")
internal inline fun GalleryWeChatActivity.obtain(uiBundle: GalleryUiBundle) {
    window.statusBarColorExpand(uiBundle.statusBarColor)
    if (hasLExpand()) {
        window.statusBarColor = uiBundle.statusBarColor
    }
    galleryWeChatToolbar.setBackgroundColor(uiBundle.toolbarBackground)

    galleryWeChatFinder.layoutManager = LinearLayoutManager(this)
    galleryWeChatFinder.setBackgroundColor(uiBundle.finderItemBackground)
    galleryWeChatFinder.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

    galleryWeChatBottomView.setBackgroundColor(uiBundle.bottomViewBackground)

    galleryWeChatPrev.text = uiBundle.preViewText
    galleryWeChatPrev.textSize = uiBundle.preViewTextSize

    galleryWeChatToolbarSend.textSize = uiBundle.selectTextSize
    galleryWeChatToolbarSend.text = uiBundle.selectText
    galleryWeChatFullImage.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)

    galleryWeChatToolbarFinderText.textSize = uiBundle.finderTextSize
    galleryWeChatToolbarFinderText.setTextColor(uiBundle.finderTextColor)
    galleryWeChatToolbarFinderIcon.setImageResource(uiBundle.finderTextCompoundDrawable)
    galleryWeChatToolbarFinderText.text = finderName
}

@Suppress("NOTHING_TO_INLINE")
@SuppressLint("NewApi")
internal inline fun GalleryWeChatPrevActivity.obtain(uiBundle: GalleryUiBundle) {
    window.statusBarColorExpand(uiBundle.statusBarColor)
    if (hasLExpand()) {
        window.statusBarColor = uiBundle.statusBarColor
    }
    prevWeChatToolbar.setBackgroundColor(uiBundle.toolbarBackground)

    prevWeChatBottomView.setBackgroundColor(uiBundle.preBottomViewBackground)
    prevWeChatSelect.text = uiBundle.preBottomOkText
    prevWeChatSelect.textSize = uiBundle.preBottomOkTextSize
    prevWeChatSelect.setTextColor(uiBundle.preBottomOkTextColor)

//    prevWeChatFullImage.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)
//    prevWeChatSelect.setButtonDrawable(R.drawable.wechat_selector_gallery_full_image_item_check)

    prevWeChatToolbarSend.textSize = uiBundle.selectTextSize
    prevWeChatToolbarSend.text = uiBundle.selectText
}
