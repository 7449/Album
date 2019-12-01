package com.gallery.ui.activity

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.gallery.core.Gallery
import com.gallery.core.GalleryBundle
import com.gallery.core.action.GalleryAction
import com.gallery.core.constant.GalleryConst
import com.gallery.core.ext.drawable
import com.gallery.core.ext.hasL
import com.gallery.core.ext.statusBarColor
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.ScanConst
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.adapter.FinderAdapter
import kotlinx.android.synthetic.main.gallery_activity_gallery.*

class GalleryActivity : GalleryBaseActivity(), View.OnClickListener, AdapterView.OnItemClickListener, GalleryAction {

    override val layoutId: Int = R.layout.gallery_activity_gallery

    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var galleryFragment: ScanFragment
    private lateinit var finderAdapter: FinderAdapter

    private lateinit var galleryBundle: GalleryBundle
    private lateinit var galleryUiBundle: GalleryUiBundle

    override fun initView() {
        galleryPre.setOnClickListener(this)
        gallerySelect.setOnClickListener(this)
        galleryFinderAll.setOnClickListener(this)
        listPopupWindow = ListPopupWindow(this)
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {
        galleryBundle = intent.extras?.getParcelable(GalleryConst.EXTRA_GALLERY_OPTIONS) ?: GalleryBundle()
        galleryUiBundle = intent.extras?.getParcelable(GalleryConst.EXTRA_GALLERY_UI_OPTIONS) ?: GalleryUiBundle()

        window.statusBarColor(ContextCompat.getColor(this, galleryUiBundle.statusBarColor))
        galleryFinderAll.text = getString(galleryBundle.allName)
        galleryPre.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        gallerySelect.visibility = if (galleryBundle.radio) View.GONE else View.VISIBLE
        galleryToolbar.setTitle(galleryUiBundle.toolbarText)
        galleryToolbar.setTitleTextColor(ContextCompat.getColor(this, galleryUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, galleryUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, galleryUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        galleryToolbar.navigationIcon = drawable
        galleryToolbar.setBackgroundColor(ContextCompat.getColor(this, galleryUiBundle.toolbarBackground))
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
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)
        if (fragment != null) {
            galleryFragment = fragment as ScanFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            galleryFragment = ScanFragment.newInstance(galleryBundle)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.galleryFrame, galleryFragment, ScanFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    private fun initBottomView() {
        galleryBottomView.setBackgroundColor(ContextCompat.getColor(this, galleryUiBundle.bottomViewBackground))
        galleryFinderAll.textSize = galleryUiBundle.bottomFinderTextSize
        galleryFinderAll.setTextColor(ContextCompat.getColor(this, galleryUiBundle.bottomFinderTextColor))
        galleryFinderAll.setCompoundDrawables(null, null, drawable(galleryUiBundle.bottomFinderTextCompoundDrawable, galleryUiBundle.bottomFinderTextDrawableColor), null)
        if (galleryUiBundle.bottomFinderTextBackground != View.NO_ID) {
            galleryFinderAll.setBackgroundResource(galleryUiBundle.bottomFinderTextBackground)
        }
        galleryPre.setText(galleryUiBundle.bottomPreViewText)
        galleryPre.textSize = galleryUiBundle.bottomPreViewTextSize
        galleryPre.setTextColor(ContextCompat.getColor(this, galleryUiBundle.bottomPreViewTextColor))
        if (galleryUiBundle.bottomPreviewTextBackground != View.NO_ID) {
            galleryPre.setBackgroundResource(galleryUiBundle.bottomPreviewTextBackground)
        }
        gallerySelect.setText(galleryUiBundle.bottomSelectText)
        gallerySelect.textSize = galleryUiBundle.bottomSelectTextSize
        gallerySelect.setTextColor(ContextCompat.getColor(this, galleryUiBundle.bottomSelectTextColor))
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
        finderAdapter = FinderAdapter(galleryUiBundle)
        listPopupWindow.setAdapter(finderAdapter)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.galleryPre -> {
                if (galleryFragment.selectEntity.isEmpty()) {
                    Gallery.instance.galleryListener?.onGalleryPreEmpty()
                    return
                }
                PreActivity.newInstance(
                        galleryBundle,
                        galleryUiBundle,
                        galleryFragment.selectEntity,
                        galleryFragment.selectEntity,
                        0,
                        galleryFragment
                )
            }
            R.id.gallerySelect -> {
                if (galleryFragment.selectEntity.isEmpty()) {
                    Gallery.instance.galleryListener?.onGallerySelectEmpty()
                    return
                }
                Gallery.instance.galleryListener?.onGalleryResources(galleryFragment.selectEntity)
                if (galleryBundle.selectImageFinish) {
                    finish()
                }
            }
            R.id.galleryFinderAll -> {
                val finderEntity = galleryFragment.finderList
                if (finderEntity.isNotEmpty()) {
                    finderAdapter.list = finderEntity
                    listPopupWindow.show()
                    listPopupWindow.listView?.setBackgroundColor(ContextCompat.getColor(this, galleryUiBundle.listPopupBackground))
                    return
                }
                Gallery.instance.galleryListener?.onGalleryContainerFinderEmpty()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getItem(position)
        if (finder.parent == galleryFragment.parent) {
            listPopupWindow.dismiss()
            return
        }
        galleryFragment.finderName = finder.bucketDisplayName
        galleryFinderAll.text = finder.bucketDisplayName
        galleryFragment.onScanGallery(finder.parent, isFinder = true, result = false)
        listPopupWindow.dismiss()
    }

    override fun onGalleryItemClick(selectEntity: ArrayList<ScanEntity>, position: Int, parentId: Long) {
        PreActivity.newInstance(
                galleryBundle,
                galleryUiBundle,
                selectEntity,
                galleryFragment.allGalleryList,
                if (parentId == ScanConst.ALL && !galleryBundle.hideCamera) position - 1 else position,
                galleryFragment)
    }

    override fun onGalleryScreenChanged(selectCount: Int) {
    }

    override fun onChangedCheckBoxCount(view: View, selectCount: Int, galleryEntity: ScanEntity) {
    }

    override fun onPrevChangedCount(selectCount: Int) {
    }

    override fun onBackPressed() {
        Gallery.instance.galleryListener?.onGalleryContainerBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        galleryFragment.disconnect()
        super.onDestroy()
    }
}
