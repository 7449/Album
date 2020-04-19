package com.gallery.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.appcompat.widget.ListPopupWindow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.*
import com.gallery.core.ext.*
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult
import com.gallery.ui.adapter.FinderAdapter
import com.gallery.ui.obtain
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

open class GalleryActivity(layoutId: Int = R.layout.gallery_activity_gallery) : GalleryBaseActivity(layoutId),
        View.OnClickListener, AdapterView.OnItemClickListener, IGalleryCallback,
        IGalleryImageLoader, IGalleryInterceptor {

    private val finderAdapter by lazy {
        FinderAdapter(galleryUiBundle) { finderEntity, container -> onDisplayGalleryThumbnails(finderEntity, container) }
    }
    private val listPopupWindow by lazy {
        ListPopupWindow(this).apply {
            this.anchorView = galleryFinderAll
            this.width = galleryUiBundle.listPopupWidth
            this.horizontalOffset = galleryUiBundle.listPopupHorizontalOffset
            this.verticalOffset = galleryUiBundle.listPopupVerticalOffset
            this.isModal = true
            this.setOnItemClickListener(this@GalleryActivity)
            this.setAdapter(finderAdapter)
        }
    }
    private val galleryBundle by lazy {
        intent.extras?.getParcelable(IGallery.GALLERY_START_CONFIG)
                ?: GalleryBundle()
    }
    private val galleryUiBundle by lazy {
        intent.extras?.getParcelable(UIResult.UI_CONFIG)
                ?: GalleryUiBundle()
    }

    private val finderList = ArrayList<ScanEntity>()
    private val requestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(UIResult.FINDER_LIST, finderList)
        outState.putString(UIResult.FINDER_NAME, galleryFinderAll.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        obtain(galleryUiBundle)

        galleryPre.setOnClickListener(this)
        gallerySelect.setOnClickListener(this)
        galleryFinderAll.setOnClickListener(this)

        finderList.addAll(savedInstanceState?.getParcelableArrayList(UIResult.FINDER_LIST)
                ?: ArrayList())

        galleryFinderAll.text = savedInstanceState?.getString(UIResult.FINDER_NAME)
                ?: galleryBundle.allName

        galleryPre.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        gallerySelect.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE

        galleryToolbar.title = galleryUiBundle.toolbarText
        galleryToolbar.setNavigationOnClickListener { finish() }

        findFragmentByTag(ScanFragment::class.java.simpleName) {
            if (it == null) {
                addFragment(R.id.galleryFrame, ScanFragment.newInstance(galleryBundle))
            } else {
                showFragment(it)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (galleryFragment.selectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                PreActivity.newInstance(
                        galleryFragment,
                        galleryFragment.selectEntities,
                        galleryFragment.selectEntities,
                        galleryBundle,
                        galleryUiBundle,
                        0
                )
            }
            R.id.gallerySelect -> onGalleryResources(galleryFragment.selectEntities)
            R.id.galleryFinderAll -> {
                if (galleryFragment.parentId.isScanAll()) {
                    finderList.clear()
                    finderList.addAll(galleryFragment.currentEntities.findFinder(
                            galleryBundle.sdName,
                            galleryBundle.allName
                    ))
                }
                if (finderList.isNotEmpty()) {
                    finderAdapter.list = finderList
                    listPopupWindow.show()
                    listPopupWindow.listView?.setBackgroundColor(galleryUiBundle.finderItemBackground)
                    return
                }
                onGalleryFinderEmpty()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getItem(position)
        if (finder.parent == galleryFragment.parentId) {
            listPopupWindow.dismiss()
            return
        }
        galleryFinderAll.text = finder.bucketDisplayName
        galleryFragment.onScanGallery(finder.parent, result = false)
        listPopupWindow.dismiss()
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

    override fun onGalleryResource(scanEntity: ScanEntity) {
        scanEntity.toString().show(this)
    }

    override fun onPhotoItemClick(selectEntities: ArrayList<ScanEntity>, position: Int, parentId: Long) {
        PreActivity.newInstance(
                galleryFragment,
                galleryFragment.currentEntities,
                selectEntities,
                galleryBundle,
                galleryUiBundle,
                if (parentId.isScanAll() && !galleryBundle.hideCamera) position - 1 else position)
    }

    override fun onScanResultSuccess(scanEntity: ScanEntity) {
        finderList.updateResultFinder(galleryFragment.parentId, scanEntity)
    }

    override fun onGalleryFragmentResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK && requestCode == IGalleryPrev.PREV_START_REQUEST_CODE) {
            if (data?.extras.orEmpty().getBoolean(UIResult.PREV_RESULT_FINISH)) {
                finish()
            }
        }
        return super.onGalleryFragmentResult(requestCode, resultCode, data)
    }

    override fun onUCropOptions() = UCrop.Options().apply { this.optionBundle.putAll(galleryUiBundle.uCropBundle) }

    override fun onUCropResources(uri: Uri) {
        uri.toString().show(this)
    }

    /**
     * 点击预览但是未选择图片
     */
    open fun onGalleryPreEmpty() {
        "未选择图片".show(this)
    }

    /**
     * 扫描到的文件目录为空
     */
    open fun onGalleryFinderEmpty() {
        "文件目录为空".show(this)
    }

    /**
     * 选择图片
     */
    open fun onGalleryResources(entities: List<ScanEntity>) {}
}