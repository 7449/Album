package com.gallery.ui.material.activity

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.compat.activity.PrevCompatActivity
import com.gallery.compat.extensions.requirePrevFragment
import com.gallery.core.GalleryConfigs
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawable
import com.gallery.core.extensions.toast
import com.gallery.ui.material.R
import com.gallery.ui.material.args.MaterialGalleryConfig
import com.gallery.ui.material.createGalleryImageView
import com.gallery.ui.material.databinding.MaterialGalleryActivityPreviewBinding
import com.gallery.ui.material.materialGalleryArgOrDefault

open class MaterialPreActivity : PrevCompatActivity() {

    companion object {
        private const val format = "%s / %s"
    }

    private val viewBinding: MaterialGalleryActivityPreviewBinding by lazy {
        MaterialGalleryActivityPreviewBinding.inflate(layoutInflater)
    }

    private val config: MaterialGalleryConfig by lazy {
        gapConfig.materialGalleryArgOrDefault
    }

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColor = config.statusBarColor
        viewBinding.toolbar.title = config.preTitle
        viewBinding.toolbar.setTitleTextColor(config.toolbarTextColor)
        val drawable = drawable(config.toolbarIcon)
        drawable?.colorFilter = PorterDuffColorFilter(config.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.toolbar.navigationIcon = drawable
        viewBinding.toolbar.setBackgroundColor(config.toolbarBackground)
        viewBinding.toolbar.elevation = config.toolbarElevation

        viewBinding.count.textSize = config.preBottomCountTextSize
        viewBinding.count.setTextColor(config.preBottomCountTextColor)

        viewBinding.bottomView.setBackgroundColor(config.preBottomViewBackground)
        viewBinding.bottomViewSelect.text = config.preBottomOkText
        viewBinding.bottomViewSelect.textSize = config.preBottomOkTextSize
        viewBinding.bottomViewSelect.setTextColor(config.preBottomOkTextColor)

        viewBinding.bottomViewSelect.setOnClickListener {
            if (requirePrevFragment.isSelectEmpty) {
                onGallerySelectEmpty()
            } else {
                onGallerySelectEntities()
            }
        }
        viewBinding.toolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onDisplayPrevGallery(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = container.createGalleryImageView()
        Glide.with(container.context)
            .load(entity.uri)
            .into(imageView)
        container.addView(imageView)
    }

    override fun onPrevCreated(delegate: IPrevDelegate, configs: GalleryConfigs, saveState: Bundle?) {
        delegate.rootView.setBackgroundColor(config.prevRootBackground)
        viewBinding.count.text = format.format(delegate.selectCount, galleryConfig.maxCount)
        viewBinding.toolbar.title = config.preTitle + "(" + (delegate.currentPosition + 1) + "/" + delegate.itemCount + ")"
    }

    override fun onClickItemFileNotExist(context: Context, configs: GalleryConfigs, scanEntity: ScanEntity) {
        super.onClickItemFileNotExist(context, configs, scanEntity)
        viewBinding.count.text = format.format(requirePrevFragment.selectCount, configs.maxCount)
    }

    override fun onPageSelected(position: Int) {
        viewBinding.toolbar.title = config.preTitle + "(" + (position + 1) + "/" + requirePrevFragment.itemCount + ")"
    }

    override fun onCheckBoxChanged() {
        viewBinding.count.text = format.format(requirePrevFragment.selectCount, galleryConfig.maxCount)
    }

    open fun onGallerySelectEmpty() {
        getString(R.string.material_gallery_prev_select_empty_pre).toast(this)
    }

}