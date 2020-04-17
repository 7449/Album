package com.gallery.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.FrameLayout
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.ext.color
import com.gallery.core.ext.drawable
import com.gallery.core.ext.hasL
import com.gallery.core.ext.statusBarColor
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult
import kotlinx.android.synthetic.main.gallery_activity_preview.*

class PreActivity : GalleryBaseActivity(R.layout.gallery_activity_preview), IGalleryPrevCallback {

    companion object {
        fun newInstance(
                fragment: ScanFragment,
                allList: ArrayList<ScanEntity>,
                selectList: ArrayList<ScanEntity> = ArrayList(),
                galleryBundle: GalleryBundle = GalleryBundle(),
                uiBundle: GalleryUiBundle = GalleryUiBundle(),
                position: Int = 0) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_ALL, allList)
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_SELECT, selectList)
            bundle.putParcelable(IGalleryPrev.PREV_START_CONFIG, galleryBundle)
            bundle.putParcelable(UIResult.UI_CONFIG, uiBundle)
            bundle.putInt(IGalleryPrev.PREV_START_POSITION, position)
            fragment.startActivityForResult(Intent(fragment.activity, PreActivity::class.java).putExtras(bundle), IGalleryPrev.PREV_START_REQUEST_CODE)
        }
    }

    private val uiBundle by lazy {
        intent.extras?.getParcelable(UIResult.UI_CONFIG) ?: GalleryUiBundle()
    }
    private val galleryBundle by lazy {
        intent.extras?.getParcelable(IGalleryPrev.PREV_START_CONFIG) ?: GalleryBundle()
    }

    override fun initView() {
        preBottomViewSelect.setOnClickListener {
            if (prevFragment().selectEmpty) {
                Gallery.instance.galleryListener?.onGalleryContainerPreSelectEmpty()
                return@setOnClickListener
            }
            Gallery.instance.galleryListener?.onGalleryResources(prevFragment().selectEntities)
            isRefreshGalleryUI(isRefresh = false)
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        preBottomView.setBackgroundColor(color(uiBundle.preBottomViewBackground))
        preBottomViewSelect.setText(uiBundle.preBottomOkText)
        preBottomViewSelect.textSize = uiBundle.preBottomOkTextSize
        preBottomViewSelect.setTextColor(color(uiBundle.preBottomOkTextColor))
        preCount.textSize = uiBundle.preBottomCountTextSize
        preCount.setTextColor(color(uiBundle.preBottomCountTextColor))
        preRootView.setBackgroundColor(color(uiBundle.preBackground))
        window.statusBarColor(color(uiBundle.statusBarColor))
        preToolbar.setNavigationOnClickListener { isRefreshGalleryUI(uiBundle.preFinishRefresh) }
        preToolbar.setTitleTextColor(color(uiBundle.toolbarTextColor))
        val drawable = drawable(uiBundle.toolbarIcon)
        drawable?.colorFilter = PorterDuffColorFilter(color(uiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        preToolbar.navigationIcon = drawable
        preToolbar.setBackgroundColor(color(uiBundle.toolbarBackground))
        preCount.text = "%s / %s".format(0, galleryBundle.multipleMaxCount)
        if (hasL()) {
            preToolbar.elevation = uiBundle.toolbarElevation
        }
        if (supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.preFragment, PrevFragment.newInstance(
                            intent.extras?.getParcelableArrayList(IGalleryPrev.PREV_START_ALL)
                                    ?: ArrayList(),
                            intent.extras?.getParcelableArrayList(IGalleryPrev.PREV_START_SELECT)
                                    ?: ArrayList(),
                            intent.extras?.getParcelable(IGalleryPrev.PREV_START_CONFIG)
                                    ?: GalleryBundle(),
                            intent.extras?.getInt(IGalleryPrev.PREV_START_POSITION) ?: 0
                    ), PrevFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().show(prevFragment()).commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        isRefreshGalleryUI(uiBundle.preBackRefresh)
        super.onBackPressed()
    }

    private fun isRefreshGalleryUI(isRefresh: Boolean) {
        val intent = Intent()
        intent.putExtras(prevFragment().resultBundle(isRefresh))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onPageSelected(position: Int) {
        preToolbar.title = getString(uiBundle.preTitle) + "(" + (position + 1) + "/" + prevFragment().itemCount + ")"
    }

    override fun onDisplayImageView(entity: ScanEntity, container: FrameLayout) {
        Gallery.instance.galleryImageLoader?.onDisplayGalleryPrev(entity, container)
    }

    override fun onClickCheckBoxMaxCount() {
    }

    override fun onClickCheckBoxFileNotExist() {
    }

    override fun onChangedCreated() {
        preToolbar.title = getString(uiBundle.preTitle) + "(" + (prevFragment().currentPosition + 1) + "/" + prevFragment().itemCount + ")"
    }

    override fun onChangedCheckBox() {
        preCount.text = "%s / %s".format(prevFragment().selectCount.toString(), galleryBundle.multipleMaxCount)
    }

}
