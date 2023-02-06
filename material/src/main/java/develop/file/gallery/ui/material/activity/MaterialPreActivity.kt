package develop.file.gallery.ui.material.activity

import android.os.Bundle
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.gallery.ui.material.R
import com.gallery.ui.material.databinding.MaterialGalleryActivityPreviewBinding
import develop.file.gallery.compat.activity.PrevCompatActivity
import develop.file.gallery.compat.extensions.ActivityCompat.requirePrevFragment
import develop.file.gallery.compat.extensions.ContextCompat.drawable
import develop.file.gallery.compat.extensions.ContextCompat.toast
import develop.file.gallery.delegate.IPrevDelegate
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.material.extensions.ResultCompat.materialGalleryConfigArgOrDefault
import develop.file.gallery.ui.material.extensions.ViewCompat.createGalleryImageView

open class MaterialPreActivity : PrevCompatActivity() {

    companion object {
        private const val format = "%s / %s"
    }

    private val viewBinding by lazy { MaterialGalleryActivityPreviewBinding.inflate(layoutInflater) }

    private val config by lazy { gapConfig.materialGalleryConfigArgOrDefault }

    override val galleryFragmentId: Int get() = R.id.preFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        window.statusBarColor = config.statusBarColor
        viewBinding.toolbar.title = config.toolbarTextConfig.text
        viewBinding.toolbar.setTitleTextColor(config.toolbarTextConfig.textColor)
        viewBinding.toolbar.navigationIcon = drawable(config.toolbarIcon)
        viewBinding.toolbar.setBackgroundColor(config.toolbarBackground)
        viewBinding.toolbar.elevation = config.toolbarElevation

        viewBinding.count.textSize = config.preBottomCountConfig.textSize
        viewBinding.count.setTextColor(config.preBottomCountConfig.textColor)

        viewBinding.bottomView.setBackgroundColor(config.bottomViewBackground)
        viewBinding.bottomViewSelect.text = config.preBottomOkConfig.text
        viewBinding.bottomViewSelect.textSize = config.preBottomOkConfig.textSize
        viewBinding.bottomViewSelect.setTextColor(config.preBottomOkConfig.textColor)

        viewBinding.bottomViewSelect.setOnClickListener {
            if (requirePrevFragment.isSelectEmpty) {
                onGallerySelectEmpty()
            } else {
                onGallerySelectEntities()
            }
        }
        viewBinding.toolbar.setNavigationOnClickListener { onGalleryFinish() }
    }

    override fun onDisplayPrevGallery(entity: ScanEntity, container: FrameLayout) {
        container.removeAllViews()
        val imageView = container.createGalleryImageView()
        Glide.with(container.context)
            .load(entity.uri)
            .into(imageView)
        container.addView(imageView)
    }

    override fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?) {
        delegate.rootView.setBackgroundColor(config.galleryRootBackground)
        viewBinding.count.text = format.format(delegate.selectCount, galleryConfig.maxCount)
        viewBinding.toolbar.title =
            config.toolbarTextConfig.text + "(" + (delegate.currentPosition + 1) + "/" + delegate.itemCount + ")"
    }

    override fun onSelectMultipleFileNotExist(entity: ScanEntity) {
        viewBinding.count.text =
            format.format(requirePrevFragment.selectCount, galleryConfig.maxCount)
    }

    override fun onPageSelected(position: Int) {
        viewBinding.toolbar.title =
            config.toolbarTextConfig.text + "(" + (position + 1) + "/" + requirePrevFragment.itemCount + ")"
    }

    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
        onSelectMultipleFileNotExist(entity)
    }

    open fun onGallerySelectEmpty() {
        toast(getString(R.string.material_gallery_prev_select_empty_pre))
    }

}