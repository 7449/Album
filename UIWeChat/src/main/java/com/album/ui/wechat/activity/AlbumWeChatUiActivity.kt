package com.album.ui.wechat.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS
import com.album.core.hasL
import com.album.core.scan.AlbumEntity
import com.album.core.scan.hasVideo
import com.album.core.settingStatusBarColor
import com.album.core.ui.AlbumBaseActivity
import com.album.listener.AlbumParentListener
import com.album.ui.fragment.AlbumFragment
import kotlinx.android.synthetic.main.album_wechat_activity.*


/**
 * @author y
 * @create 2018/12/3
 */
class AlbumWeChatUiActivity : AlbumBaseActivity(), AlbumParentListener, AlbumWeChatUiFinder.OnFinderActionListener {

    override val layoutId: Int = com.album.ui.wechat.R.layout.album_wechat_activity

    private lateinit var albumFragment: AlbumFragment
    private var finderFragment: AlbumWeChatUiFinder? = null

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
        if (hasL()) {
            album_wechat_ui_toolbar.elevation = albumUiBundle.toolbarElevation
        }
        album_wechat_ui_toolbar.setNavigationOnClickListener {
            Album.instance.albumListener?.onAlbumContainerFinish()
            finish()
        }
        album_wechat_ui_title_send.setOnClickListener {
            val selectEntity = albumFragment.getSelectEntity()
            if (selectEntity.isEmpty()) {
                return@setOnClickListener
            }
            Toast.makeText(applicationContext, album_wechat_ui_original_image.isChecked.toString(), Toast.LENGTH_SHORT).show()
        }
        album_wechat_ui_finder.setOnClickListener {
            finderFragment = AlbumWeChatUiFinder.newInstance(albumFragment.finderList, supportFragmentManager, AlbumWeChatUiFinder::class.java.simpleName)
            finderFragment?.onfinderAction = this@AlbumWeChatUiActivity
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
                    .apply { add(com.album.ui.wechat.R.id.album_frame, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
        albumFragment.albumParentListener = this
    }

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long) {
    }

    override fun onAlbumScreenChanged(currentMaxCount: Int) {
        onChangedCheckBoxCount(View(applicationContext), currentMaxCount, AlbumEntity())
    }

    override fun onAlbumCheckBoxFilter(view: View, position: Int, albumEntity: AlbumEntity): Boolean {
        if (albumEntity.hasVideo()) {
            Toast.makeText(view.context, format(albumEntity.duration.toInt()), Toast.LENGTH_SHORT).show()
        }
        return super.onAlbumCheckBoxFilter(view, position, albumEntity)
    }

    override fun onChangedCheckBoxCount(view: View, currentMaxCount: Int, albumEntity: AlbumEntity) {
        album_wechat_ui_title_send.isEnabled = currentMaxCount != 0
        if (currentMaxCount == 0) {
            album_wechat_ui_title_send.text = getString(com.album.ui.wechat.R.string.album_wechat_ui_title_send)
            album_wechat_ui_preview.text = getString(com.album.ui.wechat.R.string.album_wechat_ui_title_prev)
        } else {
            album_wechat_ui_title_send.text = String.format(getString(com.album.ui.wechat.R.string.album_wechat_ui_title_send_count), currentMaxCount, albumBundle.multipleMaxCount)
            album_wechat_ui_preview.text = String.format(getString(com.album.ui.wechat.R.string.album_wechat_ui_title_prev_count), currentMaxCount, albumBundle.multipleMaxCount)
        }
    }

    override fun onFinderActionItemClick(view: View, position: Int, albumEntity: AlbumEntity) {
        if (albumEntity.parent == albumFragment.parent) {
            finderFragment?.dismiss()
            return
        }
        albumFragment.finderName = albumEntity.bucketDisplayName
        album_wechat_ui_finder.text = albumEntity.bucketDisplayName
        albumFragment.onScanAlbum(albumEntity.parent, isFinder = true, result = false)
        finderFragment?.dismiss()
    }

    private fun format(t: Int): String {
        return when {
            t < 60000 -> (t % 60000 / 1000).toString() + "秒"
            t in 60000..3599999 -> getString(t % 3600000 / 60000) + ":" + getString(t % 60000 / 1000)
            else -> getString(t / 3600000) + ":" + getString(t % 3600000 / 60000) + ":" + getString(t % 60000 / 1000)
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