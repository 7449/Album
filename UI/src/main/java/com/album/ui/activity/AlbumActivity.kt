package com.album.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.album.*
import com.album.core.drawable
import com.album.core.hasL
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.settingStatusBarColor
import com.album.core.ui.AlbumBaseActivity
import com.album.listener.AlbumParentListener
import com.album.ui.AlbumUiBundle
import com.album.ui.R
import com.album.ui.adapter.FinderAdapter
import com.album.ui.fragment.AlbumFragment
import kotlinx.android.synthetic.main.album_activity_album.*

/**
 * by y on 14/08/2017.
 */

class AlbumActivity : AlbumBaseActivity(), View.OnClickListener, AdapterView.OnItemClickListener, AlbumParentListener {

    override val layoutId: Int = R.layout.album_activity_album

    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var albumFragment: AlbumFragment
    private lateinit var finderAdapter: FinderAdapter

    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumUiBundle: AlbumUiBundle

    override fun initView() {
        album_tv_preview.setOnClickListener(this)
        album_tv_select.setOnClickListener(this)
        album_tv_finder_all.setOnClickListener(this)
        listPopupWindow = ListPopupWindow(this)
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        albumUiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumUiBundle()

        window.settingStatusBarColor(ContextCompat.getColor(this, albumUiBundle.statusBarColor))
        album_tv_preview.visibility = if (albumBundle.radio) View.GONE else View.VISIBLE
        album_tv_select.visibility = if (albumBundle.radio) View.GONE else View.VISIBLE
        album_toolbar.setTitle(albumUiBundle.toolbarText)
        album_toolbar.setTitleTextColor(ContextCompat.getColor(this, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        album_toolbar.navigationIcon = drawable
        album_toolbar.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.toolbarBackground))
        if (hasL()) {
            album_toolbar.elevation = albumUiBundle.toolbarElevation
        }
        album_toolbar.setNavigationOnClickListener {
            Album.instance.albumListener?.onAlbumContainerFinish()
            finish()
        }

        initFragment()
        initBottomView()
        initFinderView()
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
                    .apply { add(R.id.album_frame, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
        albumFragment.albumParentListener = this
    }

    private fun initBottomView() {
        album_tv_finder_all.text = if (TextUtils.isEmpty(albumFragment.finderName)) getString(R.string.album_all) else albumFragment.finderName
        album_bottom_view.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.bottomViewBackground))
        album_tv_finder_all.textSize = albumUiBundle.bottomFinderTextSize
        album_tv_finder_all.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomFinderTextColor))
        album_tv_finder_all.setCompoundDrawables(null, null, drawable(albumUiBundle.bottomFinderTextCompoundDrawable, albumUiBundle.bottomFinderTextDrawableColor), null)
        if (albumUiBundle.bottomFinderTextBackground != -1) {
            album_tv_finder_all.setBackgroundResource(albumUiBundle.bottomFinderTextBackground)
        }
        album_tv_preview.setText(albumUiBundle.bottomPreViewText)
        album_tv_preview.textSize = albumUiBundle.bottomPreViewTextSize
        album_tv_preview.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomPreViewTextColor))
        if (albumUiBundle.bottomPreviewTextBackground != -1) {
            album_tv_preview.setBackgroundResource(albumUiBundle.bottomPreviewTextBackground)
        }
        album_tv_select.setText(albumUiBundle.bottomSelectText)
        album_tv_select.textSize = albumUiBundle.bottomSelectTextSize
        album_tv_select.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomSelectTextColor))
        if (albumUiBundle.bottomSelectTextBackground != -1) {
            album_tv_select.setBackgroundResource(albumUiBundle.bottomSelectTextBackground)
        }
    }

    private fun initFinderView() {
        listPopupWindow.anchorView = album_tv_finder_all
        listPopupWindow.width = albumUiBundle.listPopupWidth
        listPopupWindow.horizontalOffset = albumUiBundle.listPopupHorizontalOffset
        listPopupWindow.verticalOffset = albumUiBundle.listPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
        finderAdapter = FinderAdapter(ArrayList(), albumUiBundle)
        listPopupWindow.setAdapter(finderAdapter)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.album_tv_preview -> {
                val multiplePreview = albumFragment.selectPreview()
                if (multiplePreview.isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(TYPE_PREVIEW_KEY, multiplePreview)
                    bundle.putLong(TYPE_PREVIEW_PARENT, AlbumScan.PREV_PARENT)
                    bundle.putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
                    bundle.putParcelable(EXTRA_ALBUM_UI_OPTIONS, albumUiBundle)
                    albumFragment.startActivityForResult(Intent(this, PreviewActivity::class.java).putExtras(bundle), TYPE_PREVIEW_REQUEST_CODE)
                }
            }
            R.id.album_tv_select -> albumFragment.multipleSelect()
            R.id.album_tv_finder_all -> {
                val finderEntity = albumFragment.finderList
                if (finderEntity.isNotEmpty()) {
                    finderAdapter.refreshData(finderEntity)
                    listPopupWindow.show()
                    listPopupWindow.listView?.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.listPopupBackground))
                    return
                }
                Album.instance.albumListener?.onAlbumContainerFinderEmpty()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getFinder(position)
        if (finder.parent == albumFragment.parent) {
            listPopupWindow.dismiss()
            return
        }
        albumFragment.finderName = finder.bucketDisplayName
        album_tv_finder_all.text = finder.bucketDisplayName
        albumFragment.onScanAlbum(finder.parent, isFinder = true, result = false)
        listPopupWindow.dismiss()
    }

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long) {
        PreviewActivity.start(albumBundle, albumUiBundle, multiplePreviewList, if (parent == AlbumScan.ALL_PARENT && !albumBundle.hideCamera) position - 1 else position, parent, albumFragment)
    }

    override fun onAlbumScreenChanged(currentMaxCount: Int) {
    }

    override fun onChangedCheckBoxCount(view: View, currentMaxCount: Int, albumEntity: AlbumEntity) {
    }

    override fun onPrevChangedCount(currentMaxCount: Int) {
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
