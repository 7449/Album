package com.album.ui.dialog

import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.album.*
import com.album.core.AlbumCore
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseDialogFragment
import com.album.listener.SimpleAlbumImageLoader
import com.album.ui.fragment.AlbumFragment
import com.album.ui.fragment.PrevFragment
import kotlinx.android.synthetic.main.album_fragment_dialog.*

class AlbumDialogFragment : AlbumBaseDialogFragment() {

    override fun viewLayout(): View = View.inflate(mActivity, R.layout.album_fragment_dialog, null)

    override val themeId: Int = R.style.BottomDialog

    private lateinit var albumUiBundle: AlbumDialogUiBundle
    private lateinit var albumBundle: AlbumBundle

    private lateinit var albumFragment: AlbumFragment
    private lateinit var prevFragment: PrevFragment

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val window = dialog.window ?: return
        val attributes = window.attributes ?: return
        attributes.width = LinearLayout.LayoutParams.MATCH_PARENT
        attributes.height = LinearLayout.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.BOTTOM
        window.attributes = attributes
    }

    override fun onResume() {
        super.onResume()
        val view = view ?: return
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK && ::prevFragment.isInitialized && !prevFragment.isHidden) {
                childFragmentManager.beginTransaction().show(albumFragment).hide(prevFragment).commitAllowingStateLoss()
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumUiBundle = bundle.getParcelable(EXTRA_ALBUM_UI_OPTIONS)
                ?: AlbumDialogUiBundle()
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
        // temp
        albumBundle.selectImageFinish = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        if (album_dialog_fragment.layoutParams is LinearLayout.LayoutParams) {
            album_dialog_fragment.layoutParams.height = (resources.displayMetrics.heightPixels / 1.5).toInt()
        }

        album_dialog_title.setTitle(albumUiBundle.toolbarText)
        album_dialog_title.setTitleTextColor(ContextCompat.getColor(mActivity, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(mActivity, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        album_dialog_title.navigationIcon = drawable
        album_dialog_title.setBackgroundColor(ContextCompat.getColor(mActivity, albumUiBundle.toolbarBackground))
        if (AlbumCore.hasL()) {
            album_dialog_title.elevation = albumUiBundle.toolbarElevation
        }
        album_dialog_title.setNavigationOnClickListener {
            val supportFragmentManager = childFragmentManager
            if (::prevFragment.isInitialized && !prevFragment.isHidden) {
                supportFragmentManager.beginTransaction().hide(prevFragment).show(albumFragment).commitAllowingStateLoss()
                return@setNavigationOnClickListener
            }
            dismiss()
        }
        album_dialog_bottom_view_ok.setOnClickListener {
            if (albumFragment.getSelectEntity().isEmpty()) {
                return@setOnClickListener
            }
            albumFragment.multipleSelect()
            dismiss()
        }
        album_dialog_bottom_view_preview.setOnClickListener { back() }

        initAlbum()
        initAlbumFragment()
    }

    private fun back() {
        if (albumFragment.getSelectEntity().isEmpty()) {
            return
        }
        val supportFragmentManager = childFragmentManager
        supportFragmentManager.beginTransaction().hide(albumFragment).commitAllowingStateLoss()
        openAlbumPrevFragment(albumBundle, albumFragment.getSelectEntity(), 0, PREVIEW_BUTTON_KEY, albumFragment)
    }

    private fun initAlbum() {
        Album.instance.apply {
            albumImageLoader = SimpleAlbumImageLoader()
        }
    }

    private fun initAlbumFragment() {
        val supportFragmentManager = childFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(AlbumFragment::class.java.simpleName)
        if (fragment != null) {
            albumFragment = fragment as AlbumFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            albumFragment = AlbumFragment.newInstance(albumBundle)
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.album_dialog_fragment, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
    }

    private fun openAlbumPrevFragment(albumBundle: AlbumBundle, multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String, fragment: Fragment) {
        val bundle = Bundle().apply {
            putParcelableArrayList(TYPE_PREVIEW_KEY, multiplePreviewList)
            putInt(TYPE_PREVIEW_POSITION_KEY, position)
            putString(TYPE_PREVIEW_BUCKET_ID, bucketId)
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
        }

        val supportFragmentManager = childFragmentManager
        val fragment = supportFragmentManager.findFragmentByTag(PrevFragment::class.java.simpleName)
        if (fragment != null) {
            prevFragment = fragment as PrevFragment
            supportFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            prevFragment = PrevFragment.newInstance(bundle)
            supportFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.album_dialog_fragment, prevFragment, PrevFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        albumFragment.disconnectMediaScanner()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Album.destroy()
    }
}
