@file:Suppress("NOTHING_TO_INLINE")

package com.gallery.ui.activity.ext

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import androidx.kotlin.expand.content.drawableExpand
import androidx.kotlin.expand.content.minimumDrawableExpand
import androidx.kotlin.expand.version.hasLExpand
import androidx.kotlin.expand.view.statusBarColorExpand
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.activity.GalleryActivity
import com.gallery.ui.activity.PreActivity
import com.gallery.ui.fragment.PrevFragment
import com.gallery.ui.fragment.ScanFragment
import kotlinx.android.synthetic.main.gallery_activity_gallery.*
import kotlinx.android.synthetic.main.gallery_activity_preview.*

/** [ScanFragment] */
val AppCompatActivity.galleryFragment: ScanFragment get() = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) as ScanFragment

/** [PrevFragment] */
val AppCompatActivity.prevFragment: PrevFragment get() = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) as PrevFragment

@SuppressLint("NewApi")
internal inline fun GalleryActivity.obtain(galleryUiBundle: GalleryUiBundle) {
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

    galleryFinderAll.textSize = galleryUiBundle.finderTextSize
    galleryFinderAll.setTextColor(galleryUiBundle.finderTextColor)
    galleryFinderAll.setCompoundDrawables(null, null, minimumDrawableExpand(galleryUiBundle.finderTextCompoundDrawable, galleryUiBundle.finderTextDrawableColor), null)

    galleryPre.text = galleryUiBundle.preViewText
    galleryPre.textSize = galleryUiBundle.preViewTextSize
    galleryPre.setTextColor(galleryUiBundle.preViewTextColor)

    gallerySelect.text = galleryUiBundle.selectText
    gallerySelect.textSize = galleryUiBundle.selectTextSize
    gallerySelect.setTextColor(galleryUiBundle.selectTextColor)

    galleryBottomView.setBackgroundColor(galleryUiBundle.bottomViewBackground)
}

@SuppressLint("NewApi")
internal inline fun PreActivity.obtain(uiBundle: GalleryUiBundle) {
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