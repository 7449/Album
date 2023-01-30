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
import com.gallery.compat.extensions.prevFragment
import com.gallery.compat.extensions.requirePrevFragment
import com.gallery.compat.fragment.PrevCompatFragment
import com.gallery.compat.fragment.addFragment
import com.gallery.compat.fragment.showFragment
import com.gallery.compat.internal.simple.SimplePrevCallback
import com.gallery.core.GalleryConfigs
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryPrevInterceptor
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.PrevArgs.Companion.configOrDefault
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.orEmpty

abstract class PrevCompatActivity : AppCompatActivity(), SimplePrevCallback, IGalleryImageLoader,
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
             * [PrevArgs]
             * and
             * [Parcelable] // 用于存放以及获取自定义数据
             */
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
    protected val galleryConfig: GalleryConfigs by lazy { prevCompatArgs.prevArgs.configOrDefault }

    /** 自定义参数配置 */
    protected val gapConfig: Parcelable? by lazy { prevCompatArgs.gap }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prevFragment?.let {
            showFragment(fragment = it)
        } ?: addFragment(galleryFragmentId, fragment = createFragment())
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent()
                intent.putExtras(onKeyBackResult(requirePrevFragment.resultBundle(onBackRefresh())))
                setResult(RESULT_CODE_BACK, intent)
                finish()
            }
        })
    }

    /** finish */
    open fun onGalleryFinish() {
        val intent = Intent()
        intent.putExtras(onToolbarResult(requirePrevFragment.resultBundle(onFinishRefresh())))
        setResult(RESULT_CODE_TOOLBAR, intent)
        finish()
    }

    /**  prev select data */
    open fun onGallerySelectEntities() {
        val intent = Intent()
        intent.putExtras(onSelectEntitiesResult(requirePrevFragment.resultBundle(onSelectRefresh())))
        setResult(RESULT_CODE_SELECT, intent)
        finish()
    }

    /** back返回是否合并选择数据并刷新 */
    open fun onBackRefresh(): Boolean = true

    /** toolbar返回是否合并选择数据并刷新 */
    open fun onFinishRefresh(): Boolean = true

    /** 选择数据之后是否合并选择数据并刷新 */
    open fun onSelectRefresh(): Boolean = true

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

    /** 预览图初始化，预览页必须实现 ，可实现背景色之类的配置 */
    abstract override fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    )

}