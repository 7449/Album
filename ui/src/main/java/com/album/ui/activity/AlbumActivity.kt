package com.album.ui.activity

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.album.core.Album
import com.album.core.AlbumBundle
import com.album.core.AlbumConst
import com.album.core.action.AlbumAction
import com.album.core.ext.drawable
import com.album.core.ext.hasL
import com.album.core.ext.statusBarColor
import com.album.core.ui.fragment.ScanFragment
import com.album.scan.args.ScanConst
import com.album.scan.ScanEntity
import com.album.core.ui.base.AlbumBaseActivity
import com.album.ui.AlbumUiBundle
import com.album.ui.R
import com.album.ui.adapter.FinderAdapter
import kotlinx.android.synthetic.main.album_activity_album.*

class AlbumActivity : AlbumBaseActivity(), View.OnClickListener, AdapterView.OnItemClickListener, AlbumAction {

    override val layoutId: Int = R.layout.album_activity_album

    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var albumFragment: ScanFragment
    private lateinit var finderAdapter: FinderAdapter

    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumUiBundle: AlbumUiBundle

    override fun initView() {
        albumPre.setOnClickListener(this)
        albumSelect.setOnClickListener(this)
        albumFinderAll.setOnClickListener(this)
        listPopupWindow = ListPopupWindow(this)
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        albumUiBundle = intent.extras?.getParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS)
                ?: AlbumUiBundle()

        window.statusBarColor(ContextCompat.getColor(this, albumUiBundle.statusBarColor))
        albumFinderAll.text = getString(albumBundle.allName)
        albumPre.visibility = if (albumBundle.radio) View.GONE else View.VISIBLE
        albumSelect.visibility = if (albumBundle.radio) View.GONE else View.VISIBLE
        albumToolbar.setTitle(albumUiBundle.toolbarText)
        albumToolbar.setTitleTextColor(ContextCompat.getColor(this, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        albumToolbar.navigationIcon = drawable
        albumToolbar.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.toolbarBackground))
        if (hasL()) {
            albumToolbar.elevation = albumUiBundle.toolbarElevation
        }
        albumToolbar.setNavigationOnClickListener {
            Album.instance.albumListener?.onAlbumContainerFinish()
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
            albumFragment = fragment as ScanFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            albumFragment = ScanFragment.newInstance(albumBundle)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.albumFrame, albumFragment, ScanFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    private fun initBottomView() {
        albumBottomView.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.bottomViewBackground))
        albumFinderAll.textSize = albumUiBundle.bottomFinderTextSize
        albumFinderAll.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomFinderTextColor))
        albumFinderAll.setCompoundDrawables(null, null, drawable(albumUiBundle.bottomFinderTextCompoundDrawable, albumUiBundle.bottomFinderTextDrawableColor), null)
        if (albumUiBundle.bottomFinderTextBackground != -1) {
            albumFinderAll.setBackgroundResource(albumUiBundle.bottomFinderTextBackground)
        }
        albumPre.setText(albumUiBundle.bottomPreViewText)
        albumPre.textSize = albumUiBundle.bottomPreViewTextSize
        albumPre.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomPreViewTextColor))
        if (albumUiBundle.bottomPreviewTextBackground != -1) {
            albumPre.setBackgroundResource(albumUiBundle.bottomPreviewTextBackground)
        }
        albumSelect.setText(albumUiBundle.bottomSelectText)
        albumSelect.textSize = albumUiBundle.bottomSelectTextSize
        albumSelect.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomSelectTextColor))
        if (albumUiBundle.bottomSelectTextBackground != -1) {
            albumSelect.setBackgroundResource(albumUiBundle.bottomSelectTextBackground)
        }
    }

    private fun initFinderView() {
        listPopupWindow.anchorView = albumFinderAll
        listPopupWindow.width = albumUiBundle.listPopupWidth
        listPopupWindow.horizontalOffset = albumUiBundle.listPopupHorizontalOffset
        listPopupWindow.verticalOffset = albumUiBundle.listPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
        finderAdapter = FinderAdapter(albumUiBundle)
        listPopupWindow.setAdapter(finderAdapter)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.albumPre -> {
                val multiplePreview = albumFragment.selectPreview()
                if (multiplePreview.isNotEmpty()) {
                    PreActivity.newInstance(
                            albumBundle,
                            albumUiBundle,
                            multiplePreview,
                            multiplePreview,
                            0,
                            albumFragment
                    )
                }
            }
            R.id.albumSelect -> albumFragment.multipleSelect()
            R.id.albumFinderAll -> {
                val finderEntity = albumFragment.finderList
                if (finderEntity.isNotEmpty()) {
                    finderAdapter.list = finderEntity
                    listPopupWindow.show()
                    listPopupWindow.listView?.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.listPopupBackground))
                    return
                }
                Album.instance.albumListener?.onAlbumContainerFinderEmpty()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getItem(position)
        if (finder.parent == albumFragment.parent) {
            listPopupWindow.dismiss()
            return
        }
        albumFragment.finderName = finder.bucketDisplayName
        albumFinderAll.text = finder.bucketDisplayName
        albumFragment.onScanAlbum(finder.parent, isFinder = true, result = false)
        listPopupWindow.dismiss()
    }

    override fun onAlbumItemClick(selectEntity: ArrayList<ScanEntity>, position: Int, parentId: Long) {
        PreActivity.newInstance(
                albumBundle,
                albumUiBundle,
                selectEntity,
                albumFragment.allPreview(),
                if (parentId == ScanConst.ALL && !albumBundle.hideCamera) position - 1 else position,
                albumFragment)
    }

    override fun onAlbumScreenChanged(selectCount: Int) {
    }

    override fun onChangedCheckBoxCount(view: View, selectCount: Int, albumEntity: ScanEntity) {
    }

    override fun onPrevChangedCount(selectCount: Int) {
    }

    override fun onBackPressed() {
        Album.instance.albumListener?.onAlbumContainerBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        albumFragment.disconnectMediaScanner()
        super.onDestroy()
        Album.destroy()
    }
}
