package com.gallery.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.content.drawableExpand
import androidx.kotlin.expand.version.hasLExpand
import androidx.kotlin.expand.view.statusBarColorExpand
import com.bumptech.glide.Glide
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.activity.prevFragment
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.R
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : PrevCompatActivity(layoutId) {

    companion object {
        private const val format = "%s / %s"
    }

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColorExpand(uiConfig.statusBarColor)
        if (hasLExpand()) {
            window.statusBarColor = uiConfig.statusBarColor
        }
        preToolbar.setTitleTextColor(uiConfig.toolbarTextColor)
        val drawable = drawableExpand(uiConfig.toolbarIcon)
        drawable?.colorFilter = PorterDuffColorFilter(uiConfig.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        preToolbar.navigationIcon = drawable
        preToolbar.setBackgroundColor(uiConfig.toolbarBackground)
        if (hasLExpand()) {
            preToolbar.elevation = uiConfig.toolbarElevation
        }

        preCount.textSize = uiConfig.preBottomCountTextSize
        preCount.setTextColor(uiConfig.preBottomCountTextColor)

        preBottomView.setBackgroundColor(uiConfig.preBottomViewBackground)
        preBottomViewSelect.text = uiConfig.preBottomOkText
        preBottomViewSelect.textSize = uiConfig.preBottomOkTextSize
        preBottomViewSelect.setTextColor(uiConfig.preBottomOkTextColor)

        preBottomViewSelect.setOnClickListener {
            if (prevFragment.selectEmpty) {
                onGallerySelectEmpty()
            } else {
                onGallerySelectEntities()
            }
        }
        preToolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView: GalleryImageView = GalleryImageView(container.context).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.CENTER
            }
        }
        Glide.with(container.context).load(scanEntity.uri).into(imageView)
        container.addView(imageView)
    }

    override fun onPrevCreated(fragment: Fragment, galleryBundle: GalleryBundle, savedInstanceState: Bundle?) {
        super.onPrevCreated(fragment, galleryBundle, savedInstanceState)
        val prevFragment = prevFragment
        preCount.text = format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
        preToolbar.title = uiConfig.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        preCount.text = format.format(prevFragment.selectCount, galleryBundle.multipleMaxCount)
    }

    override fun onPageSelected(position: Int) {
        preToolbar.title = uiConfig.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        preCount.text = format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
    }
}
