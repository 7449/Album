package com.album.ui.wechat.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS
import com.album.core.AlbumCore
import com.album.core.AlbumCore.settingStatusBarColor
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseActivity
import com.album.listener.AlbumParentListener
import com.album.ui.fragment.AlbumFragment
import com.album.ui.wechat.R
import kotlinx.android.synthetic.main.album_wechat_activity.*

/**
 * @author y
 * @create 2018/12/3
 */
class AlbumWeChatUiActivity : AlbumBaseActivity(), AlbumParentListener {

    override val layoutId: Int = R.layout.album_wechat_activity

    private lateinit var albumFragment: AlbumFragment

    private lateinit var albumBundle: AlbumBundle
    private lateinit var albumUiBundle: AlbumWeChatUiBundle

    override fun initView() {
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        albumUiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS)
                ?: AlbumWeChatUiBundle()

        window.settingStatusBarColor(ContextCompat.getColor(this, albumUiBundle.statusBarColor))
        album_wechat_ui_toolbar.setTitle(albumUiBundle.toolbarText)
        album_wechat_ui_toolbar.setTitleTextColor(ContextCompat.getColor(this, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        album_wechat_ui_toolbar.navigationIcon = drawable
        album_wechat_ui_toolbar.setBackgroundColor(ContextCompat.getColor(this, albumUiBundle.toolbarBackground))
        if (AlbumCore.hasL()) {
            album_wechat_ui_toolbar.elevation = albumUiBundle.toolbarElevation
        }
        album_wechat_ui_toolbar.setNavigationOnClickListener {
            Album.instance.albumListener?.onAlbumContainerFinish()
            finish()
        }
        initFragment()
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

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long) {
    }

    override fun onChangedCheckBoxCount(view: View, currentMaxCount: Int, albumEntity: AlbumEntity) {
        album_wechat_ui_title_send.isEnabled = currentMaxCount != 0
        if (currentMaxCount == 0) {
            album_wechat_ui_title_send.text = getString(R.string.album_wechat_ui_title_send)
        } else {
            album_wechat_ui_title_send.text = String.format(getString(R.string.album_wechat_ui_title_send_count), currentMaxCount, albumBundle.multipleMaxCount)
        }
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