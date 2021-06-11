package com.gallery.ui.material.activity

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.extensions.requirePrevFragment
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawableExpand
import com.gallery.core.extensions.safeToastExpand
import com.gallery.ui.material.R
import com.gallery.ui.material.args.MaterialGalleryBundle
import com.gallery.ui.material.databinding.MaterialGalleryActivityPreviewBinding
import com.gallery.ui.material.materialGalleryArgOrDefault

open class MaterialPreActivity : PrevCompatActivity() {

    companion object {
        private const val format = "%s / %s"
    }

    private val viewBinding: MaterialGalleryActivityPreviewBinding by lazy {
        MaterialGalleryActivityPreviewBinding.inflate(
            layoutInflater
        )
    }

    private val materialGalleryBundle: MaterialGalleryBundle by lazy {
        gapConfig.materialGalleryArgOrDefault
    }

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColor = materialGalleryBundle.statusBarColor
        viewBinding.preToolbar.setTitleTextColor(materialGalleryBundle.toolbarTextColor)
        val drawable = drawableExpand(materialGalleryBundle.toolbarIcon)
        drawable?.colorFilter =
            PorterDuffColorFilter(materialGalleryBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.preToolbar.navigationIcon = drawable
        viewBinding.preToolbar.setBackgroundColor(materialGalleryBundle.toolbarBackground)
        viewBinding.preToolbar.elevation = materialGalleryBundle.toolbarElevation

        viewBinding.preCount.textSize = materialGalleryBundle.preBottomCountTextSize
        viewBinding.preCount.setTextColor(materialGalleryBundle.preBottomCountTextColor)

        viewBinding.preBottomView.setBackgroundColor(materialGalleryBundle.preBottomViewBackground)
        viewBinding.preBottomViewSelect.text = materialGalleryBundle.preBottomOkText
        viewBinding.preBottomViewSelect.textSize = materialGalleryBundle.preBottomOkTextSize
        viewBinding.preBottomViewSelect.setTextColor(materialGalleryBundle.preBottomOkTextColor)

        viewBinding.preBottomViewSelect.setOnClickListener {
            if (requirePrevFragment.isSelectEmpty) {
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
        delegate.rootView.setBackgroundColor(materialGalleryBundle.prevRootBackground)
        viewBinding.preCount.text =
            format.format(delegate.selectCount, galleryConfig.multipleMaxCount)
        viewBinding.preToolbar.title =
            materialGalleryBundle.preTitle + "(" + (delegate.currentPosition + 1) + "/" + delegate.itemCount + ")"
    }

    override fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        super.onClickItemFileNotExist(context, bundle, scanEntity)
        viewBinding.preCount.text =
            format.format(requirePrevFragment.selectCount, bundle.multipleMaxCount)
    }

    override fun onPageSelected(position: Int) {
        viewBinding.preToolbar.title =
            materialGalleryBundle.preTitle + "(" + (position + 1) + "/" + requirePrevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        viewBinding.preCount.text =
            format.format(requirePrevFragment.selectCount, galleryConfig.multipleMaxCount)
    }

    open fun onGallerySelectEmpty() {
        getString(R.string.material_gallery_prev_select_empty_pre).safeToastExpand(this)
    }

}