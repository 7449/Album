package com.gallery.compat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gallery.compat.GalleryCompatBundle
import com.gallery.compat.activity.args.PrevCompatArgs
import com.gallery.compat.activity.args.PrevCompatArgs.Companion.prevCompatArgsOrDefault
import com.gallery.compat.activity.args.PrevCompatArgs.Companion.putPrevArgs
import com.gallery.compat.extensions.requirePrevFragment
import com.gallery.compat.fragment.PrevCompatFragment
import com.gallery.compat.fragment.addFragmentExpand
import com.gallery.compat.fragment.showFragmentExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.configOrDefault
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.orEmptyExpand

abstract class PrevCompatActivity : AppCompatActivity(), IGalleryPrevCallback, IGalleryImageLoader,
    IGalleryPrevInterceptor {

    companion object {
        /** 预览页toolbar返回 result_code */
        const val RESULT_CODE_TOOLBAR = -15

        /** 预览页back返回 result_code */
        const val RESULT_CODE_BACK = -16

        /** 预览页选中数据返回 result_code */
        const val RESULT_CODE_SELECT = -17

        fun newInstance(
            context: Context,
            /**
             * [GalleryCompatBundle]
             * and
             * [PrevArgs]
             * and
             * [Parcelable] // 用于存放以及获取自定义数据
             */
            args: PrevCompatArgs,
            cla: Class<out PrevCompatActivity>
        ): Intent {
            return Intent(context, cla).putExtras(args.putPrevArgs())
        }
    }

    /** 当前Fragment 文件Id,用于初始化[PrevCompatFragment] */
    protected abstract val galleryFragmentId: Int

    /** [PrevCompatArgs] */
    protected val prevCompatArgs: PrevCompatArgs by lazy {
        intent?.extras.orEmptyExpand().prevCompatArgsOrDefault
    }

    /** 初始配置 */
    protected val galleryConfig: GalleryBundle by lazy { prevCompatArgs.prevArgs.configOrDefault }

    /** compat配置  */
    @Suppress("MemberVisibilityCanBePrivate")
    protected val compatConfig: GalleryCompatBundle by lazy { prevCompatArgs.compatBundle }

    /** 自定义参数配置 */
    protected val gapConfig: Parcelable? by lazy { prevCompatArgs.customBundle }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.findFragmentByTag(PrevCompatFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = createFragment())
    }

    override fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
        delegate.rootView.setBackgroundColor(compatConfig.prevRootBackground)
    }

    /** onBackPressed */
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtras(onKeyBackResult(requirePrevFragment.resultBundle(compatConfig.preBackRefresh)))
        setResult(RESULT_CODE_BACK, intent)
        super.onBackPressed()
    }

    /** finish */
    open fun onGalleryFinish() {
        val intent = Intent()
        intent.putExtras(onToolbarResult(requirePrevFragment.resultBundle(compatConfig.preFinishRefresh)))
        setResult(RESULT_CODE_TOOLBAR, intent)
        finish()
    }

    /**  prev select data */
    open fun onGallerySelectEntities() {
        val intent = Intent()
        intent.putExtras(onSelectEntitiesResult(requirePrevFragment.resultBundle(true)))
        setResult(RESULT_CODE_SELECT, intent)
        finish()
    }

    /** 自定义Fragment */
    open fun createFragment(): Fragment = PrevCompatFragment.newInstance(prevCompatArgs.prevArgs)

    /** back返回,可为Bundle插入需要的数据 */
    open fun onKeyBackResult(bundle: Bundle): Bundle = bundle

    /** toolbar返回,可为Bundle插入需要的数据 */
    open fun onToolbarResult(bundle: Bundle): Bundle = bundle

    /** 预览页点击选择返回,可为Bundle插入需要的数据 */
    open fun onSelectEntitiesResult(bundle: Bundle): Bundle = bundle

    /** 预览图加载，预览页必须实现 */
    abstract override fun onDisplayGalleryPrev(scanEntity: ScanEntity, container: FrameLayout)

}