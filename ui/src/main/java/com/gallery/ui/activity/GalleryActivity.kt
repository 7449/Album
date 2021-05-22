package com.gallery.ui.activity

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
import com.gallery.compat.crop.GalleryCompatCropper
import com.gallery.compat.extensions.galleryFragment
import com.gallery.compat.extensions.minimumDrawableExpand
import com.gallery.compat.extensions.statusBarColorExpand
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawableExpand
import com.gallery.core.extensions.isVideoScanExpand
import com.gallery.core.extensions.safeToastExpand
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.scan.Types
import com.gallery.ui.R
import com.gallery.ui.databinding.GalleryActivityGalleryBinding
import com.gallery.ui.finder.PopupFinderAdapter

open class GalleryActivity : GalleryCompatActivity(), View.OnClickListener,
    GalleryFinderAdapter.AdapterFinderListener {

    private val viewBinding: GalleryActivityGalleryBinding by lazy {
        GalleryActivityGalleryBinding.inflate(
            layoutInflater
        )
    }

    override val galleryFinderAdapter: GalleryFinderAdapter by lazy {
        PopupFinderAdapter(
            this@GalleryActivity, viewBinding.galleryFinderAll, uiConfig, this@GalleryActivity
        )
    }

    override val cropImpl: ICrop?
        get() = GalleryCompatCropper(this, uiConfig)

    override val currentFinderName: String
        get() = viewBinding.galleryFinderAll.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        window.statusBarColorExpand(uiConfig.statusBarColor)
        viewBinding.galleryToolbar.title = uiConfig.toolbarText
        viewBinding.galleryToolbar.setTitleTextColor(uiConfig.toolbarTextColor)
        val drawable = drawableExpand(uiConfig.toolbarIcon)
        drawable?.colorFilter =
            PorterDuffColorFilter(uiConfig.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.galleryToolbar.navigationIcon = drawable
        viewBinding.galleryToolbar.setBackgroundColor(uiConfig.toolbarBackground)
        viewBinding.galleryToolbar.elevation = uiConfig.toolbarElevation

        viewBinding.galleryFinderAll.textSize = uiConfig.finderTextSize
        viewBinding.galleryFinderAll.setTextColor(uiConfig.finderTextColor)
        viewBinding.galleryFinderAll.setCompoundDrawables(
            null,
            null,
            minimumDrawableExpand(
                uiConfig.finderTextCompoundDrawable,
                uiConfig.finderTextDrawableColor
            ),
            null
        )

        viewBinding.galleryPre.text = uiConfig.preViewText
        viewBinding.galleryPre.textSize = uiConfig.preViewTextSize
        viewBinding.galleryPre.setTextColor(uiConfig.preViewTextColor)

        viewBinding.gallerySelect.text = uiConfig.selectText
        viewBinding.gallerySelect.textSize = uiConfig.selectTextSize
        viewBinding.gallerySelect.setTextColor(uiConfig.selectTextColor)

        viewBinding.galleryBottomView.setBackgroundColor(uiConfig.bottomViewBackground)

        galleryFinderAdapter.finderInit()
        viewBinding.galleryPre.setOnClickListener(this)
        viewBinding.gallerySelect.setOnClickListener(this)
        viewBinding.galleryFinderAll.setOnClickListener(this)

        viewBinding.galleryFinderAll.text = finderName
        viewBinding.galleryPre.visibility =
            if (galleryConfig.radio || galleryConfig.isVideoScanExpand) View.GONE else View.VISIBLE
        viewBinding.gallerySelect.visibility = if (galleryConfig.radio) View.GONE else View.VISIBLE

        viewBinding.galleryToolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (galleryFragment.selectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                onStartPrevPage(Types.Scan.SCAN_NONE, 0, PreActivity::class.java)
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
        viewBinding.galleryFinderAll.text = item.bucketDisplayName
        galleryFragment.onScanGallery(item.parent)
        galleryFinderAdapter.hide()
    }

    override fun onGalleryFinderThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        onDisplayGalleryThumbnails(finderEntity, container)
    }

    override fun onDisplayGallery(
        width: Int,
        height: Int,
        scanEntity: ScanEntity,
        container: FrameLayout,
        checkBox: TextView
    ) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(scanEntity.uri).apply(
            RequestOptions().placeholder(R.drawable.ic_gallery_default_loading)
                .error(R.drawable.ic_gallery_default_loading).centerCrop().override(width, height)
        ).into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(finderEntity.uri).apply(
            RequestOptions().placeholder(R.drawable.ic_gallery_default_loading)
                .error(R.drawable.ic_gallery_default_loading).centerCrop()
        ).into(imageView)
        container.addView(imageView)
    }

    override fun onPhotoItemClick(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity,
        position: Int,
        parentId: Long
    ) {
        onStartPrevPage(
            parentId,
            if (parentId.isScanAllExpand && !bundle.hideCamera) position - 1 else position,
            PreActivity::class.java
        )
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