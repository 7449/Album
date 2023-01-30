package com.gallery.compat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.gallery.compat.GalleryConfig
import com.gallery.compat.activity.args.GalleryCompatArgs
import com.gallery.compat.activity.args.GalleryCompatArgs.Companion.galleryCompatArgsOrDefault
import com.gallery.compat.activity.args.GallerySaveArgs
import com.gallery.compat.activity.args.GallerySaveArgs.Companion.gallerySaveArgs
import com.gallery.compat.activity.args.GallerySaveArgs.Companion.toBundle
import com.gallery.compat.activity.args.PrevCompatArgs
import com.gallery.compat.extensions.galleryFragment
import com.gallery.compat.extensions.requireGalleryFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.finder.findFinder
import com.gallery.compat.finder.updateResultFinder
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.fragment.addFragment
import com.gallery.compat.fragment.showFragment
import com.gallery.compat.internal.simple.SimpleGalleryCallback
import com.gallery.core.GalleryConfigs
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.args.PrevArgs
import com.gallery.core.delegate.args.ScanArgs.Companion.scanArgs
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.orEmpty
import com.gallery.scan.Types

abstract class GalleryCompatActivity : AppCompatActivity(), SimpleGalleryCallback,
    IGalleryImageLoader,
    IGalleryInterceptor, ICrop {

    /** 当前文件夹名称,用于横竖屏保存数据 */
    protected abstract val currentFinderName: String

    /** 当前FragmentId */
    protected abstract val galleryFragmentId: Int

    /** Finder */
    protected open val finderAdapter: GalleryFinderAdapter
        get() = throw KotlinNullPointerException("finderAdapter == null")

    /** [GalleryCompatArgs] */
    @Suppress("MemberVisibilityCanBePrivate")
    protected val galleryCompatArgs: GalleryCompatArgs by lazy { intent?.extras.orEmpty().galleryCompatArgsOrDefault }

    /** 初始配置 */
    protected val galleryConfig: GalleryConfigs by lazy { galleryCompatArgs.bundle }

    /** 自定义参数配置 */
    protected val gapConfig: Parcelable? by lazy { galleryCompatArgs.gap }

    /** 预览页启动[ActivityResultLauncher]  */
    private val prevLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
            val bundle: Bundle = intent?.data?.extras.orEmpty()
            val scanArgs = bundle.scanArgs ?: return@registerForActivityResult
            when (intent.resultCode) {
                PrevCompatActivity.RESULT_CODE_SELECT -> {
                    galleryFragment?.onUpdateResult(scanArgs)
                    onResultSelect(bundle)
                }

                PrevCompatActivity.RESULT_CODE_TOOLBAR -> {
                    galleryFragment?.onUpdateResult(scanArgs)
                    onResultToolbar(bundle)
                }

                PrevCompatActivity.RESULT_CODE_BACK -> {
                    galleryFragment?.onUpdateResult(scanArgs)
                    onResultBack(bundle)
                }
            }
        }

    /** 文件夹数据集合 */
    val finderList = arrayListOf<ScanEntity>()

    /** 当前选中文件夹名称 */
    var finderName = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        GallerySaveArgs.onSaveInstanceState(currentFinderName, finderList).toBundle(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val saveArgs = savedInstanceState?.gallerySaveArgs
        finderList.clear()
        finderList.addAll(saveArgs?.finderList.orEmpty())
        finderName = saveArgs?.finderName ?: galleryConfig.sdNameAndAllName.second
        galleryFragment?.let {
            showFragment(fragment = it)
        } ?: addFragment(galleryFragmentId, fragment = createFragment())
    }

    /** 单个数据扫描成功之后刷新文件夹数据 */
    override fun onResultSuccess(context: Context, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity, galleryConfig.sort.first == Types.Sort.DESC)
    }

    /** 数据扫描成功之后刷新文件夹数据  该方法子类重写后需调用super 否则文件夹没数据,或者自己对文件夹进行初始化 */
    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
        if (requireGalleryFragment.isScanAll) {
            finderList.clear()
            finderList.addAll(
                scanEntities.findFinder(
                    galleryConfig.sdNameAndAllName.first,
                    galleryConfig.sdNameAndAllName.second
                )
            )
        }
    }

    /** 启动预览 */
    fun startPrevPage(
        parentId: Long,
        position: Int,
        gap: Parcelable?,
        scanSingleType: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_NONE,
        cla: Class<out PrevCompatActivity>
    ) {
        startPrevPage(
            PrevCompatArgs(
                PrevArgs(
                    parentId,
                    requireGalleryFragment.selectItem,
                    galleryConfig,
                    position,
                    scanSingleType
                ), gap
            ), cla
        )
    }

    /** 启动预览 */
    open fun startPrevPage(args: PrevCompatArgs, cla: Class<out PrevCompatActivity>) {
        prevLauncher.launch(PrevCompatActivity.newInstance(this, args, cla))
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

    /** 用于toolbar返回 finish */
    open fun onGalleryFinish() {
        setResult(GalleryConfig.RESULT_CODE_TOOLBAR_BACK)
        finish()
    }

    /** 选择图片,针对多选 */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        intent.putExtras(bundleOf(GalleryConfig.GALLERY_MULTIPLE_DATA to entities))
        setResult(GalleryConfig.RESULT_CODE_MULTIPLE_DATA, intent)
        finish()
    }

    /** 选择图片,针对单选 */
    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        val intent = Intent()
        intent.putExtras(bundleOf(GalleryConfig.GALLERY_SINGLE_DATA to scanEntity))
        setResult(GalleryConfig.RESULT_CODE_SINGLE_DATA, intent)
        finish()
    }

    /** 初始化布局 */
    abstract override fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    )

    /** 文件目录加载图片,此方法需要在自定义Finder的时候主动调用,或者自定义的时候直接加载图片即可 */
    abstract override fun onDisplayGalleryThumbnails(
        finderEntity: ScanEntity,
        container: FrameLayout
    )

    /** 文件加载图片 */
    abstract override fun onDisplayGallery(
        width: Int,
        height: Int,
        scanEntity: ScanEntity,
        container: FrameLayout,
        checkBox: TextView
    )

    override fun onDestroy() {
        super.onDestroy()
        prevLauncher.unregister()
    }

}