package com.gallery.compat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gallery.compat.activity.args.PrevCompatArgs
import com.gallery.compat.activity.args.PrevCompatArgs.Companion.prevCompatArgsOrDefault
import com.gallery.compat.activity.args.PrevCompatArgs.Companion.toBundle
import com.gallery.compat.extensions.intentResultOf
import com.gallery.compat.extensions.prevFragment
import com.gallery.compat.extensions.requirePrevFragment
import com.gallery.compat.fragment.PrevCompatFragment
import com.gallery.compat.fragment.addFragment
import com.gallery.compat.fragment.showFragment
import com.gallery.compat.internal.simple.SimplePrevCallback
import com.gallery.core.args.GalleryConfigs
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.orEmpty

abstract class PrevCompatActivity : AppCompatActivity(), SimplePrevCallback, IGalleryImageLoader,
    IGalleryPrevInterceptor {

    companion object {
        /** 预览页toolbar返回 result_code */
        internal const val RESULT_CODE_TOOLBAR = -15

        /** 预览页back返回 result_code */
        internal const val RESULT_CODE_BACK = -16

        /** 预览页选中数据返回 result_code */
        internal const val RESULT_CODE_SELECT = -17

        fun newInstance(
            context: Context,
            args: PrevCompatArgs,
            cla: Class<out PrevCompatActivity>
        ): Intent {
            return Intent(context, cla).putExtras(args.toBundle())
        }
    }

    /** 当前FragmentId */
    protected abstract val galleryFragmentId: Int

    /** [PrevCompatArgs] */
    protected val prevCompatArgs: PrevCompatArgs by lazy {
        intent?.extras.orEmpty().prevCompatArgsOrDefault
    }

    /** 初始配置 */
    protected val galleryConfig: GalleryConfigs by lazy {
        prevCompatArgs.prevArgs.config ?: GalleryConfigs()
    }

    /** 自定义参数配置 */
    protected val gapConfig: Parcelable? by lazy { prevCompatArgs.gap }

    private val rootFragment get() = requirePrevFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prevFragment?.let {
            showFragment(fragment = it)
        } ?: addFragment(galleryFragmentId, fragment = prevRootFragment)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                intentResultOf(RESULT_CODE_BACK, backResult(rootFragment.resultBundle(backRefresh)))
            }
        })
    }

    /** finish */
    open fun onGalleryFinish() {
        intentResultOf(RESULT_CODE_TOOLBAR, toolbarResult(rootFragment.resultBundle(finishRefresh)))
    }

    /**  prev select data */
    open fun onGallerySelectEntities() {
        intentResultOf(RESULT_CODE_SELECT, selectResult(rootFragment.resultBundle(selectRefresh)))
    }

    /** back返回是否合并选择数据并刷新 */
    open val backRefresh: Boolean = true

    /** toolbar返回是否合并选择数据并刷新 */
    open val finishRefresh: Boolean = true

    /** 选择数据之后是否合并选择数据并刷新 */
    open val selectRefresh: Boolean = true

    /** 自定义Fragment */
    open val prevRootFragment: Fragment get() = PrevCompatFragment.newInstance(prevCompatArgs.prevArgs)

    /** back返回,可为Bundle插入需要的数据 */
    open fun backResult(bundle: Bundle): Bundle = bundle

    /** toolbar返回,可为Bundle插入需要的数据 */
    open fun toolbarResult(bundle: Bundle): Bundle = bundle

    /** 预览页点击选择返回,可为Bundle插入需要的数据 */
    open fun selectResult(bundle: Bundle): Bundle = bundle

    /** 预览图加载，预览页必须实现 */
    abstract override fun onDisplayPrevGallery(entity: ScanEntity, container: FrameLayout)

    /** 预览图初始化，预览页必须实现 ，可实现背景色之类的配置 */
    abstract override fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?)

}