package com.gallery.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.gallery.core.Gallery
import com.gallery.core.GalleryBundle
import com.gallery.core.action.GalleryPreAction
import com.gallery.core.constant.GalleryConst
import com.gallery.core.ext.hasL
import com.gallery.core.ext.statusBarColor
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import kotlinx.android.synthetic.main.gallery_activity_preview.*

class PreActivity : GalleryBaseActivity(), GalleryPreAction {

    override val layoutId: Int = R.layout.gallery_activity_preview

    companion object {
        @JvmStatic
        fun newInstance(galleryBundle: GalleryBundle,
                        uiBundle: GalleryUiBundle,
                        selectList: ArrayList<ScanEntity>,
                        allList: ArrayList<ScanEntity>,
                        position: Int,
                        fragment: ScanFragment) {
            val bundle = Bundle().apply {
                putParcelableArrayList(GalleryConst.TYPE_PRE_SELECT, selectList)
                putParcelableArrayList(GalleryConst.TYPE_PRE_ALL, allList)
                putInt(GalleryConst.TYPE_PRE_POSITION, position)
                putParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS, galleryBundle)
                putParcelable(GalleryConst.EXTRA_GALLERY_UI_OPTIONS, uiBundle)
            }
            fragment.startActivityForResult(Intent(fragment.activity, PreActivity::class.java).putExtras(bundle), GalleryConst.TYPE_PRE_REQUEST_CODE)
        }
    }

    private lateinit var prevFragment: PrevFragment
    private lateinit var galleryBundle: GalleryBundle
    private lateinit var uiBundle: GalleryUiBundle

    override fun initView() {
        preBottomViewSelect.setOnClickListener {
            if (prevFragment.getSelectEntity().isEmpty()) {
                Gallery.instance.galleryListener?.onGalleryContainerPreSelectEmpty()
                return@setOnClickListener
            }
            Gallery.instance.galleryListener?.onGalleryResources(prevFragment.getSelectEntity())
            isRefreshGalleryUI(isRefresh = false, isFinish = uiBundle.preSelectOkFinish)
        }
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {

        galleryBundle = intent.extras?.getParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS)
                ?: GalleryBundle()
        uiBundle = intent.extras?.getParcelable(GalleryConst.EXTRA_GALLERY_UI_OPTIONS)
                ?: GalleryUiBundle()

        preBottomView.setBackgroundColor(ContextCompat.getColor(this, uiBundle.preBottomViewBackground))
        preBottomViewSelect.setText(uiBundle.preBottomOkText)
        preBottomViewSelect.textSize = uiBundle.preBottomOkTextSize
        preBottomViewSelect.setTextColor(ContextCompat.getColor(this, uiBundle.preBottomOkTextColor))
        preCount.textSize = uiBundle.preBottomCountTextSize
        preCount.setTextColor(ContextCompat.getColor(this, uiBundle.preBottomCountTextColor))
        preRootView.setBackgroundColor(ContextCompat.getColor(this, uiBundle.preBackground))
        window.statusBarColor(ContextCompat.getColor(this, uiBundle.statusBarColor))
        preToolbar.setNavigationOnClickListener { isRefreshGalleryUI(uiBundle.preFinishRefresh, false) }
        preToolbar.setTitleTextColor(ContextCompat.getColor(this, uiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, uiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, uiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        preToolbar.navigationIcon = drawable
        preToolbar.setBackgroundColor(ContextCompat.getColor(this, uiBundle.toolbarBackground))
        if (hasL()) {
            preToolbar.elevation = uiBundle.toolbarElevation
        }
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            prevFragment = PrevFragment.newInstance(
                    galleryBundle,
                    intent.extras?.getInt(GalleryConst.TYPE_PRE_POSITION) ?: 0,
                    intent.extras?.getParcelableArrayList(GalleryConst.TYPE_PRE_SELECT)
                            ?: ArrayList(),
                    intent.extras?.getParcelableArrayList(GalleryConst.TYPE_PRE_ALL) ?: ArrayList()
            )
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.preFragment, prevFragment, PrevFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    override fun onChangedCheckBoxCount(selectCount: Int) {
        preCount.text = String.format("%s / %s", selectCount.toString(), galleryBundle.multipleMaxCount)
    }

    override fun onChangedViewPager(currentPos: Int, maxPos: Int) {
        preToolbar.title = getString(uiBundle.preTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
        isRefreshGalleryUI(uiBundle.preBackRefresh, false)
        super.onBackPressed()
    }

    private fun isRefreshGalleryUI(isRefresh: Boolean, isFinish: Boolean) {
        val intent = Intent()
        intent.putExtras(prevFragment.resultBundle(isRefresh, isFinish))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
