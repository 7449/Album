package com.album.ui.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.album.*
import com.album.core.hasL
import com.album.core.orEmpty
import com.album.core.scan.AlbumEntity
import com.album.core.settingStatusBarColor
import com.album.core.ui.AlbumBaseActivity
import com.album.listener.AlbumPreviewParentListener
import com.album.ui.AlbumUiBundle
import com.album.ui.R
import com.album.ui.fragment.PrevFragment
import kotlinx.android.synthetic.main.album_activity_preview.*

/**
 * @author y
 */
class PreviewActivity : AlbumBaseActivity(), AlbumPreviewParentListener {

    override val layoutId: Int = R.layout.album_activity_preview

    companion object {
        @JvmStatic
        fun start(albumBundle: AlbumBundle, uiBundle: AlbumUiBundle, multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long, fragment: Fragment) {
            val bundle = Bundle().apply {
                putParcelableArrayList(TYPE_PREVIEW_KEY, multiplePreviewList)
                putInt(TYPE_PREVIEW_POSITION_KEY, position)
                putLong(TYPE_PREVIEW_PARENT, parent)
                putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
            }
            fragment.startActivityForResult(Intent(fragment.activity, PreviewActivity::class.java).putExtras(bundle), TYPE_PREVIEW_REQUEST_CODE)
        }
    }

    private lateinit var prevFragment: PrevFragment

    private lateinit var albumBundle: AlbumBundle
    private lateinit var uiBundle: AlbumUiBundle

    override fun initView() {
        preview_bottom_view_tv_select.setOnClickListener {
            if (prevFragment.getSelectEntity().isEmpty()) {
                Album.instance.albumListener?.onAlbumContainerPreviewSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener?.onAlbumResources(prevFragment.getSelectEntity())
            prevFragment.isRefreshAlbumUI(isRefresh = false, isFinish = uiBundle.previewSelectOkFinish)
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        uiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumUiBundle()

        preview_bottom_view.setBackgroundColor(ContextCompat.getColor(this, uiBundle.previewBottomViewBackground))
        preview_bottom_view_tv_select.setText(uiBundle.previewBottomOkText)
        preview_bottom_view_tv_select.textSize = uiBundle.previewBottomOkTextSize
        preview_bottom_view_tv_select.setTextColor(ContextCompat.getColor(this, uiBundle.previewBottomOkTextColor))
        preview_tv_preview_count.textSize = uiBundle.previewBottomCountTextSize
        preview_tv_preview_count.setTextColor(ContextCompat.getColor(this, uiBundle.previewBottomCountTextColor))
        preview_root_view.setBackgroundColor(ContextCompat.getColor(this, uiBundle.previewBackground))
        window.settingStatusBarColor(ContextCompat.getColor(this, uiBundle.statusBarColor))
        preview_toolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(uiBundle.previewFinishRefresh, false) }
        preview_toolbar.setTitleTextColor(ContextCompat.getColor(this, uiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, uiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, uiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        preview_toolbar.navigationIcon = drawable
        preview_toolbar.setBackgroundColor(ContextCompat.getColor(this, uiBundle.toolbarBackground))
        if (hasL()) {
            preview_toolbar.elevation = uiBundle.toolbarElevation
        }

        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            prevFragment = PrevFragment.newInstance(intent.extras.orEmpty())
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.preview_fragment, prevFragment, PrevFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
        prevFragment.albumParentListener = this
    }

    override fun onChangedCheckBoxCount(currentMaxCount: Int) {
        preview_tv_preview_count.text = String.format("%s / %s", currentMaxCount.toString(), albumBundle.multipleMaxCount)
    }

    override fun onChangedViewPager(currentPos: Int, maxPos: Int) {
        preview_toolbar.title = getString(uiBundle.previewTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
        prevFragment.isRefreshAlbumUI(uiBundle.previewBackRefresh, false)
        super.onBackPressed()
    }

}
