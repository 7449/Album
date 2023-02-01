package com.gallery.ui.material.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.activity.GalleryCompatActivity
import com.gallery.compat.extensions.requireGalleryFragment
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryConfigs
import com.gallery.core.crop.ICrop
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.drawable
import com.gallery.core.extensions.toast
import com.gallery.scan.Types
import com.gallery.scan.extensions.isScanAllMedia
import com.gallery.ui.material.R
import com.gallery.ui.material.args.MaterialGalleryConfig
import com.gallery.ui.material.crop.MaterialGalleryCropper
import com.gallery.ui.material.databinding.MaterialGalleryActivityGalleryBinding
import com.gallery.ui.material.finder.MaterialFinderAdapter
import com.gallery.ui.material.materialGalleryArgOrDefault
import com.gallery.ui.material.minimumDrawable
import com.theartofdev.edmodo.cropper.CropImageOptions

open class MaterialGalleryActivity : GalleryCompatActivity(), View.OnClickListener,
    GalleryFinderAdapter.AdapterFinderListener {

    private val viewBinding: MaterialGalleryActivityGalleryBinding by lazy {
        MaterialGalleryActivityGalleryBinding.inflate(layoutInflater)
    }

    private val config: MaterialGalleryConfig by lazy {
        gapConfig.materialGalleryArgOrDefault
    }

    override val finderAdapter: GalleryFinderAdapter by lazy {
        MaterialFinderAdapter(
            this@MaterialGalleryActivity,
            viewBinding.finderAll,
            config,
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
        window.statusBarColor = config.statusBarColor
        viewBinding.toolbar.title = config.toolbarText
        viewBinding.toolbar.setTitleTextColor(config.toolbarTextColor)
        viewBinding.toolbar.navigationIcon = drawable(config.toolbarIcon)
        viewBinding.toolbar.setBackgroundColor(config.toolbarBackground)
        viewBinding.toolbar.elevation = config.toolbarElevation

        viewBinding.finderAll.textSize = config.finderTextSize
        viewBinding.finderAll.setTextColor(config.finderTextColor)
        viewBinding.finderAll.setCompoundDrawables(null, null, minimumDrawable(config.finderIcon), null)

        viewBinding.openPrev.text = config.preViewText
        viewBinding.openPrev.textSize = config.preViewTextSize
        viewBinding.openPrev.setTextColor(config.preViewTextColor)

        viewBinding.select.text = config.selectText
        viewBinding.select.textSize = config.selectTextSize
        viewBinding.select.setTextColor(config.selectTextColor)

        viewBinding.bottomView.setBackgroundColor(config.bottomViewBackground)

        finderAdapter.finderInit()
        viewBinding.openPrev.setOnClickListener(this)
        viewBinding.select.setOnClickListener(this)
        viewBinding.finderAll.setOnClickListener(this)

        viewBinding.finderAll.text = finderName
        viewBinding.openPrev.visibility = if (galleryConfig.radio || galleryConfig.isScanVideoMedia) View.GONE else View.VISIBLE
        viewBinding.select.visibility = if (galleryConfig.radio) View.GONE else View.VISIBLE

        viewBinding.toolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onGalleryCreated(delegate: IScanDelegate, configs: GalleryConfigs, saveState: Bundle?) {
        delegate.rootView.setBackgroundColor(config.galleryRootBackground)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.open_prev -> {
                if (requireGalleryFragment.isSelectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                startPrevPage(parentId = Types.Id.NONE, position = 0, gap = config, cla = MaterialPreActivity::class.java)
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
        onDisplayThumbnailsGallery(finderEntity, container)
    }

    override fun onDisplayHomeGallery(width: Int, height: Int, entity: ScanEntity, container: FrameLayout) {
        val imageView = if (container.tag is ImageView) {
            container.tag as ImageView
        } else {
            GalleryImageView(container.context).apply {
                container.tag = this
                container.addView(this, 0, FrameLayout.LayoutParams(width, height))
            }
        }
        Glide.with(container.context).load(entity.uri).apply(
            RequestOptions().centerCrop().override(width, height)
        ).into(imageView)
    }

    override fun onDisplayThumbnailsGallery(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(entity.uri).apply(
            RequestOptions().centerCrop()
        ).into(imageView)
        container.addView(imageView)
    }

    override fun onPhotoItemClick(context: Context, configs: GalleryConfigs, scanEntity: ScanEntity, position: Int, parentId: Long) {
        startPrevPage(
            parentId = parentId,
            position = if (parentId.isScanAllMedia && !configs.hideCamera) position - 1 else position,
            gap = config,
            cla = MaterialPreActivity::class.java
        )
    }

    /** 点击预览但是未选择图片 */
    open fun onGalleryPreEmpty() {
        getString(R.string.material_gallery_prev_select_empty).toast(this)
    }

    /** 点击确定但是未选择图片 */
    open fun onGalleryOkEmpty() {
        getString(R.string.material_gallery_ok_select_empty).toast(this)
    }

    /** 扫描到的文件目录为空 */
    open fun onGalleryFinderEmpty() {
        getString(R.string.material_gallery_finder_empty).toast(this)
    }

}