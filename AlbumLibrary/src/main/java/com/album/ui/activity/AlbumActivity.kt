package com.album.ui.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import com.album.Album
import com.album.R
import com.album.ui.adapter.FinderAdapter
import com.album.ui.fragment.AlbumFragment
import com.album.ui.view.AlbumFragmentToAtyListener
import com.album.util.AlbumTool

/**
 * by y on 14/08/2017.
 */

class AlbumActivity : AlbumBaseActivity(),
        View.OnClickListener,
        AlbumFragmentToAtyListener,
        AdapterView.OnItemClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var preview: AppCompatTextView
    private lateinit var select: AppCompatTextView
    private lateinit var finderTv: AppCompatTextView
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var albumBottomView: RelativeLayout
    private lateinit var albumFragment: AlbumFragment

    override fun onDestroy() {
        albumFragment.disconnectMediaScanner()
        super.onDestroy()
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        initFragment()
        initBottomView()
        initFinderView()
    }

    override fun initView() {
        toolbar = findViewById(R.id.album_toolbar)
        preview = findViewById(R.id.album_tv_preview)
        select = findViewById(R.id.album_tv_select)
        finderTv = findViewById(R.id.album_tv_finder_all)
        albumBottomView = findViewById(R.id.album_bottom_view)
        listPopupWindow = ListPopupWindow(this)
        preview.setOnClickListener(this)
        select.setOnClickListener(this)
        finderTv.setOnClickListener(this)
        preview.visibility = if (albumConfig.isRadio) View.GONE else View.VISIBLE
        select.visibility = if (albumConfig.isRadio) View.GONE else View.VISIBLE
    }

    private fun initFragment() {
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(AlbumFragment::class.java.simpleName)
        if (fragment != null) {
            albumFragment = fragment as AlbumFragment
            return
        }
        albumFragment = AlbumFragment.newInstance()
        supportFragmentManager
                .beginTransaction()
                .apply { add(R.id.album_frame, albumFragment, AlbumFragment::class.java.simpleName) }
                .commit()
    }

    private fun initBottomView() {
        finderTv.text = if (TextUtils.isEmpty(albumFragment.finderName)) getString(R.string.album_all) else albumFragment.finderName
        albumBottomView.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumBottomViewBackground))
        finderTv.textSize = albumConfig.albumBottomFinderTextSize.toFloat()
        finderTv.setTextColor(ContextCompat.getColor(this, albumConfig.albumBottomFinderTextColor))
        finderTv.setCompoundDrawables(null, null, AlbumTool.getDrawable(this, albumConfig.albumBottomFinderTextCompoundDrawable, albumConfig.albumBottomFinderTextDrawableColor), null)
        if (albumConfig.albumBottomFinderTextBackground != -1) {
            finderTv.setBackgroundResource(albumConfig.albumBottomFinderTextBackground)
        }
        preview.setText(albumConfig.albumBottomPreViewText)
        preview.textSize = albumConfig.albumBottomPreViewTextSize.toFloat()
        preview.setTextColor(ContextCompat.getColor(this, albumConfig.albumBottomPreViewTextColor))
        if (albumConfig.albumBottomPreviewTextBackground != -1) {
            preview.setBackgroundResource(albumConfig.albumBottomPreviewTextBackground)
        }
        select.setText(albumConfig.albumBottomSelectText)
        select.textSize = albumConfig.albumBottomSelectTextSize.toFloat()
        select.setTextColor(ContextCompat.getColor(this, albumConfig.albumBottomSelectTextColor))
        if (albumConfig.albumBottomSelectTextBackground != -1) {
            select.setBackgroundResource(albumConfig.albumBottomSelectTextBackground)
        }
    }

    private fun initFinderView() {
        listPopupWindow.anchorView = finderTv
        listPopupWindow.width = albumConfig.albumListPopupWidth
        listPopupWindow.horizontalOffset = albumConfig.albumListPopupHorizontalOffset
        listPopupWindow.verticalOffset = albumConfig.albumListPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
    }


    override fun initTitle() {
        toolbar.setTitle(albumConfig.albumToolbarText)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.albumToolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumConfig.albumToolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumConfig.albumToolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        toolbar.navigationIcon = drawable
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumToolbarBackground))
        if (AlbumTool.hasL()) {
            toolbar.elevation = albumConfig.albumToolbarElevation
        }
        toolbar.setNavigationOnClickListener {
            Album.instance.albumListener.onAlbumActivityFinish()
            finish()
        }
    }

    override fun getLayoutId(): Int = R.layout.album_activity_album

    override fun onClick(v: View) {
        when (v.id) {
            R.id.album_tv_preview -> albumFragment.multiplePreview()
            R.id.album_tv_select -> albumFragment.multipleSelect()
            R.id.album_tv_finder_all -> {
                val finderEntity = albumFragment.getFinderEntity()
                if (!finderEntity.isEmpty()) {
                    listPopupWindow.setAdapter(FinderAdapter(finderEntity))
                    listPopupWindow.show()
                    val listView = listPopupWindow.listView
                    listView?.setBackgroundColor(ContextCompat.getColor(this, albumConfig.albumListPopupItemBackground))
                    return
                }
                Album.instance.albumListener.onAlbumFinderNull()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val listView = listPopupWindow.listView ?: return
        val adapter = listView.adapter as FinderAdapter
        val finder = adapter.getFinder(position)
        albumFragment.finderName = finder.dirName
        finderTv.text = finder.dirName
        albumFragment.onScanAlbum(finder.bucketId, true, false)
        listPopupWindow.dismiss()
    }

    override fun onChangedFinderName(name: String) {
        finderTv.text = name
    }

    override fun onBackPressed() {
        Album.instance.albumListener.onAlbumActivityBackPressed()
        super.onBackPressed()
    }
}
