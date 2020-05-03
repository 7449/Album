package com.gallery.ui.activity

import android.os.Bundle
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.ext.externalUri
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.R
import com.gallery.ui.obtain
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : PrevBaseActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader {

    override val galleryFragmentId: Int
        get() = R.id.preFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)
        preBottomViewSelect.setOnClickListener {
            if (prevFragment.selectEmpty) {
                onGallerySelectEmpty()
            } else {
                onPrevSelectEntities()
            }
        }
        preToolbar.setNavigationOnClickListener { onPrevFinish() }
        preCount.text = "%s / %s".format(0, galleryBundle.multipleMaxCount)
    }

    override fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(galleryEntity.externalUri()).into(imageView)
        container.addView(imageView)
    }

    override fun onPageSelected(position: Int) {
        preToolbar.title = uiBundle.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCreated() {
        preToolbar.title = uiBundle.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        preCount.text = "%s / %s".format(prevFragment.selectCount.toString(), galleryBundle.multipleMaxCount)
    }
}
