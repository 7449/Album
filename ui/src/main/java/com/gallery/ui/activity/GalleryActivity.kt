package com.gallery.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.kotlin.expand.text.toastExpand
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.findFinder
import com.gallery.core.ext.isScanAll
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.FinderType
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.adapter.BottomFinderAdapter
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.adapter.PopupFinderAdapter
import com.gallery.ui.obtain
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

open class GalleryActivity(layoutId: Int = R.layout.gallery_activity_gallery) : GalleryBaseActivity(layoutId),
        View.OnClickListener, GalleryFinderAdapter.AdapterFinderListener {

    private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()
    private val newFinderAdapter: GalleryFinderAdapter by lazy {
        if (galleryUiBundle.finderType == FinderType.POPUP) {
            return@lazy PopupFinderAdapter()
        } else {
            return@lazy BottomFinderAdapter()
        }
    }

    override val adapterGalleryUiBundle: GalleryUiBundle
        get() = galleryUiBundle

    override val currentFinderName: String
        get() = galleryFinderAll.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(galleryUiBundle)
        newFinderAdapter.setOnAdapterFinderListener(this)
        newFinderAdapter.onGalleryFinderInit(this, galleryFinderAll)
        galleryPre.setOnClickListener(this)
        gallerySelect.setOnClickListener(this)
        galleryFinderAll.setOnClickListener(this)

        galleryFinderAll.text = finderName
        galleryPre.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        gallerySelect.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE

        galleryToolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (galleryFragment.selectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                onStartPrevPage(
                        galleryFragment.selectEntities,
                        0,
                        PreActivity::class.java
                )
            }
            R.id.gallerySelect -> {
                if (galleryFragment.selectEmpty) {
                    onGalleryOkEmpty()
                    return
                }
                onGalleryResources(galleryFragment.selectEntities)
            }
            R.id.galleryFinderAll -> {
                if (galleryFragment.parentId.isScanAll()) {
                    finderList.clear()
                    finderList.addAll(galleryFragment.currentEntities.findFinder(
                            galleryBundle.sdName,
                            galleryBundle.allName
                    ))
                }
                if (finderList.isNotEmpty()) {
                    newFinderAdapter.onGalleryFinderUpdate(finderList)
                    newFinderAdapter.onGalleryFinderShow()
                    return
                }
                onGalleryFinderEmpty()
            }
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        if (item.parent == galleryFragment.parentId) {
            newFinderAdapter.onGalleryFinderHide()
            return
        }
        galleryFinderAll.text = item.bucketDisplayName
        galleryFragment.onScanGallery(item.parent, result = false)
        newFinderAdapter.onGalleryFinderHide()
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(galleryEntity.externalUri()).apply(requestOptions.override(width, height)).into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(finderEntity.externalUri()).apply(requestOptions).into(imageView)
        container.addView(imageView)
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
        onStartPrevPage(galleryFragment.currentEntities,
                if (parentId.isScanAll() && !galleryBundle.hideCamera) position - 1 else position,
                PreActivity::class.java)
    }

    /**
     * 点击预览但是未选择图片
     */
    open fun onGalleryPreEmpty() {
        getString(R.string.gallery_prev_select_empty).toastExpand(this)
    }

    /**
     * 点击确定但是未选择图片
     */
    open fun onGalleryOkEmpty() {
        getString(R.string.gallery_ok_select_empty).toastExpand(this)
    }

    /**
     * 扫描到的文件目录为空
     */
    open fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).toastExpand(this)
    }
}