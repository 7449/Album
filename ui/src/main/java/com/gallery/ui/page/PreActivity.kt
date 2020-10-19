package com.gallery.ui.page

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.ui.compat.prevFragment
import com.gallery.ui.widget.GalleryImageView
import com.gallery.ui.R
import com.gallery.ui.base.activity.PrevBaseActivity
import com.gallery.ui.obtain
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : PrevBaseActivity(layoutId) {

    companion object {
        private const val format = "%s / %s"
    }

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiConfig)
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

    override fun onPrevCreated() {
        val fragment = prevFragment
        preCount.text = format.format(fragment.selectCount, galleryConfig.multipleMaxCount)
        preToolbar.title = uiConfig.preTitle + "(" + (fragment.currentPosition + 1) + "/" + fragment.itemCount + ")"
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
