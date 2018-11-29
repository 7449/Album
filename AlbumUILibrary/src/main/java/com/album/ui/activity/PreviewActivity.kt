package com.album.ui.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.album.*
import com.album.ui.AlbumUiBundle
import com.album.ui.R
import com.album.ui.fragment.PrevFragment
import com.album.util.checkNotBundleNull
import com.album.util.hasL
import com.album.util.setStatusBarColor

/**
 * @author y
 */
class PreviewActivity : AlbumBaseActivity(), AlbumPreviewParentListener {

    companion object {
        fun start(albumBundle: AlbumBundle, uiBundle: AlbumUiBundle, multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String, fragment: Fragment) {
            val bundle = Bundle().apply {
                putParcelableArrayList(AlbumConstant.PREVIEW_KEY, multiplePreviewList)
                putInt(AlbumConstant.PREVIEW_POSITION_KEY, position)
                putString(AlbumConstant.PREVIEW_BUCKET_ID, bucketId)
                putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
            }
            fragment.startActivityForResult(Intent(fragment.activity, PreviewActivity::class.java).putExtras(bundle), AlbumConstant.TYPE_PREVIEW_CODE)
        }
    }

    private lateinit var toolbar: Toolbar
    private lateinit var previewCount: AppCompatTextView
    private lateinit var prevFragment: PrevFragment
    private lateinit var rootView: LinearLayout
    private lateinit var previewBottomView: RelativeLayout
    private lateinit var previewOk: AppCompatTextView

    private lateinit var albumBundle: AlbumBundle
    private lateinit var uiBundle: AlbumUiBundle

    override fun initView() {
        toolbar = findViewById(R.id.preview_toolbar)
        previewCount = findViewById(R.id.preview_tv_preview_count)
        rootView = findViewById(R.id.preview_root_view)
        previewBottomView = findViewById(R.id.preview_bottom_view)
        previewOk = findViewById(R.id.preview_bottom_view_tv_select)
        previewOk.setOnClickListener {
            val entity = prevFragment.getSelectEntity()
            if (entity.isEmpty()) {
                Album.instance.albumListener.onAlbumPreviewSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener.onAlbumResources(entity)
            prevFragment.isRefreshAlbumUI(false, true)
        }
    }

    override fun initCreate(savedInstanceState: Bundle?) {
        albumBundle = intent.extras?.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        uiBundle = intent.extras?.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumUiBundle()

        previewBottomView.setBackgroundColor(ContextCompat.getColor(this, uiBundle.previewBottomViewBackground))
        previewOk.setText(uiBundle.previewBottomOkText)
        previewOk.textSize = uiBundle.previewBottomOkTextSize
        previewOk.setTextColor(ContextCompat.getColor(this, uiBundle.previewBottomOkTextColor))
        previewCount.textSize = uiBundle.previewBottomCountTextSize
        previewCount.setTextColor(ContextCompat.getColor(this, uiBundle.previewBottomCountTextColor))
        rootView.setBackgroundColor(ContextCompat.getColor(this, uiBundle.previewBackground))
        setStatusBarColor(ContextCompat.getColor(this, uiBundle.statusBarColor), window)
        toolbar.setNavigationOnClickListener { prevFragment.isRefreshAlbumUI(uiBundle.previewFinishRefresh, false) }
        toolbar.setTitleTextColor(ContextCompat.getColor(this, uiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(this, uiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(this, uiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        toolbar.navigationIcon = drawable
        toolbar.setBackgroundColor(ContextCompat.getColor(this, uiBundle.toolbarBackground))
        if (hasL()) {
            toolbar.elevation = uiBundle.toolbarElevation
        }

        val supportFragmentManager = supportFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commit()
        } else {
            prevFragment = PrevFragment.newInstance(checkNotBundleNull(intent.extras))
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.preview_fragment, prevFragment, PrevFragment::class.java.simpleName) }
                    .commit()
        }
        prevFragment.albumParentListener = this
    }

    override fun getLayoutId(): Int = R.layout.album_activity_preview

    override fun onChangedCount(currentCount: Int) {
        previewCount.text = String.format("%s / %s", currentCount.toString(), albumBundle.multipleMaxCount)
    }

    override fun onChangedToolbarCount(currentPos: Int, maxPos: Int) {
        toolbar.title = getString(uiBundle.previewTitle) + "(" + currentPos + "/" + maxPos + ")"
    }

    override fun onBackPressed() {
        prevFragment.isRefreshAlbumUI(uiBundle.previewBackRefresh, false)
        super.onBackPressed()
    }

}
