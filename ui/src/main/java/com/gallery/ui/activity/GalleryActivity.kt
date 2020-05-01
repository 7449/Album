package com.gallery.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.ListPopupWindow
import androidx.kotlin.expand.app.addFragmentExpand
import androidx.kotlin.expand.app.findFragmentByTagExpand
import androidx.kotlin.expand.app.showFragmentExpand
import androidx.kotlin.expand.os.bundleParcelableOrDefault
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.orEmptyExpand
import androidx.kotlin.expand.text.toastExpand
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGallery
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.callback.IGalleryInterceptor
import com.gallery.core.ext.externalUri
import com.gallery.core.ext.findFinder
import com.gallery.core.ext.isScanAll
import com.gallery.core.ext.updateResultFinder
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
        bundleParcelableOrDefault<GalleryBundle>(IGallery.GALLERY_START_CONFIG, GalleryBundle())
    }
    private val galleryUiBundle by lazy {
        bundleParcelableOrDefault<GalleryUiBundle>(UIResult.UI_CONFIG, GalleryUiBundle())
    }
    private val finderList = ArrayList<ScanEntity>()
    private val requestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

    private val startActivityForResult: ActivityResultContracts.StartActivityForResult = ActivityResultContracts.StartActivityForResult()

    private val activityResult: ActivityResultCallback<ActivityResult> = ActivityResultCallback<ActivityResult> { intent ->
        val bundleExpand = intent?.data?.extras.orEmptyExpand()
        if (intent.resultCode == Activity.RESULT_OK) {
            galleryFragment.onUpdatePrevResult(bundleExpand.orEmptyExpand())
            val entities = bundleExpand.getParcelableArrayListExpand<ScanEntity>(UIResult.PREV_RESULT_SELECT)
            if (entities.isNotEmpty()) {
                onGalleryResources(entities)
            }
            if (bundleExpand.getBoolean(UIResult.PREV_RESULT_FINISH)) {
                finish()
            }
        } else if (intent.resultCode == Activity.RESULT_CANCELED) {
            galleryFragment.onUpdatePrevResult(bundleExpand.orEmptyExpand())
        }
    }

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

        galleryToolbar.setNavigationOnClickListener { finish() }

        findFragmentByTagExpand(ScanFragment::class.java.simpleName) {
            if (it == null) {
                addFragmentExpand(R.id.galleryFrame, ScanFragment.newInstance(galleryBundle))
            } else {
                showFragmentExpand(it)
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
                val newInstance = PreActivity.newInstance(
                        galleryFragment,
                        galleryFragment.selectEntities,
                        galleryFragment.selectEntities,
                        galleryBundle,
                        galleryUiBundle,
                        0
                )
                registerForActivityResult(startActivityForResult, activityResult).launch(newInstance)
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
                    finderAdapter.updateFinder(finderList)
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

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        onGalleryResource(scanEntity)
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
        val newInstance = PreActivity.newInstance(
                galleryFragment,
                galleryFragment.currentEntities,
                galleryFragment.selectEntities,
                galleryBundle,
                galleryUiBundle,
                if (parentId.isScanAll() && !galleryBundle.hideCamera) position - 1 else position)
        registerForActivityResult(startActivityForResult, activityResult).launch(newInstance)
    }

    override fun onScanResultSuccess(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        finderList.updateResultFinder(scanEntity)
    }

    override fun onUCropOptions() = UCrop.Options().apply { this.optionBundle.putAll(galleryUiBundle.uCropBundle) }

    override fun onUCropResources(uri: Uri) {
        onGalleryCropResource(uri)
    }

    /**
     * 点击预览但是未选择图片
     */
    open fun onGalleryPreEmpty() {
        getString(R.string.gallery_prev_select_empty).toastExpand(this)
    }

    /**
     * 扫描到的文件目录为空
     */
    open fun onGalleryFinderEmpty() {
        getString(R.string.gallery_finder_empty).toastExpand(this)
    }

    /**
     * 裁剪成功
     */
    open fun onGalleryCropResource(uri: Uri) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.FRAGMENT_RESULT_URI, uri)
        bundle.putInt(UIResult.FRAGMENT_RESULT_TYPE, UIResult.FRAGMENT_RESULT_CROP)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * 单选不裁剪
     */
    open fun onGalleryResource(scanEntity: ScanEntity) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(UIResult.FRAGMENT_RESULT_ENTITY, scanEntity)
        bundle.putInt(UIResult.FRAGMENT_RESULT_TYPE, UIResult.FRAGMENT_RESULT_RESOURCE)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * 选择图片
     */
    open fun onGalleryResources(entities: List<ScanEntity>) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelableArrayList(UIResult.FRAGMENT_RESULT_ENTITIES, entities as ArrayList<ScanEntity>)
        bundle.putInt(UIResult.FRAGMENT_RESULT_TYPE, UIResult.FRAGMENT_RESULT_RESOURCES)
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}