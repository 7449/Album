package com.gallery.ui.page

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.text.safeToastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.ScanEntity
import com.gallery.core.delegate.galleryFragment
import com.gallery.core.expand.isVideoScanExpand
import com.gallery.scan.types.SCAN_NONE
import com.gallery.scan.types.isScanAllExpand
import com.gallery.ui.CropType
import com.gallery.ui.FinderType
import com.gallery.ui.R
import com.gallery.ui.activity.GalleryBaseActivity
import com.gallery.ui.adapter.BottomFinderAdapter
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.adapter.PopupFinderAdapter
import com.gallery.ui.crop.CropperImpl
import com.gallery.ui.crop.UCropImpl
import com.gallery.ui.engine.displayGallery
import com.gallery.ui.engine.displayGalleryThumbnails
import com.gallery.ui.obtain
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

open class GalleryActivity(layoutId: Int = R.layout.gallery_activity_gallery) : GalleryBaseActivity(layoutId),
        View.OnClickListener, GalleryFinderAdapter.AdapterFinderListener {

    private val newFinderAdapter: GalleryFinderAdapter by lazy {
        if (uiConfig.finderType == FinderType.POPUP) {
            return@lazy PopupFinderAdapter()
        } else {
            return@lazy BottomFinderAdapter()
        }
    }

    override val cropImpl: ICrop?
        get() = if (uiConfig.cropType == CropType.CROPPER) CropperImpl(uiConfig) else UCropImpl(uiConfig)

    override val currentFinderName: String
        get() = galleryFinderAll.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(uiConfig)
        newFinderAdapter.adapterInit(this, uiConfig, galleryFinderAll)
        newFinderAdapter.setOnAdapterFinderListener(this)
        galleryPre.setOnClickListener(this)
        gallerySelect.setOnClickListener(this)
        galleryFinderAll.setOnClickListener(this)

        galleryFinderAll.text = finderName
        galleryPre.visibility = if (galleryConfig.radio || galleryConfig.isVideoScanExpand) View.GONE else View.VISIBLE
        gallerySelect.visibility = if (galleryConfig.radio) View.GONE else View.VISIBLE

        galleryToolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (galleryFragment.selectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                onStartPrevPage(SCAN_NONE, 0, PreActivity::class.java)
            }
            R.id.gallerySelect -> {
                if (galleryFragment.selectEmpty) {
                    onGalleryOkEmpty()
                    return
                }
                onGalleryResources(galleryFragment.selectEntities)
            }
            R.id.galleryFinderAll -> {
                if (finderList.isEmpty()) {
                    onGalleryFinderEmpty()
                    return
                }
                newFinderAdapter.finderUpdate(finderList)
                newFinderAdapter.show()
            }
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        if (item.parent == galleryFragment.parentId) {
            newFinderAdapter.hide()
            return
        }
        galleryFinderAll.text = item.bucketDisplayName
        galleryFragment.onScanGallery(item.parent)
        newFinderAdapter.hide()
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(width: Int, height: Int, scanEntity: ScanEntity, container: FrameLayout, selectView: TextView) {
        container.displayGallery(width, height, scanEntity)
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.displayGalleryThumbnails(finderEntity)
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
        onStartPrevPage(parentId, if (parentId.isScanAllExpand() && !galleryBundle.hideCamera) position - 1 else position, PreActivity::class.java)
    }

    /** 点击预览但是未选择图片 */
    open fun onGalleryPreEmpty() {
        getString(R.string.gallery_prev_select_empty).safeToastExpand(this)
    }

    /** 点击确定但是未选择图片 */
    open fun onGalleryOkEmpty() {
        getString(R.string.gallery_ok_select_empty).safeToastExpand(this)
    }

    /** 扫描到的文件目录为空 */
    open fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).safeToastExpand(this)
    }
}