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
import com.bumptech.glide.Glide
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.activity.prevFragment
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawableExpand
import com.gallery.core.extensions.hasLExpand
import com.gallery.core.extensions.statusBarColorExpand
import com.gallery.ui.R
import com.gallery.ui.databinding.GalleryActivityPreviewBinding

open class PreActivity : PrevCompatActivity() {

    companion object {
        private const val format = "%s / %s"
    }

    private val viewBinding: GalleryActivityPreviewBinding by lazy { GalleryActivityPreviewBinding.inflate(layoutInflater) }

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColorExpand(uiConfig.statusBarColor)
        if (hasLExpand()) {
            window.statusBarColor = uiConfig.statusBarColor
        }
        viewBinding.preToolbar.setTitleTextColor(uiConfig.toolbarTextColor)
        val drawable = drawableExpand(uiConfig.toolbarIcon)
        drawable?.colorFilter = PorterDuffColorFilter(uiConfig.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.preToolbar.navigationIcon = drawable
        viewBinding.preToolbar.setBackgroundColor(uiConfig.toolbarBackground)
        if (hasLExpand()) {
            viewBinding.preToolbar.elevation = uiConfig.toolbarElevation
        }

        viewBinding.preCount.textSize = uiConfig.preBottomCountTextSize
        viewBinding.preCount.setTextColor(uiConfig.preBottomCountTextColor)

        viewBinding.preBottomView.setBackgroundColor(uiConfig.preBottomViewBackground)
        viewBinding.preBottomViewSelect.text = uiConfig.preBottomOkText
        viewBinding.preBottomViewSelect.textSize = uiConfig.preBottomOkTextSize
        viewBinding.preBottomViewSelect.setTextColor(uiConfig.preBottomOkTextColor)

        viewBinding.preBottomViewSelect.setOnClickListener {
            if (prevFragment.selectEmpty) {
                onGallerySelectEmpty()
            } else {
                onGallerySelectEntities()
            }
        }
        viewBinding.preToolbar.setNavigationOnClickListener { onGalleryFinish() }
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
        viewBinding.preCount.text = format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
        viewBinding.preToolbar.title = uiConfig.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        viewBinding.preCount.text = format.format(prevFragment.selectCount, galleryBundle.multipleMaxCount)
    }

    override fun onPageSelected(position: Int) {
        viewBinding.preToolbar.title = uiConfig.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        viewBinding.preCount.text = format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
    }
}
