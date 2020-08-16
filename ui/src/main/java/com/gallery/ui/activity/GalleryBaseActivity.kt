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
import com.gallery.ui.crop.UCropImpl


abstract class GalleryBaseActivity(layoutId: Int) : GalleryBaseActivity(layoutId), IGalleryCallback, IGalleryImageLoader, IGalleryInterceptor, ICrop {

    /** 当前文件夹名称,用于横竖屏保存数据 */
    protected abstract val currentFinderName: String

    /** 当前Fragment 文件Id,用于初始化[ScanFragment] */
    protected abstract val galleryFragmentId: Int

    /** 初始配置 */
    protected val galleryBundle by lazy {
        bundleParcelableOrDefault<GalleryBundle>(GalleryConfig.GALLERY_CONFIG, GalleryBundle())
    }

    /** ui 配置 */
    protected val galleryUiBundle by lazy {
        bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle())
    }

    /** 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    protected val galleryOption by lazy {
        bundleBundleExpand(UIResult.GALLERY_START_BUNDLE)
    }

    /** 预览页启动[ActivityResultLauncher],暂不对实现类开放  */
    private val prevLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        val bundleExpand: Bundle = intent?.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            UIResult.PREV_OK_FINISH_RESULT_CODE -> {
                galleryFragment.onUpdatePrevResult(bundleExpand)
                onPrevSelectBack(bundleExpand)
            }
            UIResult.PREV_TOOLBAR_FINISH_RESULT_CODE -> {
                galleryFragment.onUpdatePrevResult(bundleExpand)
                onPrevToolbarFinish(bundleExpand)
            }
            UIResult.PREV_BACk_FINISH_RESULT_CODE -> {
                galleryFragment.onUpdatePrevResult(bundleExpand)
                onPrevKeyBack(bundleExpand)
            }
        }
    }

    /** 文件夹数据集合 */
    val finderList = ArrayList<ScanEntity>()

    /** 当前选中文件夹名称 */
    var finderName = ""

    override val cropImpl: ICrop?
        get() = UCropImpl(galleryFragment, galleryBundle, galleryUiBundle)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(UIResult.FINDER_LIST, finderList)
        outState.putString(UIResult.FINDER_NAME, currentFinderName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finderList.clear()
        finderList.addAll(savedInstanceState.getParcelableArrayListExpand(UIResult.FINDER_LIST))
        finderName = savedInstanceState.getStringOrDefault(UIResult.FINDER_NAME, galleryBundle.allName)
        supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        }
                ?: addFragmentExpand(galleryFragmentId, fragment = ScanFragment.newInstance(galleryBundle))
    }

    /** 数据扫描成功之后刷新文件夹数据 */
    override fun onResultSuccess(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity)
    }

    /** 数据扫描成功之后刷新文件夹数据  该方法重写后需调用super 否则文件夹没数据,或者自己对文件夹进行初始化 */
    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (galleryFragment.parentId.isScanAll() && scanEntities.isNotEmpty()) {
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(
                    galleryBundle.sdName,
                    galleryBundle.allName
            ))
        }
    }

    /** 点击选中,针对单选 */
    override fun onGalleryResource(context: Context?, scanEntity: ScanEntity) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_ENTITY, scanEntity)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_RESULT_RESOURCE, intent)
        finish()
    }

    /** 预览页toolbar返回 */
    open fun onPrevToolbarFinish(bundle: Bundle) {
    }

    /** 预览页back返回 */
    open fun onPrevKeyBack(bundle: Bundle) {
    }

    /**  预览页点击确定选择 */
    open fun onPrevSelectBack(bundle: Bundle) {
        onGalleryResources(bundle.getParcelableArrayListExpand(GalleryConfig.PREV_RESULT_SELECT))
    }

    /** 启动预览 */
    fun onStartPrevPage(
            parentId: Long,
            position: Int = 0,
            cla: Class<out PrevBaseActivity>) {
        onStartPrevPage(parentId, position, bundleBundleExpand(UIResult.PREV_START_BUNDLE), cla)
    }

    /** 启动预览 */
    fun onStartPrevPage(
            parentId: Long,
            position: Int = 0,
            option: Bundle = Bundle.EMPTY,
            cla: Class<out PrevBaseActivity>) {
        prevLauncher.launch(PrevBaseActivity.newInstance(
                this,
                parentId,
                galleryFragment.selectEntities,
                galleryBundle,
                galleryUiBundle,
                position,
                option,
                cla))
    }

    /** 用于 toolbar 返回 finish */
    open fun onGalleryFinish() {
        setResult(UIResult.GALLERY_FINISH_RESULT_CODE)
        finish()
    }

    /** 选择图片 */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.GALLERY_RESULT_ENTITIES, entities)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_RESULT_RESOURCES, intent)
        finish()
    }
}