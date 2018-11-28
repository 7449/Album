package com.album.sample.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.sample.R
import com.album.ui.activity.AlbumBaseActivity
import com.album.ui.fragment.PrevFragment
import com.album.ui.view.PrevFragmentToAtyListener
import com.album.util.checkNotBundleNull
import com.album.util.hasL

/**
 * by y on 22/08/2017.
 */
class SimplePreviewUI : AlbumBaseActivity(), PrevFragmentToAtyListener {

    private lateinit var toolbar: Toolbar
    private lateinit var prevFragment: PrevFragment

    override fun initCreate(savedInstanceState: Bundle?) {
        initTitle()
//        toolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(albumConfig.previewFinishRefresh, false) }
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            return
        }
        prevFragment = PrevFragment.newInstance(checkNotBundleNull(intent.extras))
        supportFragmentManager
                .beginTransaction()
                .apply { add(R.id.preview_fragment, prevFragment, PrevFragment::class.java.simpleName) }
                .commit()
    }

    override fun initView() {
        toolbar = findViewById(R.id.preview_toolbar)
        val rootView = findViewById<LinearLayout>(R.id.preview_root_view)
        val previewBottomView = findViewById<RelativeLayout>(R.id.preview_bottom_view)
        val previewOk = findViewById<AppCompatTextView>(R.id.preview_bottom_view_tv_select)
//        previewBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumPreviewBottomViewBackground))
//        previewOk.setText(albumConfig.albumPreviewBottomOkText)
//        previewOk.textSize = albumConfig.albumPreviewBottomOkTextSize.toFloat()
//        previewOk.setTextColor(ContextCompat.getColor(this, albumConfig.albumPreviewBottomOkTextColor))
//        rootView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumPreviewBackground))
        previewOk.setOnClickListener {
            val entity = prevFragment.getSelectEntity()
            if (entity.isEmpty()) {
                Album.instance.albumListener.onAlbumPreviewSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener.onAlbumResources(entity)
            prevFragment.isRefreshAlbumUI(false, true)
        }
    }

    private fun initTitle() {
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.albumToolbarTextColor))
//        val drawable = ContextCompat.getDrawable(this, albumConfig.albumToolbarIcon)
//        drawable?.setColorFilter(ContextCompat.getColor(this, albumConfig.albumToolbarIconColor), PorterDuff.Mode.SRC_ATOP)
//        toolbar.navigationIcon = drawable
//        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumToolbarBackground))
//        if (hasL()) {
//            toolbar.elevation = albumConfig.albumToolbarElevation
//        }
    }

    override fun getLayoutId(): Int = R.layout.activity_simple_preview

    override fun onChangedCount(currentPos: Int) {
    }

    override fun onChangedToolbarCount(currentPos: Int, maxPos: Int) {
//        toolbar.title = getString(albumConfig.albumPreviewTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
//        prevFragment.isRefreshAlbumUI(albumConfig.previewBackRefresh, false)
        super.onBackPressed()
    }
}
