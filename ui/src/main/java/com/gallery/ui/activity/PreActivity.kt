package com.gallery.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.ext.drawable
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.hasL
import com.gallery.core.ext.statusBarColor
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult
import kotlinx.android.synthetic.main.gallery_activity_preview.*

class PreActivity : GalleryBaseActivity(R.layout.gallery_activity_preview), IGalleryPrevCallback, IGalleryImageLoader {

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
            isRefreshGalleryUI(isRefresh = false, isFinish = true)
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        preBottomView.setBackgroundColor(uiBundle.preBottomViewBackground)
        preBottomViewSelect.text = uiBundle.preBottomOkText
        preBottomViewSelect.textSize = uiBundle.preBottomOkTextSize
        preBottomViewSelect.setTextColor(uiBundle.preBottomOkTextColor)
        preCount.textSize = uiBundle.preBottomCountTextSize
        preCount.setTextColor(uiBundle.preBottomCountTextColor)
        window.statusBarColor(uiBundle.statusBarColor)
        preToolbar.setNavigationOnClickListener { isRefreshGalleryUI(uiBundle.preFinishRefresh, false) }
        preToolbar.setTitleTextColor(uiBundle.toolbarTextColor)
        val drawable = drawable(uiBundle.toolbarIcon)
        drawable?.colorFilter = PorterDuffColorFilter(uiBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        preToolbar.navigationIcon = drawable
        preToolbar.setBackgroundColor(uiBundle.toolbarBackground)
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
        isRefreshGalleryUI(uiBundle.preBackRefresh, false)
        super.onBackPressed()
    }

    private fun isRefreshGalleryUI(isRefresh: Boolean, isFinish: Boolean) {
        val intent = Intent()
        val resultBundle = prevFragment().resultBundle(isRefresh)
        resultBundle.putBoolean(UIResult.PREV_RESULT_FINISH, isFinish)
        intent.putExtras(resultBundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onPageSelected(position: Int) {
        preToolbar.title = uiBundle.preTitle + "(" + (position + 1) + "/" + prevFragment().itemCount + ")"
    }

    override fun onDisplayGalleryPrev(galleryEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(galleryEntity.externalUri()).into(imageView)
        container.addView(imageView)
    }

    override fun onClickCheckBoxMaxCount() {
    }

    override fun onClickCheckBoxFileNotExist() {
    }

    override fun onChangedCreated() {
        preToolbar.title = uiBundle.preTitle + "(" + (prevFragment().currentPosition + 1) + "/" + prevFragment().itemCount + ")"
    }

    override fun onChangedCheckBox() {
        preCount.text = "%s / %s".format(prevFragment().selectCount.toString(), galleryBundle.multipleMaxCount)
    }

}
