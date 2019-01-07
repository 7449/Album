package com.album.sample.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.album.*
import com.album.sample.R
import com.album.ui.AlbumUiBundle
import com.album.ui.activity.AlbumBaseActivity
import com.album.ui.adapter.FinderAdapter
import com.album.ui.fragment.AlbumFragment
import kotlinx.android.synthetic.main.activity_simple_album.*

/**
 * by y on 22/08/2017.
 */

class SimpleAlbumUI : AlbumBaseActivity(), AdapterView.OnItemClickListener, AlbumParentListener {

    private lateinit var albumFragment: AlbumFragment
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var finderAdapter: FinderAdapter

    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumUiBundle: AlbumUiBundle

    override fun initView() {
        listPopupWindow = ListPopupWindow(this)
        sample_finder_name.setOnClickListener {
            val finderEntity = albumFragment.getFinderEntity()
            if (!finderEntity.isEmpty()) {
                finderAdapter.refreshData(finderEntity)
                listPopupWindow.show()
                listPopupWindow.listView?.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.listPopupBackground))
                return@setOnClickListener
            }
            Album.instance.albumListener?.onAlbumFinderEmpty()
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        albumUiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumUiBundle()

        sample_toolbar.title = "sample UI"
        val drawable = ContextCompat.getDrawable(this, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        sample_toolbar.setTitleTextColor(ContextCompat.getColor(this, albumUiBundle.toolbarTextColor))
        sample_toolbar.navigationIcon = drawable
        sample_toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        sample_toolbar.setNavigationOnClickListener {
            Album.instance.albumListener?.onAlbumActivityFinish()
            finish()
        }
        initFragment()
        sample_finder_name.text = if (TextUtils.isEmpty(albumFragment.finderName)) getString(R.string.album_all) else albumFragment.finderName
        initListPopupWindow()
    }

    private fun initFragment() {
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(AlbumFragment::class.java.simpleName)
        if (fragment != null) {
            albumFragment = fragment as AlbumFragment
            supportFragmentManager.beginTransaction().show(fragment).commit()
        } else {
            albumFragment = AlbumFragment.newInstance(albumBundle)
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.sample_album_frame, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commit()
        }
        albumFragment.albumParentListener = this
    }

    private fun initListPopupWindow() {
        listPopupWindow.anchorView = sample_finder_name
        listPopupWindow.width = albumUiBundle.listPopupWidth
        listPopupWindow.horizontalOffset = albumUiBundle.listPopupHorizontalOffset
        listPopupWindow.verticalOffset = albumUiBundle.listPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
        finderAdapter = FinderAdapter(ArrayList(), albumUiBundle)
        listPopupWindow.setAdapter(finderAdapter)
    }

    override fun getLayoutId(): Int = R.layout.activity_simple_album

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getFinder(position)
        albumFragment.finderName = finder.dirName
        sample_finder_name.text = finder.dirName
        albumFragment.onScanAlbum(finder.bucketId, true, false)
        listPopupWindow.dismiss()
    }

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String) {
        SimplePreviewUI.start(albumBundle, albumUiBundle, multiplePreviewList, if (TextUtils.isEmpty(bucketId) && !albumBundle.hideCamera) position - 1 else position, bucketId, albumFragment)
    }

    override fun onBackPressed() {
        Album.instance.albumListener?.onAlbumActivityBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        albumFragment.disconnectMediaScanner()
        super.onDestroy()
    }

}
