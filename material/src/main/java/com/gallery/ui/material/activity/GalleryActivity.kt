package com.gallery.ui.material.activity

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
import com.gallery.compat.extensions.requireGalleryFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawableExpand
import com.gallery.core.extensions.isVideoScanExpand
import com.gallery.core.extensions.safeToastExpand
import com.gallery.scan.Types
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.ui.material.R
import com.gallery.ui.material.args.GalleryMaterialBundle
import com.gallery.ui.material.crop.GalleryMaterialCropper
import com.gallery.ui.material.databinding.GalleryMaterialActivityGalleryBinding
import com.gallery.ui.material.finder.PopupFinderAdapter
import com.gallery.ui.material.materialArgOrDefault
import com.gallery.ui.material.minimumDrawableExpand
import com.theartofdev.edmodo.cropper.CropImageOptions

open class GalleryActivity : GalleryCompatActivity(), View.OnClickListener,
    GalleryFinderAdapter.AdapterFinderListener {

    private val viewBinding: GalleryMaterialActivityGalleryBinding by lazy {
        GalleryMaterialActivityGalleryBinding.inflate(
            layoutInflater
        )
    }

    private val materialBundle: GalleryMaterialBundle by lazy {
        gapConfig.materialArgOrDefault
    }

    override val finderAdapter: GalleryFinderAdapter by lazy {
        PopupFinderAdapter(
            this@GalleryActivity,
            viewBinding.galleryFinderAll,
            materialBundle,
            this@GalleryActivity
        )
    }

    override val cropImpl: ICrop?
        get() = GalleryMaterialCropper(CropImageOptions())

    override val currentFinderName: String
        get() = viewBinding.galleryFinderAll.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColor = materialBundle.statusBarColor
        viewBinding.galleryToolbar.title = materialBundle.toolbarText
        viewBinding.galleryToolbar.setTitleTextColor(materialBundle.toolbarTextColor)
        val drawable = drawableExpand(materialBundle.toolbarIcon)
        drawable?.colorFilter =
            PorterDuffColorFilter(materialBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.galleryToolbar.navigationIcon = drawable
        viewBinding.galleryToolbar.setBackgroundColor(materialBundle.toolbarBackground)
        viewBinding.galleryToolbar.elevation = materialBundle.toolbarElevation

        viewBinding.galleryFinderAll.textSize = materialBundle.finderTextSize
        viewBinding.galleryFinderAll.setTextColor(materialBundle.finderTextColor)
        viewBinding.galleryFinderAll.setCompoundDrawables(
            null,
            null,
            minimumDrawableExpand(
                materialBundle.finderTextCompoundDrawable,
                materialBundle.finderTextDrawableColor
            ),
            null
        )

        viewBinding.galleryPre.text = materialBundle.preViewText
        viewBinding.galleryPre.textSize = materialBundle.preViewTextSize
        viewBinding.galleryPre.setTextColor(materialBundle.preViewTextColor)

        viewBinding.gallerySelect.text = materialBundle.selectText
        viewBinding.gallerySelect.textSize = materialBundle.selectTextSize
        viewBinding.gallerySelect.setTextColor(materialBundle.selectTextColor)

        viewBinding.galleryBottomView.setBackgroundColor(materialBundle.bottomViewBackground)

        finderAdapter.finderInit()
        viewBinding.galleryPre.setOnClickListener(this)
        viewBinding.gallerySelect.setOnClickListener(this)
        viewBinding.galleryFinderAll.setOnClickListener(this)

        viewBinding.galleryFinderAll.text = finderName
        viewBinding.galleryPre.visibility =
            if (galleryConfig.radio || galleryConfig.isVideoScanExpand) View.GONE else View.VISIBLE
        viewBinding.gallerySelect.visibility = if (galleryConfig.radio) View.GONE else View.VISIBLE

        viewBinding.galleryToolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
        delegate.rootView.setBackgroundColor(materialBundle.galleryRootBackground)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (requireGalleryFragment.isSelectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                startPrevPage(
                    parentId = Types.Scan.SCAN_NONE,
                    position = 0,
                    customBundle = materialBundle,
                    cla = PreActivity::class.java
                )
            }
            R.id.gallerySelect -> {
                if (requireGalleryFragment.isSelectEmpty) {
                    onGalleryOkEmpty()
                    return
                }
                onGalleryResources(requireGalleryFragment.selectItem)
            }
            R.id.galleryFinderAll -> {
                if (finderList.isEmpty()) {
                    onGalleryFinderEmpty()
                    return
                }
                finderAdapter.finderUpdate(finderList)
                finderAdapter.show()
            }
        }
    }

    override fun onGalleryAdapterItemClick(view: View, position: Int, item: ScanEntity) {
        val fragment = requireGalleryFragment
        if (item.parent == fragment.parentId) {
            finderAdapter.hide()
            return
        }
        viewBinding.galleryFinderAll.text = item.bucketDisplayName
        fragment.onScanGallery(item.parent)
        finderAdapter.hide()
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
            RequestOptions()
                .centerCrop().override(width, height)
        ).into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

    override fun onDisplayGalleryThumbnails(finderEntity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(finderEntity.uri).apply(
            RequestOptions().centerCrop()
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
        startPrevPage(
            parentId = parentId,
            position = if (parentId.isScanAllExpand && !bundle.hideCamera) position - 1 else position,
            customBundle = materialBundle,
            cla = PreActivity::class.java
        )
    }

    /** 点击预览但是未选择图片 */
    open fun onGalleryPreEmpty() {
        getString(R.string.gallery_material_prev_select_empty).safeToastExpand(this)
    }

    /** 点击确定但是未选择图片 */
    open fun onGalleryOkEmpty() {
        getString(R.string.gallery_material_ok_select_empty).safeToastExpand(this)
    }

    /** 扫描到的文件目录为空 */
    open fun onGalleryFinderEmpty() {
        getString(R.string.gallery_material_finder_empty).safeToastExpand(this)
    }

}