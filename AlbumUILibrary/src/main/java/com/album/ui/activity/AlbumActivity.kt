package com.album.ui.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.album.*
import com.album.ui.AlbumUiBundle
import com.album.ui.R
import com.album.ui.adapter.FinderAdapter
import com.album.ui.fragment.AlbumFragment
import com.album.util.getDrawable
import com.album.util.hasL
import com.album.util.setStatusBarColor

/**
 * by y on 14/08/2017.
 */

class AlbumActivity : AlbumBaseActivity(), View.OnClickListener, AdapterView.OnItemClickListener, AlbumParentListener {

    private lateinit var toolbar: Toolbar
    private lateinit var preview: AppCompatTextView
    private lateinit var select: AppCompatTextView
    private lateinit var finderTv: AppCompatTextView
    private lateinit var listPopupWindow: ListPopupWindow
    private lateinit var albumBottomView: RelativeLayout
    private lateinit var albumFragment: AlbumFragment
    private lateinit var finderAdapter: FinderAdapter

    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumUiBundle: AlbumUiBundle

    override fun initView() {
        toolbar = findViewById(R.id.album_toolbar)
        preview = findViewById(R.id.album_tv_preview)
        select = findViewById(R.id.album_tv_select)
        finderTv = findViewById(R.id.album_tv_finder_all)
        albumBottomView = findViewById(R.id.album_bottom_view)
        preview.setOnClickListener(this)
        select.setOnClickListener(this)
        finderTv.setOnClickListener(this)
        listPopupWindow = ListPopupWindow(this)
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        albumUiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumUiBundle()

        setStatusBarColor(ContextCompat.getColor(this, albumUiBundle.statusBarColor), window)
        preview.visibility = if (albumBundle.radio) View.GONE else View.VISIBLE
        select.visibility = if (albumBundle.radio) View.GONE else View.VISIBLE
        toolbar.setTitle(albumUiBundle.toolbarText)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        toolbar.navigationIcon = drawable
        toolbar.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.toolbarBackground))
        if (hasL()) {
            toolbar.elevation = albumUiBundle.toolbarElevation
        }
        toolbar.setNavigationOnClickListener {
            Album.instance.albumListener?.onAlbumActivityFinish()
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
            supportFragmentManager.beginTransaction().show(fragment).commit()
        } else {
            albumFragment = AlbumFragment.newInstance(albumBundle)
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.album_frame, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commit()
        }
        albumFragment.albumParentListener = this
    }

    private fun initBottomView() {
        finderTv.text = if (TextUtils.isEmpty(albumFragment.finderName)) getString(R.string.album_all) else albumFragment.finderName
        albumBottomView.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.bottomViewBackground))
        finderTv.textSize = albumUiBundle.bottomFinderTextSize
        finderTv.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomFinderTextColor))
        finderTv.setCompoundDrawables(null, null, getDrawable(this, albumUiBundle.bottomFinderTextCompoundDrawable, albumUiBundle.bottomFinderTextDrawableColor), null)
        if (albumUiBundle.bottomFinderTextBackground != -1) {
            finderTv.setBackgroundResource(albumUiBundle.bottomFinderTextBackground)
        }
        preview.setText(albumUiBundle.bottomPreViewText)
        preview.textSize = albumUiBundle.bottomPreViewTextSize
        preview.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomPreViewTextColor))
        if (albumUiBundle.bottomPreviewTextBackground != -1) {
            preview.setBackgroundResource(albumUiBundle.bottomPreviewTextBackground)
        }
        select.setText(albumUiBundle.bottomSelectText)
        select.textSize = albumUiBundle.bottomSelectTextSize
        select.setTextColor(ContextCompat.getColor(this, albumUiBundle.bottomSelectTextColor))
        if (albumUiBundle.bottomSelectTextBackground != -1) {
            select.setBackgroundResource(albumUiBundle.bottomSelectTextBackground)
        }
    }

    private fun initFinderView() {
        listPopupWindow.anchorView = finderTv
        listPopupWindow.width = albumUiBundle.listPopupWidth
        listPopupWindow.horizontalOffset = albumUiBundle.listPopupHorizontalOffset
        listPopupWindow.verticalOffset = albumUiBundle.listPopupVerticalOffset
        listPopupWindow.isModal = true
        listPopupWindow.setOnItemClickListener(this)
        finderAdapter = FinderAdapter(ArrayList(), albumUiBundle)
        listPopupWindow.setAdapter(finderAdapter)
        listPopupWindow.listView?.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.listPopupItemBackground))
    }

    override fun getLayoutId(): Int = R.layout.album_activity_album

    override fun onClick(v: View) {
        when (v.id) {
            R.id.album_tv_preview -> {
                val multiplePreview = albumFragment.multiplePreview()
                if (multiplePreview != null) {
                    val bundle = Bundle()
                    bundle.putParcelableArrayList(AlbumConstant.PREVIEW_KEY, multiplePreview)
                    bundle.putString(AlbumConstant.PREVIEW_BUCKET_ID, AlbumConstant.PREVIEW_BUTTON_KEY)
                    albumFragment.startActivityForResult(Intent(this, PreviewActivity::class.java).putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE)
                }
            }
            R.id.album_tv_select -> albumFragment.multipleSelect()
            R.id.album_tv_finder_all -> {
                val finderEntity = albumFragment.getFinderEntity()
                if (!finderEntity.isEmpty()) {
                    finderAdapter.refreshData(finderEntity)
                    listPopupWindow.show()
                    return
                }
                Album.instance.albumListener?.onAlbumFinderEmpty()
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val finder = finderAdapter.getFinder(position)
        albumFragment.finderName = finder.dirName
        finderTv.text = finder.dirName
        albumFragment.onScanAlbum(finder.bucketId, true, false)
        listPopupWindow.dismiss()
    }

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String) {
        PreviewActivity.start(albumBundle, albumUiBundle, multiplePreviewList, if (TextUtils.isEmpty(bucketId) && !albumBundle.hideCamera) position - 1 else position, bucketId, albumFragment)
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
