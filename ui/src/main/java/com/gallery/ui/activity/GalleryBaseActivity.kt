package com.gallery.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.findFragmentByTagExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleParcelableOrDefault
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getStringOrDefault
import androidx.kotlin.expand.os.orEmptyExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.*
import com.gallery.core.ext.updateResultFinder
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.UIResult
import com.yalantis.ucrop.UCrop

@Suppress("MemberVisibilityCanBePrivate")
abstract class GalleryBaseActivity(layoutId: Int) : GalleryBaseActivity(layoutId), IGalleryCallback, IGalleryImageLoader, IGalleryInterceptor {

    protected abstract val currentFinderName: String

    protected abstract val galleryFragmentId: Int

    val galleryBundle by lazy {
        bundleParcelableOrDefault<GalleryBundle>(IGallery.GALLERY_START_CONFIG, GalleryBundle())
    }
    val galleryUiBundle by lazy {
        bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle())
    }
    val prevLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
        val bundleExpand: Bundle = intent?.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            UIResult.PREV_OK_FINISH_RESULT_CODE -> {
                galleryFragment.onUpdatePrevResult(bundleExpand)
                onPrevSelectEntities(bundleExpand.getParcelableArrayListExpand(IGalleryPrev.PREV_RESULT_SELECT))
            }
            UIResult.PREV_TOOLBAR_FINISH_RESULT_CODE -> {
                galleryFragment.onUpdatePrevResult(bundleExpand.orEmptyExpand())
            }
            UIResult.PREV_BACk_FINISH_RESULT_CODE -> {
                galleryFragment.onUpdatePrevResult(bundleExpand.orEmptyExpand())
            }
        }
    }
    val finderList = ArrayList<ScanEntity>()
    var finderName = ""

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
        findFragmentByTagExpand(ScanFragment::class.java.simpleName) {
            if (it == null) {
                addFragmentExpand(galleryFragmentId, ScanFragment.newInstance(galleryBundle))
            } else {
                showFragmentExpand(it)
            }
        }
    }

    override fun onScanResultSuccess(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity)
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        onGalleryResource(scanEntity)
    }

    override fun onUCropOptions() = UCrop.Options().apply { this.optionBundle.putAll(galleryUiBundle.uCropBundle) }

    override fun onUCropResources(uri: Uri) {
        onGalleryCropResource(uri)
    }

    /**
     * 启动预览
     */
    open fun onStartPrevPage(
            allList: ArrayList<ScanEntity>,
            position: Int = 0,
            cla: Class<out PrevBaseActivity>) {
        prevLauncher.launch(PrevBaseActivity.newInstance(
                this,
                allList,
                galleryFragment.selectEntities,
                galleryBundle,
                galleryUiBundle,
                position,
                cla))
    }

    /**
     * 预览页点击确定选择
     */
    open fun onPrevSelectEntities(entities: ArrayList<ScanEntity>) {
        onGalleryResources(entities)
    }

    /**
     * finish
     */
    open fun onGalleryFinish() {
        setResult(UIResult.GALLERY_FINISH_RESULT_CODE)
        finish()
    }

    /**
     * 裁剪成功
     */
    open fun onGalleryCropResource(uri: Uri) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_URI, uri)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_RESULT_CROP, intent)
        finish()
    }

    /**
     * 单选不裁剪
     */
    open fun onGalleryResource(scanEntity: ScanEntity) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.GALLERY_RESULT_ENTITY, scanEntity)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_RESULT_RESOURCE, intent)
        finish()
    }

    /**
     * 选择图片
     */
    open fun onGalleryResources(entities: ArrayList<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.GALLERY_RESULT_ENTITIES, entities)
        intent.putExtras(bundle)
        setResult(UIResult.GALLERY_RESULT_RESOURCES, intent)
        finish()
    }
}