package develop.file.gallery.ui.material.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.ui.material.R
import com.gallery.ui.material.databinding.MaterialGalleryActivityGalleryBinding
import com.theartofdev.edmodo.cropper.CropImageOptions
import develop.file.gallery.compat.activity.GalleryCompatActivity
import develop.file.gallery.compat.extensions.ActivityCompat.requireGalleryFragment
import develop.file.gallery.compat.extensions.ContextCompat.drawable
import develop.file.gallery.compat.extensions.ContextCompat.minimumDrawable
import develop.file.gallery.compat.extensions.ContextCompat.toast
import develop.file.gallery.compat.finder.GalleryFinderAdapter
import develop.file.gallery.compat.widget.GalleryImageView
import develop.file.gallery.crop.ICrop
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.material.crop.MaterialGalleryCropper
import develop.file.gallery.ui.material.extensions.ResultCompat.materialGalleryConfigArgOrDefault
import develop.file.gallery.ui.material.finder.MaterialFinderAdapter
import develop.file.media.Types
import develop.file.media.extensions.isAllMediaParent

open class MaterialGalleryActivity : GalleryCompatActivity(), View.OnClickListener,
    GalleryFinderAdapter.AdapterFinderListener {

    private val viewBinding by lazy { MaterialGalleryActivityGalleryBinding.inflate(layoutInflater) }

    private val config by lazy { gapConfig.materialGalleryConfigArgOrDefault }

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
        viewBinding.toolbar.title = config.toolbarTextConfig.text
        viewBinding.toolbar.setTitleTextColor(config.toolbarTextConfig.textColor)
        viewBinding.toolbar.navigationIcon = drawable(config.toolbarIcon)
        viewBinding.toolbar.setBackgroundColor(config.toolbarBackground)
        viewBinding.toolbar.elevation = config.toolbarElevation

        viewBinding.finderAll.textSize = config.finderTextConfig.textSize
        viewBinding.finderAll.setTextColor(config.finderTextConfig.textColor)
        viewBinding.finderAll.setCompoundDrawables(
            null,
            null,
            minimumDrawable(config.finderIcon),
            null
        )

        viewBinding.openPrev.text = config.prevTextConfig.text
        viewBinding.openPrev.textSize = config.prevTextConfig.textSize
        viewBinding.openPrev.setTextColor(config.prevTextConfig.textColor)

        viewBinding.select.text = config.selectTextConfig.text
        viewBinding.select.textSize = config.selectTextConfig.textSize
        viewBinding.select.setTextColor(config.selectTextConfig.textColor)

        viewBinding.bottomView.setBackgroundColor(config.bottomViewBackground)

        finderAdapter.finderInit()
        viewBinding.openPrev.setOnClickListener(this)
        viewBinding.select.setOnClickListener(this)
        viewBinding.finderAll.setOnClickListener(this)

        viewBinding.finderAll.text = finderName
        viewBinding.openPrev.visibility =
            if (galleryConfig.radio || galleryConfig.isScanVideoMedia) View.GONE else View.VISIBLE
        viewBinding.select.visibility = if (galleryConfig.radio) View.GONE else View.VISIBLE

        viewBinding.toolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onGalleryCreated(delegate: IScanDelegate, saveState: Bundle?) {
        delegate.rootView.setBackgroundColor(config.galleryRootBackground)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.open_prev -> {
                if (requireGalleryFragment.isSelectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                startPrevPage(
                    parentId = Types.Id.NONE,
                    position = 0,
                    gap = config,
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

    override fun onGalleryFinderThumbnails(entity: ScanEntity, container: FrameLayout) {
        onDisplayFinderGallery(entity, container)
    }

    override fun onDisplayHomeGallery(
        width: Int,
        height: Int,
        entity: ScanEntity,
        container: FrameLayout
    ) {
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

    override fun onDisplayFinderGallery(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context).load(entity.uri).apply(
            RequestOptions().centerCrop()
        ).into(imageView)
        container.addView(imageView)
    }

    override fun onPhotoItemClick(entity: ScanEntity, position: Int, parentId: Long) {
        startPrevPage(
            parentId = parentId,
            position = if (parentId.isAllMediaParent && !galleryConfig.hideCamera) position - 1 else position,
            gap = config,
            cla = MaterialPreActivity::class.java
        )
    }

    /** 点击预览但是未选择图片 */
    open fun onGalleryPreEmpty() {
        toast(getString(R.string.material_gallery_prev_select_empty))
    }

    /** 点击确定但是未选择图片 */
    open fun onGalleryOkEmpty() {
        toast(getString(R.string.material_gallery_ok_select_empty))
    }

    /** 扫描到的文件目录为空 */
    open fun onGalleryFinderEmpty() {
        toast(getString(R.string.material_gallery_finder_empty))
    }

}