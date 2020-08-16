package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.*
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryConfig
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.expand.findFinder
import com.gallery.core.expand.isScanAll
import com.gallery.core.expand.updateResultFinder
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult


abstract class GalleryBaseActivity(layoutId: Int) : GalleryBaseActivity(layoutId), IGalleryCallback, IGalleryImageLoader, IGalleryInterceptor, ICrop {

    /** 当前文件夹名称,用于横竖屏保存数据 */
    protected abstract val currentFinderName: String

    /** 当前Fragment 文件Id,用于初始化[ScanFragment] */
    protected abstract val galleryFragmentId: Int

    /** ui 配置 */
    protected val uiConfig by lazy { bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle()) }

    /** 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    protected val uiGalleryConfig by lazy { bundleBundleExpand(UIResult.UI_GALLERY_CONFIG) }

    /** 预览页启动[ActivityResultLauncher],暂不对实现类开放  */
    private val prevLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        val bundleExpand: Bundle = intent?.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            UIResult.PREV_OK_RESULT_CODE -> {
                galleryFragment.onUpdateResult(bundleExpand)
                onResultSelect(bundleExpand)
            }
            UIResult.PREV_TOOLBAR_BACK_RESULT_CODE -> {
                galleryFragment.onUpdateResult(bundleExpand)
                onResultToolbar(bundleExpand)
            }
            UIResult.PREV_BACK_RESULT_CODE -> {
                galleryFragment.onUpdateResult(bundleExpand)
                onResultBack(bundleExpand)
            }
        }
    }

    /** 文件夹数据集合 */
    val finderList = ArrayList<ScanEntity>()

    /** 当前选中文件夹名称 */
    var finderName = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(UIResult.UI_FINDER_LIST, finderList)
        outState.putString(UIResult.UI_FINDER_NAME, currentFinderName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finderList.clear()
        finderList.addAll(savedInstanceState.getParcelableArrayListExpand(UIResult.UI_FINDER_LIST))
        finderName = savedInstanceState.getStringOrDefault(UIResult.UI_FINDER_NAME, galleryBundle.allName)
        supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = ScanFragment.newInstance(galleryBundle))
    }

    /** 单个数据扫描成功之后刷新文件夹数据 */
    override fun onResultSuccess(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity)
    }

    /** 数据扫描成功之后刷新文件夹数据  该方法重写后需调用super 否则文件夹没数据,或者自己对文件夹进行初始化 */
    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (galleryFragment.parentId.isScanAll() && scanEntities.isNotEmpty()) {
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(galleryBundle.sdName, galleryBundle.allName))
        }
    }

    /** 点击选中,针对单选 */
    override fun onGalleryResource(context: Context?, scanEntity: ScanEntity) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_SINGLE_DATA, scanEntity)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_SINGLE_RESULT_CODE, intent)
        finish()
    }

    /** 预览页toolbar返回 */
    open fun onResultToolbar(bundle: Bundle) {
    }

    /** 预览页back返回 */
    open fun onResultBack(bundle: Bundle) {
    }

    /**  预览页点击确定选择 */
    open fun onResultSelect(bundle: Bundle) {
        onGalleryResources(bundle.getParcelableArrayListExpand(GalleryConfig.GALLERY_SELECT))
    }

    /** 启动预览 */
    fun onStartPrevPage(parentId: Long, position: Int = 0, cla: Class<out PrevBaseActivity>) {
        onStartPrevPage(parentId, position, bundleBundleExpand(UIResult.UI_RESULT_CONFIG), cla)
    }

    /** 启动预览 */
    fun onStartPrevPage(parentId: Long, position: Int = 0, option: Bundle = Bundle.EMPTY, cla: Class<out PrevBaseActivity>) {
        prevLauncher.launch(PrevBaseActivity.newInstance(
                this,
                parentId,
                galleryFragment.selectEntities,
                galleryBundle,
                uiConfig,
                position,
                option,
                cla))
    }

    /** 用于 toolbar 返回 finish */
    open fun onGalleryFinish() {
        setResult(UIResult.UI_TOOLBAR_BACK_RESULT_CODE)
        finish()
    }

    /** 选择图片 */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.GALLERY_MULTIPLE_DATA, entities)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_MULTIPLE_RESULT_CODE, intent)
        finish()
    }
}