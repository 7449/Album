package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleOrEmptyExpand
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.PrevArgs.Companion.configOrDefault
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.delegate.prevFragment
import com.gallery.core.ui.fragment.PrevFragment
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIPrevArgs
import com.gallery.ui.UIPrevArgs.Companion.putPrevArgs
import com.gallery.ui.UIPrevArgs.Companion.uiPrevArgs
import com.gallery.ui.UIResult

abstract class PrevBaseActivity(layoutId: Int) : AppCompatActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader, IGalleryPrevInterceptor {

    companion object {
        fun newInstance(context: Context, uiPrevArgs: UIPrevArgs, cla: Class<out PrevBaseActivity>): Intent {
            return Intent(context, cla).putExtras(uiPrevArgs.putPrevArgs())
        }
    }

    /** 当前Fragment 文件Id,用于初始化[PrevFragment] */
    protected abstract val galleryFragmentId: Int

    /** [UIPrevArgs] */
    protected val uiPrevArgs by lazy { bundleOrEmptyExpand().uiPrevArgs ?: throw KotlinNullPointerException("uiPrevArgs == null") }

    /** 初始配置 */
    protected val galleryBundle by lazy { uiPrevArgs.prevArgs.configOrDefault }

    /** ui 配置 */
    val uiConfig by lazy { uiPrevArgs.uiBundle }

    /**  暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    val uiResultConfig by lazy { uiPrevArgs.option }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = createFragment())
    }

    /** 自定义Fragment */
    open fun createFragment(): Fragment {
        return PrevFragment.newInstance(uiPrevArgs.prevArgs)
    }

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

    /** onBackPressed */
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