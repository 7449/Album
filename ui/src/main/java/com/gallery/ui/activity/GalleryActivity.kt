package com.gallery.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.activity.GalleryCompatActivity
import com.gallery.compat.activity.galleryFragment
import com.gallery.compat.crop.GalleryCompatCropper
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.*
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.scan.types.ScanType
import com.gallery.ui.R
import com.gallery.ui.finder.PopupFinderAdapter
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

open class GalleryActivity(layoutId: Int = R.layout.gallery_activity_gallery) : GalleryCompatActivity(layoutId),
        View.OnClickListener, GalleryFinderAdapter.AdapterFinderListener {

    override val galleryFinderAdapter: GalleryFinderAdapter by lazy { PopupFinderAdapter() }

    override val cropImpl: ICrop?
        get() = GalleryCompatCropper(this, uiConfig)

    override val currentFinderName: String
        get() = galleryFinderAll.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryFrame

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColorExpand(uiConfig.statusBarColor)
        if (hasLExpand()) {
            window.statusBarColor = uiConfig.statusBarColor
        }
        galleryToolbar.title = uiConfig.toolbarText
        galleryToolbar.setTitleTextColor(uiConfig.toolbarTextColor)
        val drawable = drawableExpand(uiConfig.toolbarIcon)
        drawable?.colorFilter = PorterDuffColorFilter(uiConfig.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        galleryToolbar.navigationIcon = drawable
        galleryToolbar.setBackgroundColor(uiConfig.toolbarBackground)
        if (hasLExpand()) {
            galleryToolbar.elevation = uiConfig.toolbarElevation
        }

        galleryFinderAll.textSize = uiConfig.finderTextSize
        galleryFinderAll.setTextColor(uiConfig.finderTextColor)
        galleryFinderAll.setCompoundDrawables(null, null, minimumDrawableExpand(uiConfig.finderTextCompoundDrawable, uiConfig.finderTextDrawableColor), null)

        galleryPre.text = uiConfig.preViewText
        galleryPre.textSize = uiConfig.preViewTextSize
        galleryPre.setTextColor(uiConfig.preViewTextColor)

        gallerySelect.text = uiConfig.selectText
        gallerySelect.textSize = uiConfig.selectTextSize
        gallerySelect.setTextColor(uiConfig.selectTextColor)

        galleryBottomView.setBackgroundColor(uiConfig.bottomViewBackground)

        galleryFinderAdapter.adapterInit(this, uiConfig, galleryFinderAll)
        galleryFinderAdapter.setOnAdapterFinderListener(this)
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
                galleryFinderAdapter.finderUpdate(finderList)
                galleryFinderAdapter.show()
            }
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        if (item.parent == galleryFragment.parentId) {
            galleryFinderAdapter.hide()
            return
        }
        galleryFinderAll.text = item.bucketDisplayName
        galleryFragment.onScanGallery(item.parent)
        galleryFinderAdapter.hide()
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