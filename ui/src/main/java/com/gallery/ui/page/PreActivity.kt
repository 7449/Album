package com.gallery.ui.page

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.gallery.core.GalleryBundle
import com.gallery.scan.ScanEntity
import com.gallery.ui.R
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.obtain
import com.gallery.ui.util.displayGalleryPrev
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : PrevBaseActivity(layoutId) {

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
    }

    override fun onPrevViewCreated(savedInstanceState: Bundle?) {
        preCount.text = "%s / %s".format(prevFragment.selectCount, galleryBundle.multipleMaxCount)
        preToolbar.title = uiBundle.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onClickCheckBoxFileNotExist(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanEntity)
        preCount.text = "%s / %s".format(prevFragment.selectCount, galleryBundle.multipleMaxCount)
    }

    override fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryPrev(galleryEntity)
    }

    override fun onPageSelected(position: Int) {
        preToolbar.title = uiBundle.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        preCount.text = "%s / %s".format(prevFragment.selectCount.toString(), galleryBundle.multipleMaxCount)
    }
}
