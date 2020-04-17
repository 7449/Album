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
import com.gallery.core.CameraStatus
import com.gallery.core.GalleryBundle
import com.gallery.core.PermissionCode
import com.gallery.core.callback.IGallery
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.callback.IGalleryPrev
import com.gallery.core.ext.*
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.ui.Gallery
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.UIResult
import com.gallery.ui.adapter.FinderAdapter
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

class GalleryActivity : GalleryBaseActivity(R.layout.gallery_activity_gallery), View.OnClickListener, AdapterView.OnItemClickListener, IGalleryCallback {

    private val finderAdapter by lazy {
        FinderAdapter(galleryUiBundle)
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

        window.statusBarColor(color(galleryUiBundle.statusBarColor))
        galleryPre.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        gallerySelect.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        galleryToolbar.setTitle(galleryUiBundle.toolbarText)
        galleryToolbar.setTitleTextColor(color(galleryUiBundle.toolbarTextColor))
        val drawable = drawable(galleryUiBundle.toolbarIcon)
        drawable?.setColorFilter(color(galleryUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        galleryToolbar.navigationIcon = drawable
        galleryToolbar.setBackgroundColor(color(galleryUiBundle.toolbarBackground))
        if (hasL()) {
            galleryToolbar.elevation = galleryUiBundle.toolbarElevation
        }
        galleryToolbar.setNavigationOnClickListener {
            Gallery.instance.galleryListener?.onGalleryContainerFinish()
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
        galleryBottomView.setBackgroundColor(color(galleryUiBundle.bottomViewBackground))
        galleryFinderAll.textSize = galleryUiBundle.bottomFinderTextSize
        galleryFinderAll.setTextColor(color(galleryUiBundle.bottomFinderTextColor))
        galleryFinderAll.setCompoundDrawables(null, null, drawable(galleryUiBundle.bottomFinderTextCompoundDrawable, galleryUiBundle.bottomFinderTextDrawableColor), null)
        if (galleryUiBundle.bottomFinderTextBackground != View.NO_ID) {
            galleryFinderAll.setBackgroundResource(galleryUiBundle.bottomFinderTextBackground)
        }
        galleryPre.setText(galleryUiBundle.bottomPreViewText)
        galleryPre.textSize = galleryUiBundle.bottomPreViewTextSize
        galleryPre.setTextColor(color(galleryUiBundle.bottomPreViewTextColor))
        if (galleryUiBundle.bottomPreviewTextBackground != View.NO_ID) {
            galleryPre.setBackgroundResource(galleryUiBundle.bottomPreviewTextBackground)
        }
        gallerySelect.setText(galleryUiBundle.bottomSelectText)
        gallerySelect.textSize = galleryUiBundle.bottomSelectTextSize
        gallerySelect.setTextColor(color(galleryUiBundle.bottomSelectTextColor))
        if (galleryUiBundle.bottomSelectTextBackground != View.NO_ID) {
            gallerySelect.setBackgroundResource(galleryUiBundle.bottomSelectTextBackground)
        }
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
                    Gallery.instance.galleryListener?.onGalleryPreEmpty()
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
                    Gallery.instance.galleryListener?.onGallerySelectEmpty()
                    return
                }
                Gallery.instance.galleryListener?.onGalleryResources(galleryFragment().selectEntities)
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
                    listPopupWindow.listView?.setBackgroundColor(color(galleryUiBundle.listPopupBackground))
                    return
                }
                Gallery.instance.galleryListener?.onGalleryContainerFinderEmpty()
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
        galleryFragment().onScanGallery(finder.parent, isFinder = true, result = false)
        listPopupWindow.dismiss()
    }

    override fun onPhotoResult(scanEntity: ScanEntity?) {
    }

    override fun onBackPressed() {
        Gallery.instance.galleryListener?.onGalleryContainerBackPressed()
        super.onBackPressed()
    }

    override fun onGalleryResource(scanEntities: ArrayList<ScanEntity>) {
    }

    override fun onGalleryFragmentResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK && requestCode == IGalleryPrev.PREV_START_REQUEST_CODE) {
            if (data?.extras.orEmpty().getBoolean(IGalleryPrev.PREV_RESULT_FINISH)) {
                finish()
            }
        }
        return super.onGalleryFragmentResult(requestCode, resultCode, data)
    }

    override fun onDisplayImageView(width: Int, height: Int, galleryEntity: ScanEntity, container: FrameLayout) {
        Gallery.instance.galleryImageLoader?.onDisplayGallery(width, height, galleryEntity, container)
    }

    override fun onClickCheckBoxFileNotExist() {
    }

    override fun onClickCheckBoxMaxCount() {
    }

    override fun onClickItemFileNotExist() {
    }

    override fun onChangedCheckBox(isSelect: Boolean, scanEntity: ScanEntity) {
    }

    override fun onScreenChanged(selectCount: Int) {
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

    override fun onOpenCameraStatus(status: CameraStatus) {
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
}
