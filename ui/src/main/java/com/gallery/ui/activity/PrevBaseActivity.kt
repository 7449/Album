package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleBundleExpand
import androidx.kotlin.expand.os.bundleIntOrDefault
import androidx.kotlin.expand.os.bundleParcelableArrayListExpand
import androidx.kotlin.expand.os.bundleParcelableOrDefault
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult

abstract class PrevBaseActivity(layoutId: Int) : GalleryBaseActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader, IGalleryPrevInterceptor {

    companion object {
        fun newInstance(
                context: Context,
                allList: ArrayList<ScanEntity>,
                selectList: ArrayList<ScanEntity> = ArrayList(),
                galleryBundle: GalleryBundle = GalleryBundle(),
                uiBundle: GalleryUiBundle = GalleryUiBundle(),
                position: Int = 0,
                option: Bundle = Bundle.EMPTY,
                cla: Class<out PrevBaseActivity>): Intent {
            val bundle = Bundle()
            bundle.putParcelableArrayList(GalleryConfig.PREV_START_ALL, allList)
            bundle.putParcelableArrayList(GalleryConfig.PREV_START_SELECT, selectList)
            bundle.putBundle(UIResult.PREV_START_BUNDLE, option)
            bundle.putParcelable(GalleryConfig.PREV_CONFIG, galleryBundle)
            bundle.putParcelable(UIResult.UI_CONFIG, uiBundle)
            bundle.putInt(GalleryConfig.PREV_START_POSITION, position)
            return Intent(context, cla).putExtras(bundle)
        }
    }

    /** 当前Fragment 文件Id,用于初始化[PrevFragment] */
    protected abstract val galleryFragmentId: Int

    /** ui 配置 */
    val uiBundle by lazy {
        bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle())
    }

    /** 初始配置 */
    val galleryBundle by lazy {
        bundleParcelableOrDefault<GalleryBundle>(GalleryConfig.PREV_CONFIG, GalleryBundle())
    }

    /** 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    val prevOption by lazy {
        bundleBundleExpand(UIResult.PREV_START_BUNDLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = PrevFragment.newInstance(
                bundleParcelableArrayListExpand(GalleryConfig.PREV_START_ALL),
                bundleParcelableArrayListExpand(GalleryConfig.PREV_START_SELECT),
                galleryBundle,
                bundleIntOrDefault(GalleryConfig.PREV_START_POSITION)
        ))
    }

    override fun onBackPressed() {
        val intent = Intent()
        val resultBundle: Bundle = prevFragment.resultBundle(uiBundle.preBackRefresh)
        onKeyBackResult(resultBundle)
        intent.putExtras(resultBundle)
        setResult(UIResult.PREV_BACk_FINISH_RESULT_CODE, intent)
        super.onBackPressed()
    }

    /** finish */
    open fun onPrevFinish() {
        val intent = Intent()
        val resultBundle: Bundle = prevFragment.resultBundle(uiBundle.preFinishRefresh)
        onToolbarFinishResult(resultBundle)
        intent.putExtras(resultBundle)
        setResult(UIResult.PREV_TOOLBAR_FINISH_RESULT_CODE, intent)
        finish()
    }

    /** back返回,可为Bundle插入需要的数据 */
    open fun onKeyBackResult(bundle: Bundle) {}

    /** toolbar返回,可为Bundle插入需要的数据 */
    open fun onToolbarFinishResult(bundle: Bundle) {}

    /** 选择数据为空 */
    open fun onGallerySelectEmpty() {
        getString(R.string.gallery_prev_select_empty_pre).toastExpand(this)
    }

    /**  prev select data */
    open fun onPrevSelectEntities() {
        val intent = Intent()
        intent.putExtras(prevFragment.resultBundle(true))
        setResult(UIResult.PREV_OK_FINISH_RESULT_CODE, intent)
        finish()
    }
}