package com.gallery.ui.page

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import com.gallery.core.GalleryBundle
import com.gallery.core.delegate.prevFragment
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.ui.R
import com.gallery.ui.activity.PrevBaseActivity
import com.gallery.ui.engine.displayGalleryPrev
import com.gallery.ui.obtain
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : PrevBaseActivity(layoutId) {

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

    override fun onPrevViewCreated(savedInstanceState: Bundle?) {
        preCount.text = "%s / %s".format(prevFragment.selectCount, galleryConfig.multipleMaxCount)
        preToolbar.title = uiConfig.preTitle + "(" + (prevFragment.currentPosition + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanFileEntity: ScanFileEntity) {
        super.onClickCheckBoxFileNotExist(context, galleryBundle, scanFileEntity)
        preCount.text = "%s / %s".format(prevFragment.selectCount, galleryBundle.multipleMaxCount)
    }

    override fun onDisplayGalleryPrev(scanFileEntity: ScanFileEntity, container: FrameLayout) {
        container.displayGalleryPrev(scanFileEntity)
    }

    override fun onPageSelected(position: Int) {
        preToolbar.title = uiConfig.preTitle + "(" + (position + 1) + "/" + prevFragment.itemCount + ")"
    }

    override fun onChangedCheckBox() {
        preCount.text = "%s / %s".format(prevFragment.selectCount.toString(), galleryConfig.multipleMaxCount)
    }
}
