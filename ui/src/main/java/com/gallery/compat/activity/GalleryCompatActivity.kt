package com.gallery.compat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.gallery.compat.*
import com.gallery.compat.UIGalleryArgs.Companion.uiGalleryArgsOrDefault
import com.gallery.compat.UIGallerySaveArgs.Companion.putArgs
import com.gallery.compat.UIGallerySaveArgs.Companion.uiGallerySaveArgs
import com.gallery.compat.extensions.galleryFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.finder.findFinder
import com.gallery.compat.finder.updateResultFinder
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.addFragmentExpand
import com.gallery.compat.fragment.showFragmentExpand
import com.gallery.compat.widget.GalleryDivider
import com.gallery.core.GalleryBundle
import com.gallery.core.PrevArgs
import com.gallery.core.ScanArgs.Companion.scanArgs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.scan.types.Sort

abstract class GalleryCompatActivity : AppCompatActivity(), IGalleryCallback, IGalleryImageLoader, IGalleryInterceptor, ICrop {

    /** 当前文件夹名称,用于横竖屏保存数据 */
    protected abstract val currentFinderName: String

    /** 当前Fragment 文件Id,用于初始化[GalleryCompatFragment] */
    protected abstract val galleryFragmentId: Int

    /** 目录View */
    protected open val galleryFinderAdapter: GalleryFinderAdapter
        get() = throw KotlinNullPointerException("galleryFinderAdapter == null")

    /** [UIGalleryArgs] */
    @Suppress("MemberVisibilityCanBePrivate")
    protected val galleryArgs: UIGalleryArgs by lazy { intent?.extras.orEmptyExpand().uiGalleryArgsOrDefault }

    /** 初始配置 */
    protected val galleryConfig: GalleryBundle by lazy { galleryArgs.galleryBundle }

    /** ui 配置 */
    protected val uiConfig: GalleryUiBundle by lazy { galleryArgs.galleryUiBundle }

    /** 暂存Bundle,用于自定义布局时[GalleryUiBundle]无法满足需要配置时携带数据 */
    protected val uiGapConfig: Bundle by lazy { galleryArgs.galleryOption }

    /** 预览页启动[ActivityResultLauncher]  */
    private val prevLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        val bundleExpand: Bundle = intent?.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            PrevCompatActivity.RESULT_CODE_SELECT -> {
                galleryFragment.onUpdateResult(bundleExpand.scanArgs)
                onResultSelect(bundleExpand)
            }
            PrevCompatActivity.RESULT_CODE_TOOLBAR -> {
                galleryFragment.onUpdateResult(bundleExpand.scanArgs)
                onResultToolbar(bundleExpand)
            }
            PrevCompatActivity.RESULT_CODE_BACK -> {
                galleryFragment.onUpdateResult(bundleExpand.scanArgs)
                onResultBack(bundleExpand)
            }
        }
    }

    /** 文件夹数据集合 */
    val finderList = arrayListOf<ScanEntity>()

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
        finderList.addAll(saveArgs?.finderList.orEmpty())
        finderName = saveArgs?.finderName ?: galleryConfig.allName
        supportFragmentManager.findFragmentByTag(GalleryCompatFragment::class.java.simpleName)?.let {
            showFragmentExpand(fragment = it)
        } ?: addFragmentExpand(galleryFragmentId, fragment = createFragment())
    }

    /** 初始化布局，子类重写时需注意super */
    override fun onGalleryCreated(fragment: Fragment, recyclerView: RecyclerView, galleryBundle: GalleryBundle, savedInstanceState: Bundle?) {
        fragment.view?.setBackgroundColor(uiConfig.galleryRootBackground)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = when (uiConfig.layoutManager) {
            LayoutManagerTypes.GRID -> GridLayoutManager(recyclerView.context, galleryBundle.spanCount, uiConfig.orientation, false)
            LayoutManagerTypes.LINEAR -> LinearLayoutManager(recyclerView.context, uiConfig.orientation, false)
        }
        recyclerView.addItemDecoration(GalleryDivider(uiConfig.dividerWidth))
        if (recyclerView.itemAnimator is SimpleItemAnimator) {
            (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    /** 单个数据扫描成功之后刷新文件夹数据 */
    override fun onResultSuccess(context: Context?, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity, galleryConfig.scanSort == Sort.DESC)
    }

    /** 数据扫描成功之后刷新文件夹数据  该方法子类重写后需调用super 否则文件夹没数据,或者自己对文件夹进行初始化 */
    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (galleryFragment.isScanAll) {
            finderList.clear()
            finderList.addAll(scanEntities.findFinder(galleryConfig.sdName, galleryConfig.allName))
        }
    }

    /** 启动预览 */
    fun onStartPrevPage(parentId: Long, position: Int, cla: Class<out PrevCompatActivity>) {
        onStartPrevPage(parentId = parentId, position = position, option = galleryArgs.galleryPrevOption, cla = cla)
    }

    /** 启动预览 */
    fun onStartPrevPage(parentId: Long, position: Int, scanAlone: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_NONE, option: Bundle = Bundle.EMPTY, cla: Class<out PrevCompatActivity>) {
        onStartPrevPage(UIPrevArgs(uiConfig, PrevArgs(parentId, galleryFragment.selectEntities, galleryConfig, position, scanAlone), option), cla)
    }

    /** 启动预览 */
    open fun onStartPrevPage(uiPrevArgs: UIPrevArgs, cla: Class<out PrevCompatActivity>) {
        prevLauncher.launch(PrevCompatActivity.newInstance(this, uiPrevArgs, cla))
    }

    /** 自定义Fragment */
    open fun createFragment(): Fragment {
        return GalleryCompatFragment.newInstance(galleryConfig)
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
        setResult(GalleryConfig.RESULT_CODE_TOOLBAR_BACK)
        finish()
    }

    /** 选择图片,针对多选 */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(GalleryConfig.GALLERY_MULTIPLE_DATA, entities)
        intent.putExtras(bundle)
        setResult(GalleryConfig.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

    /** 点击选中,针对单选 */
    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(GalleryConfig.GALLERY_SINGLE_DATA, scanEntity)
        intent.putExtras(bundle)
        setResult(GalleryConfig.RESULT_CODE_SINGLE_DATA, intent)
        finish()
    }

    /** 文件目录加载图片 */
    abstract override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout)

    /** 文件加载图片 */
    abstract override fun onDisplayGallery(width: Int, height: Int, scanEntity: ScanEntity, container: FrameLayout, checkBox: TextView)
}