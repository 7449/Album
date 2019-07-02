package com.album.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.AlbumBundle
import com.album.AlbumConst
import com.album.callback.AlbumPreCallback
import com.album.core.hasL
import com.album.core.scan.AlbumEntity
import com.album.core.statusBarColor
import com.album.core.ui.AlbumBaseActivity
import com.album.ui.AlbumUiBundle
import com.album.ui.R
import com.album.ui.fragment.AlbumFragment
import com.album.ui.fragment.PrevFragment
import kotlinx.android.synthetic.main.album_activity_preview.*

class PreActivity : AlbumBaseActivity(), AlbumPreCallback {

    override val layoutId: Int = R.layout.album_activity_preview

    companion object {
        @JvmStatic
        fun newInstance(albumBundle: AlbumBundle,
                        uiBundle: AlbumUiBundle,
                        selectList: ArrayList<AlbumEntity>,
                        allList: ArrayList<AlbumEntity>,
                        position: Int,
                        fragment: AlbumFragment) {
            val bundle = Bundle().apply {
                putParcelableArrayList(AlbumConst.TYPE_PRE_SELECT, selectList)
                putParcelableArrayList(AlbumConst.TYPE_PRE_ALL, allList)
                putInt(AlbumConst.TYPE_PRE_POSITION, position)
                putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS, uiBundle)
            }
            fragment.startActivityForResult(Intent(fragment.activity, PreActivity::class.java).putExtras(bundle), AlbumConst.TYPE_PRE_REQUEST_CODE)
        }
    }

    private lateinit var prevFragment: PrevFragment
    private lateinit var albumBundle: AlbumBundle
    private lateinit var uiBundle: AlbumUiBundle

    override fun initView() {
        preBottomViewSelect.setOnClickListener {
            if (prevFragment.getSelectEntity().isEmpty()) {
                Album.instance.albumListener?.onAlbumContainerPreSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener?.onAlbumResources(prevFragment.getSelectEntity())
            prevFragment.isRefreshAlbumUI(isRefresh = false, isFinish = uiBundle.preSelectOkFinish)
        }
    }

    @SuppressLint("NewApi")
    override fun initCreate(savedInstanceState: Bundle?) {

        albumBundle = intent.extras?.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        uiBundle = intent.extras?.getParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS)
                ?: AlbumUiBundle()

        preBottomView.setBackgroundColor(ContextCompat.getColor(this, uiBundle.preBottomViewBackground))
        preBottomViewSelect.setText(uiBundle.preBottomOkText)
        preBottomViewSelect.textSize = uiBundle.preBottomOkTextSize
        preBottomViewSelect.setTextColor(ContextCompat.getColor(this, uiBundle.preBottomOkTextColor))
        preCount.textSize = uiBundle.preBottomCountTextSize
        preCount.setTextColor(ContextCompat.getColor(this, uiBundle.preBottomCountTextColor))
        preRootView.setBackgroundColor(ContextCompat.getColor(this, uiBundle.preBackground))
        window.statusBarColor(ContextCompat.getColor(this, uiBundle.statusBarColor))
        preToolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(uiBundle.preFinishRefresh, false) }
        preToolbar.setTitleTextColor(ContextCompat.getColor(this, uiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, uiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, uiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        preToolbar.navigationIcon = drawable
        preToolbar.setBackgroundColor(ContextCompat.getColor(this, uiBundle.toolbarBackground))
        if (hasL()) {
            preToolbar.elevation = uiBundle.toolbarElevation
        }
        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            prevFragment = PrevFragment.newInstance(
                    albumBundle,
                    intent.extras?.getInt(AlbumConst.TYPE_PRE_POSITION) ?: 0,
                    intent.extras?.getParcelableArrayList(AlbumConst.TYPE_PRE_SELECT)
                            ?: ArrayList(),
                    intent.extras?.getParcelableArrayList(AlbumConst.TYPE_PRE_ALL) ?: ArrayList()
            )
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.preFragment, prevFragment, PrevFragment::class.java.simpleName)
                    .commitAllowingStateLoss()
        }
    }

    override fun onChangedCheckBoxCount(selectCount: Int) {
        preCount.text = String.format("%s / %s", selectCount.toString(), albumBundle.multipleMaxCount)
    }

    override fun onChangedViewPager(currentPos: Int, maxPos: Int) {
        preToolbar.title = getString(uiBundle.preTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
        prevFragment.isRefreshAlbumUI(uiBundle.preBackRefresh, false)
        super.onBackPressed()
    }

}
