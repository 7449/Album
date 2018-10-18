package com.album.sample.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.album.sample.R
import com.album.ui.activity.AlbumBaseActivity
import com.album.ui.adapter.FinderAdapter
import com.album.ui.fragment.AlbumFragment

/**
 * by y on 22/08/2017.
 */

class SimpleAlbumUI : AlbumBaseActivity(), AdapterView.OnItemClickListener {

    private lateinit var toolbar: Toolbar
    private lateinit var albumFragment: AlbumFragment
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var finerName: AppCompatTextView

    override fun onDestroy() {
        super.onDestroy()
        albumFragment.disconnectMediaScanner()
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        initListPopupWindow()
    }

    private fun initListPopupWindow() {
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow.anchorView = finerName
        listPopupWindow.width = albumConfig.albumListPopupWidth
        listPopupWindow.horizontalOffset = albumConfig.albumListPopupHorizontalOffset
        listPopupWindow.verticalOffset = albumConfig.albumListPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
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
                .apply {
                    add(R.id.sample_album_frame, albumFragment, AlbumFragment::class.java.simpleName)
                }
                .commit()
    }

    override fun initView() {
        initFragment()
        toolbar = findViewById(R.id.sample_toolbar)
        finerName = findViewById(R.id.sample_finder_name)
        finerName.text = if (TextUtils.isEmpty(albumFragment.finderName)) getString(R.string.album_all) else albumFragment.finderName
        finerName.setOnClickListener {
            val finderEntity = albumFragment.getFinderEntity()
            if (!finderEntity.isEmpty()) {
                listPopupWindow.setAdapter(FinderAdapter(finderEntity))
                listPopupWindow.show()
            }
        }
    }

    override fun initTitle() {
        toolbar.title = "sample UI"
        val drawable = ContextCompat.getDrawable(this, albumConfig.albumToolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumConfig.albumToolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumConfig.albumToolbarTextColor))
        toolbar.navigationIcon = drawable
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        toolbar.setNavigationOnClickListener { finish() }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_simple_album
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val listView = listPopupWindow.listView ?: return
        val adapter = listView.adapter as FinderAdapter
        val finder = adapter.getFinder(position)
        albumFragment.finderName = finder.dirName
        finerName.text = finder.dirName
        albumFragment.onScanAlbum(finder.bucketId, true, false)
        listPopupWindow.dismiss()
    }

}
