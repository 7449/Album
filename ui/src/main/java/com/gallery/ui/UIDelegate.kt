package com.gallery.ui

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.kotlin.expand.drawableExpand
import androidx.kotlin.expand.hasLExpand
import androidx.kotlin.expand.minimumDrawable
import androidx.kotlin.expand.statusBarColorExpand
import com.gallery.ui.activity.GalleryActivity
import com.gallery.ui.activity.PreActivity
import kotlinx.android.synthetic.main.gallery_activity_gallery.*
import kotlinx.android.synthetic.main.gallery_activity_preview.*

@SuppressLint("NewApi")
internal fun GalleryActivity.obtain(galleryUiBundle: GalleryUiBundle) {
    window.statusBarColorExpand(galleryUiBundle.statusBarColor)
    if (hasLExpand()) {
        window.statusBarColor = galleryUiBundle.statusBarColor
    }
    galleryToolbar.title = galleryUiBundle.toolbarText
    galleryToolbar.setTitleTextColor(galleryUiBundle.toolbarTextColor)
    val drawable = drawableExpand(galleryUiBundle.toolbarIcon)
    drawable?.colorFilter = PorterDuffColorFilter(galleryUiBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
    galleryToolbar.navigationIcon = drawable
    galleryToolbar.setBackgroundColor(galleryUiBundle.toolbarBackground)
    if (hasLExpand()) {
        galleryToolbar.elevation = galleryUiBundle.toolbarElevation
    }

    galleryFinderAll.textSize = galleryUiBundle.bottomFinderTextSize
    galleryFinderAll.setTextColor(galleryUiBundle.bottomFinderTextColor)
    galleryFinderAll.setCompoundDrawables(null, null, minimumDrawable(galleryUiBundle.bottomFinderTextCompoundDrawable, galleryUiBundle.bottomFinderTextDrawableColor), null)

    galleryPre.text = galleryUiBundle.bottomPreViewText
    galleryPre.textSize = galleryUiBundle.bottomPreViewTextSize
    galleryPre.setTextColor(galleryUiBundle.bottomPreViewTextColor)

    gallerySelect.text = galleryUiBundle.bottomSelectText
    gallerySelect.textSize = galleryUiBundle.bottomSelectTextSize
    gallerySelect.setTextColor(galleryUiBundle.bottomSelectTextColor)

    galleryBottomView.setBackgroundColor(galleryUiBundle.bottomViewBackground)
}

@SuppressLint("NewApi")
internal fun PreActivity.obtain(uiBundle: GalleryUiBundle) {
    window.statusBarColorExpand(uiBundle.statusBarColor)
    if (hasLExpand()) {
        window.statusBarColor = uiBundle.statusBarColor
    }
    preToolbar.setTitleTextColor(uiBundle.toolbarTextColor)
    val drawable = drawableExpand(uiBundle.toolbarIcon)
    drawable?.colorFilter = PorterDuffColorFilter(uiBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
    preToolbar.navigationIcon = drawable
    preToolbar.setBackgroundColor(uiBundle.toolbarBackground)
    if (hasLExpand()) {
        preToolbar.elevation = uiBundle.toolbarElevation
    }

    preCount.textSize = uiBundle.preBottomCountTextSize
    preCount.setTextColor(uiBundle.preBottomCountTextColor)

    preBottomView.setBackgroundColor(uiBundle.preBottomViewBackground)
    preBottomViewSelect.text = uiBundle.preBottomOkText
    preBottomViewSelect.textSize = uiBundle.preBottomOkTextSize
    preBottomViewSelect.setTextColor(uiBundle.preBottomOkTextColor)
}