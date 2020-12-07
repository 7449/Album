package com.gallery.compat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.UIPrevArgs
import com.gallery.compat.UIPrevArgs.Companion.putPrevArgs
import com.gallery.compat.UIPrevArgs.Companion.uiPrevArgs
import com.gallery.compat.fragment.PrevCompatFragment
import com.gallery.compat.fragment.addFragmentExpand
import com.gallery.compat.fragment.showFragmentExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs.Companion.configOrDefault
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.core.extensions.safeToastExpand
import com.gallery.ui.R

abstract class PrevCompatActivity(layoutId: Int) : AppCompatActivity(layoutId), IGalleryPrevCallback, IGalleryImageLoader, IGalleryPrevInterceptor {

    companion object {
        /** 预览页toolbar返回 result_code */
        internal const val RESULT_CODE_TOOLBAR = -15

        /** 预览页back返回 result_code */
        internal const val RESULT_CODE_BACK = -16

        /** 预览页选中数据返回 result_code */
        internal const val RESULT_CODE_SELECT = -17

        fun newInstance(context: Context, uiPrevArgs: UIPrevArgs, cla: Class<out PrevCompatActivity>): Intent {
            return Intent(context, cla).putExtras(uiPrevArgs.putPrevArgs())
        }
    }

    /** 当前Fragment 文件Id,用于初始化[PrevCompatFragment] */
    protected abstract val galleryFragmentId: Int

    /** [UIPrevArgs] */
    protected val uiPrevArgs: UIPrevArgs by lazy {
        intent?.extras.orEmptyExpand().uiPrevArgs ?: throw KotlinNullPointerException("uiPrevArgs == null")
    }

    /** 初始配置 */
    protected val galleryConfig: GalleryBundle by lazy { uiPrevArgs.prevArgs.configOrDefault }

    /** ui 配置 */
    val uiConfig: GalleryUiBundle by lazy { uiPrevArgs.uiBundle }

    /**  暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    val uiGapConfig: Bundle by lazy { uiPrevArgs.option }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.findFragmentByTag(PrevCompatFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = createFragment())
    }

    override fun onPrevCreated(fragment: Fragment, galleryBundle: GalleryBundle, savedInstanceState: Bundle?) {
        fragment.view?.setBackgroundColor(uiConfig.prevPhotoBackgroundColor)
    }

    /** 自定义Fragment */
    open fun createFragment(): Fragment = PrevCompatFragment.newInstance(uiPrevArgs.prevArgs)

    /** back返回,可为Bundle插入需要的数据 */
    open fun onKeyBackResult(bundle: Bundle): Bundle = bundle

    /** toolbar返回,可为Bundle插入需要的数据 */
    open fun onToolbarFinishResult(bundle: Bundle): Bundle = bundle

    /** 预览页点击选择返回,可为Bundle插入需要的数据 */
    open fun onSelectEntitiesResult(bundle: Bundle): Bundle = bundle

    /** onBackPressed */
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtras(onKeyBackResult(prevFragment.resultBundle(uiConfig.preBackRefresh)))
        setResult(RESULT_CODE_BACK, intent)
        super.onBackPressed()
    }

    /** finish */
    open fun onGalleryFinish() {
        val intent = Intent()
        intent.putExtras(onToolbarFinishResult(prevFragment.resultBundle(uiConfig.preFinishRefresh)))
        setResult(RESULT_CODE_TOOLBAR, intent)
        finish()
    }

    /**  prev select data */
    open fun onGallerySelectEntities() {
        val intent = Intent()
        intent.putExtras(onSelectEntitiesResult(prevFragment.resultBundle(true)))
        setResult(RESULT_CODE_SELECT, intent)
        finish()
    }

    /** 选择数据为空 */
    open fun onGallerySelectEmpty() {
        getString(R.string.gallery_prev_select_empty_pre).safeToastExpand(this)
    }

    /** 预览图加载，预览页必须实现 */
    abstract override fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout)
}