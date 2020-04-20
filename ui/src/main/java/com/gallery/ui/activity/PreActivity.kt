package com.gallery.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.ext.externalUri
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult
import com.gallery.ui.obtain
import com.kotlin.x.addFragment
import com.kotlin.x.findFragmentByTag
import com.kotlin.x.getParcelableArrayList
import com.kotlin.x.showFragment
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : GalleryBaseActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)

        preBottomViewSelect.setOnClickListener { onGalleryResources(prevFragment.selectEntities) }
        preToolbar.setNavigationOnClickListener { isRefreshGalleryUI(uiBundle.preFinishRefresh, false) }
        preCount.text = "%s / %s".format(0, galleryBundle.multipleMaxCount)

        findFragmentByTag(PrevFragment::class.java.simpleName) {
            if (it == null) {
                addFragment(R.id.preFragment, PrevFragment.newInstance(
                        getParcelableArrayList(IGalleryPrev.PREV_START_ALL),
                        getParcelableArrayList(IGalleryPrev.PREV_START_SELECT),
                        intent.extras?.getParcelable(IGalleryPrev.PREV_START_CONFIG)
                                ?: GalleryBundle(),
                        intent.extras?.getInt(IGalleryPrev.PREV_START_POSITION) ?: 0
                ))
            } else {
                showFragment(it)
            }
        }
    }

    override fun onBackPressed() {
        isRefreshGalleryUI(uiBundle.preBackRefresh, false)
        super.onBackPressed()
    }

    private fun isRefreshGalleryUI(isRefresh: Boolean, isFinish: Boolean, arrayList: ArrayList<ScanEntity> = ArrayList()) {
        val intent = Intent()
        val resultBundle = prevFragment.resultBundle(isRefresh)
        resultBundle.putBoolean(UIResult.PREV_RESULT_FINISH, isFinish)
        resultBundle.putParcelableArrayList(UIResult.PREV_RESULT_SELECT, arrayList)
        intent.putExtras(resultBundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
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

    /**
     * 选择图片
     */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        isRefreshGalleryUI(isRefresh = true, isFinish = false, arrayList = entities)
    }

}
