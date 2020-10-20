package com.gallery.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.kotlin.expand.text.safeToastExpand
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.core.extensions.isVideoScanExpand
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.scan.types.ScanType
import com.gallery.ui.R
import com.gallery.ui.activity.base.GalleryBaseActivity
import com.gallery.ui.activity.ext.galleryFragment
import com.gallery.ui.activity.ext.obtain
import com.gallery.ui.crop.CropperImpl
import com.gallery.ui.crop.UCropImpl
import com.gallery.ui.finder.BottomFinderAdapter
import com.gallery.ui.finder.GalleryFinderAdapter
import com.gallery.ui.finder.PopupFinderAdapter
import com.gallery.ui.result.CropType
import com.gallery.ui.result.FinderType
import com.gallery.ui.widget.GalleryImageView
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
                onStartPrevPage(ScanType.SCAN_NONE, 0, PreActivity::class.java)
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

    override fun onDisplayGallery(width: Int, height: Int, scanEntity: ScanEntity, container: FrameLayout, checkBox: TextView) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(scanEntity.uri).apply(RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop().override(width, height)).into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(finderEntity.uri).apply(RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()).into(imageView)
        container.addView(imageView)
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