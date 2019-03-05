package com.album.ui.sample

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.ui.AlbumBaseActivity
import com.album.listener.AlbumParentListener
import com.album.ui.AlbumUiBundle
import com.album.ui.adapter.FinderAdapter
import com.album.ui.fragment.AlbumFragment
import kotlinx.android.synthetic.main.activity_simple_album.*

/**
 * by y on 22/08/2017.
 */

class SimpleAlbumUI : AlbumBaseActivity(), AdapterView.OnItemClickListener, AlbumParentListener {
    override val layoutId: Int
        get() = R.layout.activity_simple_album

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
            Album.instance.albumListener?.onAlbumContainerFinderEmpty()
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
            Album.instance.albumListener?.onAlbumContainerFinish()
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
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            albumFragment = AlbumFragment.newInstance(albumBundle)
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.sample_album_frame, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
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

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getFinder(position)
        albumFragment.finderName = finder.bucketDisplayName
        sample_finder_name.text = finder.bucketDisplayName
        albumFragment.onScanAlbum(finder.parent, isFinder = true, result = false)
        listPopupWindow.dismiss()
    }

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long) {
        SimplePreviewUI.start(albumBundle, albumUiBundle, multiplePreviewList, if (parent == AlbumScan.ALL_PARENT && !albumBundle.hideCamera) position - 1 else position, parent, albumFragment)
    }

    override fun onBackPressed() {
        Album.instance.albumListener?.onAlbumContainerBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        albumFragment.disconnectMediaScanner()
        super.onDestroy()
    }

}
