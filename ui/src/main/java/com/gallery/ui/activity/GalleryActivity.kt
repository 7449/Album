package com.gallery.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import androidx.appcompat.widget.ListPopupWindow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.CameraStatus
import com.gallery.core.GalleryBundle
import com.gallery.core.PermissionCode
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
import com.gallery.ui.callback.IGalleryRootCallback
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

class GalleryActivity : GalleryBaseActivity(R.layout.gallery_activity_gallery),
        View.OnClickListener, AdapterView.OnItemClickListener, IGalleryCallback,
        IGalleryImageLoader, IGalleryInterceptor, IGalleryRootCallback {

    private val finderAdapter by lazy {
        FinderAdapter(galleryUiBundle) { finderEntity, container -> onDisplayGalleryThumbnails(finderEntity, container) }
    }
    private val listPopupWindow by lazy {
        ListPopupWindow(this)
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

    override fun initView() {
        galleryPre.setOnClickListener(this)
        gallerySelect.setOnClickListener(this)
        galleryFinderAll.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(UIResult.FINDER_LIST, finderList)
        outState.putString(UIResult.FINDER_NAME, galleryFinderAll.text.toString())
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {

        finderList.addAll(savedInstanceState?.getParcelableArrayList(UIResult.FINDER_LIST)
                ?: ArrayList())

        galleryFinderAll.text = savedInstanceState?.getString(UIResult.FINDER_NAME)
                ?: galleryBundle.allName

        window.statusBarColor(galleryUiBundle.statusBarColor)
        galleryPre.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        gallerySelect.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        galleryToolbar.title = galleryUiBundle.toolbarText
        galleryToolbar.setTitleTextColor(galleryUiBundle.toolbarTextColor)
        val drawable = drawable(galleryUiBundle.toolbarIcon)
        drawable?.setColorFilter(galleryUiBundle.toolbarIconColor, PorterDuff.Mode.SRC_ATOP)
        galleryToolbar.navigationIcon = drawable
        galleryToolbar.setBackgroundColor(galleryUiBundle.toolbarBackground)
        if (hasL()) {
            galleryToolbar.elevation = galleryUiBundle.toolbarElevation
        }
        galleryToolbar.setNavigationOnClickListener {
            onGalleryRootFinish()
            finish()
        }
        initFragment()
        initBottomView()
        initFinderView()
    }

    private fun initFragment() {
        if (supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.galleryFrame, ScanFragment.newInstance(galleryBundle), ScanFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().show(galleryFragment()).commitAllowingStateLoss()
        }
    }

    private fun initBottomView() {
        galleryBottomView.setBackgroundColor(galleryUiBundle.bottomViewBackground)
        galleryFinderAll.textSize = galleryUiBundle.bottomFinderTextSize
        galleryFinderAll.setTextColor(galleryUiBundle.bottomFinderTextColor)
        galleryFinderAll.setCompoundDrawables(null, null, drawable(galleryUiBundle.bottomFinderTextCompoundDrawable, galleryUiBundle.bottomFinderTextDrawableColor), null)
        galleryPre.text = galleryUiBundle.bottomPreViewText
        galleryPre.textSize = galleryUiBundle.bottomPreViewTextSize
        galleryPre.setTextColor(galleryUiBundle.bottomPreViewTextColor)
        gallerySelect.text = galleryUiBundle.bottomSelectText
        gallerySelect.textSize = galleryUiBundle.bottomSelectTextSize
        gallerySelect.setTextColor(galleryUiBundle.bottomSelectTextColor)
    }

    private fun initFinderView() {
        listPopupWindow.anchorView = galleryFinderAll
        listPopupWindow.width = galleryUiBundle.listPopupWidth
        listPopupWindow.horizontalOffset = galleryUiBundle.listPopupHorizontalOffset
        listPopupWindow.verticalOffset = galleryUiBundle.listPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
        listPopupWindow.setAdapter(finderAdapter)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (galleryFragment().selectEmpty) {
                    onGalleryPreEmpty()
                    return
                }
                PreActivity.newInstance(
                        galleryFragment(),
                        galleryFragment().selectEntities,
                        galleryFragment().selectEntities,
                        galleryBundle,
                        galleryUiBundle,
                        0
                )
            }
            R.id.gallerySelect -> {
                if (galleryFragment().selectEmpty) {
                    onGallerySelectEmpty()
                    return
                }
                onGalleryResources(galleryFragment().selectEntities)
            }
            R.id.galleryFinderAll -> {
                if (galleryFragment().parentId.isScanAll()) {
                    finderList.clear()
                    finderList.addAll(galleryFragment().currentEntities.findFinder(
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
        if (finder.parent == galleryFragment().parentId) {
            listPopupWindow.dismiss()
            return
        }
        galleryFinderAll.text = finder.bucketDisplayName
        galleryFragment().onScanGallery(finder.parent, result = false)
        listPopupWindow.dismiss()
    }

    override fun onScanResultSuccess(scanEntity: ScanEntity) {
        finderList.updateResultFinder(galleryFragment().parentId, scanEntity)
    }

    override fun onBackPressed() {
        onGalleryRootFinish()
        super.onBackPressed()
    }

    override fun onGalleryResource(scanEntities: ArrayList<ScanEntity>) {
    }

    override fun onGalleryFragmentResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK && requestCode == IGalleryPrev.PREV_START_REQUEST_CODE) {
            if (data?.extras.orEmpty().getBoolean(UIResult.PREV_RESULT_FINISH)) {
                finish()
            }
        }
        return super.onGalleryFragmentResult(requestCode, resultCode, data)
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

    override fun onClickCheckBoxFileNotExist() {
    }

    override fun onClickCheckBoxMaxCount() {
    }

    override fun onClickItemFileNotExist() {
    }

    override fun onChangedCheckBox(isSelect: Boolean, scanEntity: ScanEntity) {
    }

    override fun onChangedScreen(selectCount: Int) {
    }

    override fun onChangedPrevCount(selectCount: Int) {
    }

    override fun onPhotoItemClick(selectEntities: ArrayList<ScanEntity>, position: Int, parentId: Long) {
        PreActivity.newInstance(
                galleryFragment(),
                galleryFragment().currentEntities,
                selectEntities,
                galleryBundle,
                galleryUiBundle,
                if (parentId.isScanAll() && !galleryBundle.hideCamera) position - 1 else position)
    }

    override fun onCameraCanceled() {
    }

    override fun onCameraResultError() {
    }

    override fun onCameraOpenStatus(status: CameraStatus) {
    }

    override fun onUCropOptions(): UCrop.Options {
        val uCropBundle = galleryUiBundle.uCropBundle
        return if (uCropBundle.isEmpty) super.onUCropOptions() else UCrop.Options().apply {
            this.optionBundle.putAll(uCropBundle)
        }
    }

    override fun onUCropCanceled() {
    }

    override fun onUCropError(throwable: Throwable?) {
    }

    override fun onUCropResources(uri: Uri) {
    }

    override fun onScanSuccessEmpty() {
    }

    override fun onOpenVideoPlayError() {
    }

    override fun onPermissionsDenied(type: PermissionCode) {
    }

    override fun onGalleryRootFinish() {
    }

    override fun onGalleryRootBackPressed() {
    }

    override fun onGalleryPreEmpty() {
    }

    override fun onGallerySelectEmpty() {
    }

    override fun onGalleryFinderEmpty() {
    }

    override fun onGalleryResources(entities: List<ScanEntity>) {
    }
}
