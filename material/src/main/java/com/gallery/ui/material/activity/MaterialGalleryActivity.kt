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
import com.gallery.core.extensions.safeToastExpand
import com.gallery.scan.Types
import com.gallery.scan.extensions.isScanAllExpand
import com.gallery.ui.material.R
import com.gallery.ui.material.args.MaterialGalleryBundle
import com.gallery.ui.material.crop.MaterialGalleryCropper
import com.gallery.ui.material.databinding.MaterialGalleryActivityGalleryBinding
import com.gallery.ui.material.finder.MaterialFinderAdapter
import com.gallery.ui.material.materialGalleryArgOrDefault
import com.gallery.ui.material.minimumDrawableExpand
import com.theartofdev.edmodo.cropper.CropImageOptions

open class MaterialGalleryActivity : GalleryCompatActivity(), View.OnClickListener,
        GalleryFinderAdapter.AdapterFinderListener {

    private val viewBinding: MaterialGalleryActivityGalleryBinding by lazy {
        MaterialGalleryActivityGalleryBinding.inflate(layoutInflater)
    }

    private val materialGalleryBundle: MaterialGalleryBundle by lazy {
        gapConfig.materialGalleryArgOrDefault
    }

    override val finderAdapter: GalleryFinderAdapter by lazy {
        MaterialFinderAdapter(
                this@MaterialGalleryActivity,
                viewBinding.finderAll,
                materialGalleryBundle,
                this@MaterialGalleryActivity
        )
    }

    override val cropImpl: ICrop?
        get() = MaterialGalleryCropper(CropImageOptions())

    override val currentFinderName: String
        get() = viewBinding.finderAll.text.toString()

    override val galleryFragmentId: Int
        get() = R.id.galleryFrame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColor = materialGalleryBundle.statusBarColor
        viewBinding.toolbar.title = materialGalleryBundle.toolbarText
        viewBinding.toolbar.setTitleTextColor(materialGalleryBundle.toolbarTextColor)
        val drawable = drawableExpand(materialGalleryBundle.toolbarIcon)
        drawable?.colorFilter =
                PorterDuffColorFilter(materialGalleryBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        viewBinding.toolbar.navigationIcon = drawable
        viewBinding.toolbar.setBackgroundColor(materialGalleryBundle.toolbarBackground)
        viewBinding.toolbar.elevation = materialGalleryBundle.toolbarElevation

        viewBinding.finderAll.textSize = materialGalleryBundle.finderTextSize
        viewBinding.finderAll.setTextColor(materialGalleryBundle.finderTextColor)
        viewBinding.finderAll.setCompoundDrawables(
                null,
                null,
                minimumDrawableExpand(
                        materialGalleryBundle.finderTextCompoundDrawable,
                        materialGalleryBundle.finderTextDrawableColor
                ),
                null
        )

        viewBinding.openPrev.text = materialGalleryBundle.preViewText
        viewBinding.openPrev.textSize = materialGalleryBundle.preViewTextSize
        viewBinding.openPrev.setTextColor(materialGalleryBundle.preViewTextColor)

        viewBinding.select.text = materialGalleryBundle.selectText
        viewBinding.select.textSize = materialGalleryBundle.selectTextSize
        viewBinding.select.setTextColor(materialGalleryBundle.selectTextColor)

        viewBinding.bottomView.setBackgroundColor(materialGalleryBundle.bottomViewBackground)

        finderAdapter.finderInit()
        viewBinding.openPrev.setOnClickListener(this)
        viewBinding.select.setOnClickListener(this)
        viewBinding.finderAll.setOnClickListener(this)

        viewBinding.finderAll.text = finderName
        viewBinding.openPrev.visibility =
                if (galleryConfig.radio || galleryConfig.isVideoScanExpand) View.GONE else View.VISIBLE
        viewBinding.select.visibility = if (galleryConfig.radio) View.GONE else View.VISIBLE

        viewBinding.toolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onGalleryCreated(
            delegate: IScanDelegate,
            bundle: GalleryBundle,
            savedInstanceState: Bundle?
    ) {
        delegate.rootView.setBackgroundColor(materialGalleryBundle.galleryRootBackground)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.open_prev -> {
                if (requireGalleryFragment.isSelectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                startPrevPage(
                        parentId = Types.Scan.NONE,
                        position = 0,
                        customBundle = materialGalleryBundle,
                        cla = MaterialPreActivity::class.java
                )
            }
            R.id.select -> {
                if (requireGalleryFragment.isSelectEmpty) {
                    onGalleryOkEmpty()
                    return
                }
                onGalleryResources(requireGalleryFragment.selectItem)
            }
            R.id.finder_all -> {
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
        viewBinding.finderAll.text = item.bucketDisplayName
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
                RequestOptions().centerCrop().override(width, height)
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
                customBundle = materialGalleryBundle,
                cla = MaterialPreActivity::class.java
        )
    }

    /** 点击预览但是未选择图片 */
    open fun onGalleryPreEmpty() {
        getString(R.string.material_gallery_prev_select_empty).safeToastExpand(this)
    }

    /** 点击确定但是未选择图片 */
    open fun onGalleryOkEmpty() {
        getString(R.string.material_gallery_ok_select_empty).safeToastExpand(this)
    }

    /** 扫描到的文件目录为空 */
    open fun onGalleryFinderEmpty() {
        getString(R.string.material_gallery_finder_empty).safeToastExpand(this)
    }

}