package com.gallery.ui.activity

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.extensions.prevFragment
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawableExpand
import com.gallery.core.extensions.safeToastExpand
import com.gallery.ui.R
import com.gallery.ui.databinding.GalleryActivityPreviewBinding

open class PreActivity : PrevCompatActivity() {

    companion object {
        private const val format = "%s / %s"
    }

    private val viewBinding: GalleryActivityPreviewBinding by lazy {
        GalleryActivityPreviewBinding.inflate(
            layoutInflater
        )
    }

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColor = compatConfig.statusBarColor
        viewBinding.preToolbar.setTitleTextColor(compatConfig.toolbarTextColor)
        val drawable = drawableExpand(compatConfig.toolbarIcon)
        drawable?.colorFilter =
            PorterDuffColorFilter(compatConfig.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.preToolbar.navigationIcon = drawable
        viewBinding.preToolbar.setBackgroundColor(compatConfig.toolbarBackground)
        viewBinding.preToolbar.elevation = compatConfig.toolbarElevation

        viewBinding.preCount.textSize = compatConfig.preBottomCountTextSize
        viewBinding.preCount.setTextColor(compatConfig.preBottomCountTextColor)

        viewBinding.preBottomView.setBackgroundColor(compatConfig.preBottomViewBackground)
        viewBinding.preBottomViewSelect.text = compatConfig.preBottomOkText
        viewBinding.preBottomViewSelect.textSize = compatConfig.preBottomOkTextSize
        viewBinding.preBottomViewSelect.setTextColor(compatConfig.preBottomOkTextColor)

        viewBinding.preBottomViewSelect.setOnClickListener {
            if (prevFragment.isSelectEmpty) {
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
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }
        Glide.with(container.context).load(scanEntity.uri).into(imageView)
        container.addView(imageView)
    }

    override fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
        super.onPrevCreated(delegate, bundle, savedInstanceState)
        val prevFragment = prevFragment
        viewBinding.preCount.text =
            format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
        viewBinding.preToolbar.title =
            compatConfig.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        super.onClickItemFileNotExist(context, bundle, scanEntity)
        viewBinding.preCount.text = format.format(prevFragment.selectCount, bundle.multipleMaxCount)
    }

    override fun onPageSelected(position: Int) {
        viewBinding.preToolbar.title =
            compatConfig.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        viewBinding.preCount.text =
            format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
    }

    open fun onGallerySelectEmpty() {
        getString(R.string.gallery_prev_select_empty_pre).safeToastExpand(this)
    }

}