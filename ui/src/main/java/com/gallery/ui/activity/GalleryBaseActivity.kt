package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleOrEmptyExpand
import androidx.kotlin.expand.os.orEmptyExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs
import com.gallery.core.ScanArgs.Companion.scanArgs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.ScanEntity
import com.gallery.core.delegate.galleryFragment
import com.gallery.core.expand.findFinder
import com.gallery.core.expand.updateResultFinder
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.types.Sort
import com.gallery.scan.types.isScanAllExpand
import com.gallery.ui.*
import com.gallery.ui.UIGalleryArgs.Companion.uiGalleryArgsOrDefault
import com.gallery.ui.UIGallerySaveArgs.Companion.putArgs
import com.gallery.ui.UIGallerySaveArgs.Companion.uiGallerySaveArgs

abstract class GalleryBaseActivity(layoutId: Int) : AppCompatActivity(layoutId), IGalleryCallback, IGalleryImageLoader, IGalleryInterceptor, ICrop {

    /** 当前文件夹名称,用于横竖屏保存数据 */
    protected abstract val currentFinderName: String

    /** 当前Fragment 文件Id,用于初始化[ScanFragment] */
    protected abstract val galleryFragmentId: Int

    /** [UIGalleryArgs] */
    protected val galleryArgs by lazy { bundleOrEmptyExpand().uiGalleryArgsOrDefault }

    /** 初始配置 */
    protected val galleryConfig by lazy { galleryArgs.galleryBundle }

    /** ui 配置 */
    protected val uiConfig by lazy { galleryArgs.galleryUiBundle }

    /** 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    protected val uiGapConfig by lazy { galleryArgs.galleryOption }

    /** 预览页启动[ActivityResultLauncher],暂不对实现类开放  */
    private val prevLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        val bundleExpand: Bundle = intent?.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            PrevBaseActivity.RESULT_CODE_SELECT -> {
                galleryFragment.onUpdateResult(bundleExpand.scanArgs)
                onResultSelect(bundleExpand)
            }
            PrevBaseActivity.RESULT_CODE_TOOLBAR -> {
                galleryFragment.onUpdateResult(bundleExpand.scanArgs)
                onResultToolbar(bundleExpand)
            }
            PrevBaseActivity.RESULT_CODE_BACK -> {
                galleryFragment.onUpdateResult(bundleExpand.scanArgs)
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
        UIGallerySaveArgs.newSaveInstance(currentFinderName, finderList).putArgs(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val saveArgs = savedInstanceState?.uiGallerySaveArgs
        finderList.clear()
        finderList.addAll(saveArgs?.finderList ?: arrayListOf())
        finderName = saveArgs?.finderName ?: galleryConfig.allName
        supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = createFragment())
    }

    /** 单个数据扫描成功之后刷新文件夹数据 */
    override fun onResultSuccess(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity, galleryBundle.scanSort == Sort.DESC)
    }

    /** 数据扫描成功之后刷新文件夹数据  该方法重写后需调用super 否则文件夹没数据,或者自己对文件夹进行初始化 */
    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (galleryFragment.parentId.isScanAllExpand() && scanEntities.isNotEmpty()) {
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(galleryConfig.sdName, galleryConfig.allName))
        }
    }

    /** 启动预览 */
    fun onStartPrevPage(parentId: Long, position: Int, cla: Class<out PrevBaseActivity>) {
        onStartPrevPage(parentId = parentId, position = position, option = galleryArgs.galleryPrevOption, cla = cla)
    }

    /** 启动预览 */
    fun onStartPrevPage(parentId: Long, position: Int, scanAlone: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_NONE, option: Bundle = Bundle.EMPTY, cla: Class<out PrevBaseActivity>) {
        onStartPrevPage(UIPrevArgs(uiConfig, PrevArgs(parentId, galleryFragment.selectEntities, galleryConfig, position, scanAlone), option), cla)
    }

    /** 自定义Fragment */
    open fun createFragment(): Fragment {
        return ScanFragment.newInstance(galleryConfig)
    }

    /** 启动预览 */
    open fun onStartPrevPage(uiPrevArgs: UIPrevArgs, cla: Class<out PrevBaseActivity>) {
        prevLauncher.launch(PrevBaseActivity.newInstance(this, uiPrevArgs, cla))
    }

    /** 预览页toolbar返回 */
    open fun onResultToolbar(bundle: Bundle) {
    }

    /** 预览页back返回 */
    open fun onResultBack(bundle: Bundle) {
    }

    /** 预览页确定选择 */
    open fun onResultSelect(bundle: Bundle) {
        onGalleryResources(bundle.scanArgs?.selectList ?: arrayListOf())
    }

    /** 用于 toolbar 返回 finish */
    open fun onGalleryFinish() {
        setResult(UIResult.RESULT_CODE_TOOLBAR_BACK)
        finish()
    }

    /** 选择图片,针对多选 */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.GALLERY_MULTIPLE_DATA, entities)
        intent.putExtras(bundle)
        setResult(UIResult.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

    /** 点击选中,针对单选 */
    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_SINGLE_DATA, scanEntity)
        intent.putExtras(bundle)
        setResult(UIResult.RESULT_CODE_SINGLE_DATA, intent)
        finish()
    }
}