package com.gallery.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.findFragmentByTagExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleIntOrDefault
import androidx.kotlin.expand.os.bundleParcelableArrayListExpand
import androidx.kotlin.expand.os.bundleParcelableOrDefault
import androidx.kotlin.expand.text.toastExpand
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
import kotlinx.android.synthetic.main.gallery_activity_preview.*

open class PreActivity(layoutId: Int = R.layout.gallery_activity_preview) : GalleryBaseActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader {

    companion object {
        fun newInstance(
                fragment: ScanFragment,
                allList: ArrayList<ScanEntity>,
                selectList: ArrayList<ScanEntity> = ArrayList(),
                galleryBundle: GalleryBundle = GalleryBundle(),
                uiBundle: GalleryUiBundle = GalleryUiBundle(),
                position: Int = 0): Intent {
            val bundle = Bundle()
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_ALL, allList)
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_SELECT, selectList)
            bundle.putParcelable(IGalleryPrev.PREV_START_CONFIG, galleryBundle)
            bundle.putParcelable(UIResult.UI_CONFIG, uiBundle)
            bundle.putInt(IGalleryPrev.PREV_START_POSITION, position)
            return Intent(fragment.activity, PreActivity::class.java).putExtras(bundle)
        }
    }

    private val uiBundle by lazy {
        bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle())
    }
    private val galleryBundle by lazy {
        bundleParcelableOrDefault<GalleryBundle>(IGalleryPrev.PREV_START_CONFIG, GalleryBundle())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiBundle)

        preBottomViewSelect.setOnClickListener {
            if (prevFragment.selectEmpty) {
                onGallerySelectEmpty()
            } else {
                onGalleryResources(prevFragment.selectEntities)
            }
        }
        preToolbar.setNavigationOnClickListener { isRefreshGalleryUI(uiBundle.preFinishRefresh, false) }
        preCount.text = "%s / %s".format(0, galleryBundle.multipleMaxCount)

        findFragmentByTagExpand(PrevFragment::class.java.simpleName) {
            if (it == null) {
                addFragmentExpand(R.id.preFragment, PrevFragment.newInstance(
                        bundleParcelableArrayListExpand(IGalleryPrev.PREV_START_ALL),
                        bundleParcelableArrayListExpand(IGalleryPrev.PREV_START_SELECT),
                        galleryBundle,
                        bundleIntOrDefault(IGalleryPrev.PREV_START_POSITION)
                ))
            } else {
                showFragmentExpand(it)
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
        isRefreshGalleryUI(isRefresh = true, isFinish = true, arrayList = entities)
    }

    /**
     * 选择数据为空
     */
    open fun onGallerySelectEmpty() {
        getString(R.string.gallery_prev_select_empty_pre).toastExpand(this)
    }

}
