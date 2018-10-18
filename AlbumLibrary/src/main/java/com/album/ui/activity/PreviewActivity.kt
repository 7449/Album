package com.album.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.R
import com.album.ui.fragment.PrevFragment
import com.album.ui.view.PrevFragmentToAtyListener
import com.album.util.AlbumTool

/**
 * @author y
 */
class PreviewActivity : AlbumBaseActivity(), PrevFragmentToAtyListener {

    private lateinit var toolbar: Toolbar
    private lateinit var previewCount: AppCompatTextView
    private lateinit var prevFragment: PrevFragment

    override fun initCreate(savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(albumConfig.previewFinishRefresh, false) }
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            return
        }
        prevFragment = PrevFragment.newInstance(intent.extras)
        supportFragmentManager
                .beginTransaction()
                .apply { add(R.id.preview_fragment, prevFragment, PrevFragment::class.java.simpleName) }
                .commit()
    }

    override fun initView() {
        toolbar = findViewById(R.id.preview_toolbar)
        previewCount = findViewById(R.id.preview_tv_preview_count)
        val rootView = findViewById<LinearLayout>(R.id.preview_root_view)
        val previewBottomView = findViewById<RelativeLayout>(R.id.preview_bottom_view)
        val previewOk = findViewById<AppCompatTextView>(R.id.preview_bottom_view_tv_select)
        previewBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumPreviewBottomViewBackground))
        previewOk.setText(albumConfig.albumPreviewBottomOkText)
        previewOk.textSize = albumConfig.albumPreviewBottomOkTextSize.toFloat()
        previewOk.setTextColor(ContextCompat.getColor(this, albumConfig.albumPreviewBottomOkTextColor))
        previewCount.textSize = albumConfig.albumPreviewBottomCountTextSize.toFloat()
        previewCount.setTextColor(ContextCompat.getColor(this, albumConfig.albumPreviewBottomCountTextColor))
        rootView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumPreviewBackground))
        previewOk.setOnClickListener {
            val entity = prevFragment.getSelectEntity()
            if (entity.isEmpty()) {
                Album.instance.albumListener.onAlbumPreviewSelectNull()
                return@setOnClickListener
            }
            Album.instance.albumListener.onAlbumResources(entity)
            prevFragment.isRefreshAlbumUI(false, true)
        }
    }

    override fun initTitle() {
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.albumToolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumConfig.albumToolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumConfig.albumToolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        toolbar.navigationIcon = drawable
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumToolbarBackground))
        if (AlbumTool.hasL()) {
            toolbar.elevation = albumConfig.albumToolbarElevation
        }
    }

    override fun getLayoutId(): Int = R.layout.album_activity_preview

    override fun onChangedCount(currentPos: Int) {
        previewCount.text = String.format("%s / %s", currentPos.toString(), Album.instance.config.multipleMaxCount)
    }

    override fun onChangedToolbarCount(currentPos: Int, maxPos: Int) {
        toolbar.title = getString(albumConfig.albumPreviewTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
        prevFragment.isRefreshAlbumUI(albumConfig.previewBackRefresh, false)
        super.onBackPressed()
    }

}
