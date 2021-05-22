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
import com.gallery.compat.extensions.statusBarColorExpand
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawableExpand
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
        window.statusBarColorExpand(uiConfig.statusBarColor)
        viewBinding.preToolbar.setTitleTextColor(uiConfig.toolbarTextColor)
        val drawable = drawableExpand(uiConfig.toolbarIcon)
        drawable?.colorFilter =
            PorterDuffColorFilter(uiConfig.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.preToolbar.navigationIcon = drawable
        viewBinding.preToolbar.setBackgroundColor(uiConfig.toolbarBackground)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            viewBinding.preToolbar.elevation = uiConfig.toolbarElevation
        }

        viewBinding.preCount.textSize = uiConfig.preBottomCountTextSize
        viewBinding.preCount.setTextColor(uiConfig.preBottomCountTextColor)

        viewBinding.preBottomView.setBackgroundColor(uiConfig.preBottomViewBackground)
        viewBinding.preBottomViewSelect.text = uiConfig.preBottomOkText
        viewBinding.preBottomViewSelect.textSize = uiConfig.preBottomOkTextSize
        viewBinding.preBottomViewSelect.setTextColor(uiConfig.preBottomOkTextColor)

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
            uiConfig.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
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
            uiConfig.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        viewBinding.preCount.text =
            format.format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
    }
}
