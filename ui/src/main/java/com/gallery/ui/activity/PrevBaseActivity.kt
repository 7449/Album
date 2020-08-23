package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.*
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
import com.gallery.ui.UIResult

abstract class PrevBaseActivity(layoutId: Int) : GalleryBaseActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader, IGalleryPrevInterceptor {

    companion object {
        fun newInstance(
                context: Context,
                parentId: Long,
                selectList: ArrayList<ScanEntity>,
                galleryBundle: GalleryBundle,
                uiBundle: GalleryUiBundle,
                position: Int,
                scanAlone: Int,
                option: Bundle,
                cla: Class<out PrevBaseActivity>): Intent {
            val bundle = Bundle()
            /** ui library 数据 */
            bundle.putParcelable(UIResult.UI_CONFIG, uiBundle)
            bundle.putBundle(UIResult.UI_RESULT_CONFIG, option)
            /** core library 数据 */
            bundle.putParcelableArrayList(GalleryConfig.GALLERY_SELECT, selectList)
            bundle.putInt(GalleryConfig.GALLERY_POSITION, position)
            bundle.putInt(GalleryConfig.GALLERY_RESULT_SCAN_ALONE, scanAlone)
            bundle.putLong(GalleryConfig.GALLERY_PARENT_ID, parentId)
            bundle.putParcelable(GalleryConfig.GALLERY_CONFIG, galleryBundle)
            return Intent(context, cla).putExtras(bundle)
        }
    }

    /** 当前Fragment 文件Id,用于初始化[PrevFragment] */
    protected abstract val galleryFragmentId: Int

    /** ui 配置 */
    val uiConfig by lazy { bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle()) }

    /** 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    val uiResultConfig by lazy { bundleBundleExpand(UIResult.UI_RESULT_CONFIG) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (onInitDefaultFragment()) {
            supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)?.let {
                showFragmentExpand(fragment = it)
            } ?: addFragmentExpand(galleryFragmentId, fragment = PrevFragment.newInstance(
                    bundleLongOrDefault(GalleryConfig.GALLERY_PARENT_ID),
                    bundleParcelableArrayListExpand(GalleryConfig.GALLERY_SELECT),
                    galleryBundle,
                    bundleIntOrDefault(GalleryConfig.GALLERY_POSITION),
                    bundleIntOrDefault(GalleryConfig.GALLERY_RESULT_SCAN_ALONE, GalleryConfig.DEFAULT_SCAN_ALONE_TYPE)
            ))
        }
    }

    /** 是否初始化默认的Fragment */
    open fun onInitDefaultFragment(): Boolean = true

    /** back返回,可为Bundle插入需要的数据 */
    open fun onKeyBackResult(bundle: Bundle): Bundle {
        return bundle
    }

    /** toolbar返回,可为Bundle插入需要的数据 */
    open fun onToolbarFinishResult(bundle: Bundle): Bundle {
        return bundle
    }

    /** 预览页点击选择返回,可为Bundle插入需要的数据 */
    open fun onSelectEntitiesResult(bundle: Bundle): Bundle {
        return bundle
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtras(onKeyBackResult(prevFragment.resultBundle(uiConfig.preBackRefresh)))
        setResult(UIResult.PREV_BACK_RESULT_CODE, intent)
        super.onBackPressed()
    }

    /** finish */
    open fun onGalleryFinish() {
        val intent = Intent()
        intent.putExtras(onToolbarFinishResult(prevFragment.resultBundle(uiConfig.preFinishRefresh)))
        setResult(UIResult.PREV_TOOLBAR_BACK_RESULT_CODE, intent)
        finish()
    }

    /**  prev select data */
    open fun onGallerySelectEntities() {
        val intent = Intent()
        intent.putExtras(onSelectEntitiesResult(prevFragment.resultBundle(true)))
        setResult(UIResult.PREV_OK_RESULT_CODE, intent)
        finish()
    }

    /** 选择数据为空 */
    open fun onGallerySelectEmpty() {
        getString(com.gallery.ui.R.string.gallery_prev_select_empty_pre).toastExpand(this)
    }
}