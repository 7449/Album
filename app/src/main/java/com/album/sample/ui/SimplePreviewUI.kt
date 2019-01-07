package com.album.sample.ui

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.album.*
import com.album.sample.R
import com.album.ui.AlbumUiBundle
import com.album.ui.activity.AlbumBaseActivity
import com.album.ui.fragment.PrevFragment
import kotlinx.android.synthetic.main.activity_simple_preview.*

/**
 * by y on 22/08/2017.
 */
class SimplePreviewUI : AlbumBaseActivity(), AlbumPreviewParentListener {

    companion object {
        fun start(albumBundle: AlbumBundle, uiBundle: AlbumUiBundle, multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String, fragment: Fragment) {
            val bundle = Bundle().apply {
                putParcelableArrayList(TYPE_PREVIEW_KEY, multiplePreviewList)
                putInt(TYPE_PREVIEW_POSITION_KEY, position)
                putString(TYPE_PREVIEW_BUCKET_ID, bucketId)
                putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
            }
            fragment.startActivityForResult(Intent(fragment.activity, SimplePreviewUI::class.java).putExtras(bundle), TYPE_PREVIEW_CODE)
        }
    }

    private lateinit var prevFragment: PrevFragment
    private lateinit var albumBundle: AlbumBundle
    private lateinit var uiBundle: AlbumUiBundle

    override fun initView() {
        preview_bottom_view_tv_select.setOnClickListener {
            val entity = prevFragment.getSelectEntity()
            if (entity.isEmpty()) {
                Album.instance.albumListener?.onAlbumPreviewSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener?.onAlbumResources(entity)
            prevFragment.isRefreshAlbumUI(false, true)
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        uiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumUiBundle()

        preview_bottom_view.setBackgroundColor(ContextCompat.getColor(this, uiBundle.previewBottomViewBackground))
        preview_bottom_view_tv_select.setText(uiBundle.previewBottomOkText)
        preview_bottom_view_tv_select.textSize = uiBundle.previewBottomOkTextSize
        preview_bottom_view_tv_select.setTextColor(ContextCompat.getColor(this, uiBundle.previewBottomOkTextColor))
        preview_root_view.setBackgroundColor(ContextCompat.getColor(this, uiBundle.previewBackground))

        initTitle()
        preview_toolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(uiBundle.previewFinishRefresh, false) }


        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commit()
        } else {
            prevFragment = PrevFragment.newInstance(checkNotBundleNull(intent.extras))
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.preview_fragment, prevFragment, PrevFragment::class.java.simpleName) }
                    .commit()
        }
        prevFragment.albumParentListener = this
    }

    private fun initTitle() {
        preview_toolbar.setTitleTextColor(ContextCompat.getColor(this, uiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, uiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, uiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        preview_toolbar.navigationIcon = drawable
        preview_toolbar.setBackgroundColor(ContextCompat.getColor(this, uiBundle.toolbarBackground))
        if (hasL()) {
            preview_toolbar.elevation = uiBundle.toolbarElevation
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_simple_preview

    override fun onChangedCount(currentCount: Int) {
    }

    override fun onChangedToolbarCount(currentPos: Int, maxPos: Int) {
        preview_toolbar.title = getString(uiBundle.previewTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
        prevFragment.isRefreshAlbumUI(uiBundle.previewBackRefresh, false)
        super.onBackPressed()
    }
}
