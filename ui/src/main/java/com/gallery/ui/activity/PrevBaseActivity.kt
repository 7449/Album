package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.findFragmentByTagExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleIntOrDefault
import androidx.kotlin.expand.os.bundleParcelableArrayListExpand
import androidx.kotlin.expand.os.bundleParcelableOrDefault
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult

abstract class PrevBaseActivity(layoutId: Int) : GalleryBaseActivity(layoutId) {

    companion object {
        fun newInstance(
                context: Context,
                allList: ArrayList<ScanEntity>,
                selectList: ArrayList<ScanEntity> = ArrayList(),
                galleryBundle: GalleryBundle = GalleryBundle(),
                uiBundle: GalleryUiBundle = GalleryUiBundle(),
                position: Int = 0,
                cla: Class<out PrevBaseActivity>): Intent {
            val bundle = Bundle()
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_ALL, allList)
            bundle.putParcelableArrayList(IGalleryPrev.PREV_START_SELECT, selectList)
            bundle.putParcelable(IGalleryPrev.PREV_START_CONFIG, galleryBundle)
            bundle.putParcelable(UIResult.UI_CONFIG, uiBundle)
            bundle.putInt(IGalleryPrev.PREV_START_POSITION, position)
            return Intent(context, cla).putExtras(bundle)
        }
    }

    protected abstract val galleryFragmentId: Int

    val uiBundle by lazy {
        bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle())
    }
    val galleryBundle by lazy {
        bundleParcelableOrDefault<GalleryBundle>(IGalleryPrev.PREV_START_CONFIG, GalleryBundle())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findFragmentByTagExpand(PrevFragment::class.java.simpleName) {
            if (it == null) {
                addFragmentExpand(galleryFragmentId, PrevFragment.newInstance(
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
        val intent = Intent()
        intent.putExtras(prevFragment.resultBundle(uiBundle.preBackRefresh))
        setResult(UIResult.PREV_BACk_FINISH_RESULT_CODE, intent)
        super.onBackPressed()
    }

    /**
     * finish
     */
    open fun onPrevFinish() {
        val intent = Intent()
        intent.putExtras(prevFragment.resultBundle(uiBundle.preFinishRefresh))
        setResult(UIResult.PREV_TOOLBAR_FINISH_RESULT_CODE, intent)
        finish()
    }

    /**
     * 选择数据为空
     */
    open fun onGallerySelectEmpty() {
        getString(R.string.gallery_prev_select_empty_pre).toastExpand(this)
    }

    /**
     * prev select select data
     */
    open fun onPrevSelectEntities() {
        val intent = Intent()
        intent.putExtras(prevFragment.resultBundle(true))
        setResult(UIResult.PREV_OK_FINISH_RESULT_CODE, intent)
        finish()
    }
}