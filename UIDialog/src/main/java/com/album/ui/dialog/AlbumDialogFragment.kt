package com.album.ui.dialog

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.album.*
import com.album.core.getCameraFile
import com.album.core.hasL
import com.album.core.orEmpty
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.toFile
import com.album.core.ui.AlbumBaseDialogFragment
import com.album.listener.AlbumParentListener
import com.album.ui.fragment.AlbumFragment
import com.album.ui.fragment.PrevFragment
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import kotlinx.android.synthetic.main.album_fragment_dialog.*
import java.io.File

class AlbumDialogFragment : AlbumBaseDialogFragment(), AlbumParentListener {

    companion object {
        fun newInstance(albumBundle: AlbumBundle, uiBundle: AlbumDialogUiBundle): AlbumDialogFragment {
            val albumDialogFragment = AlbumDialogFragment()
            albumDialogFragment.arguments = Bundle().apply {
                putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
            }
            return albumDialogFragment
        }
    }

    override fun viewLayout(): View = View.inflate(mActivity, R.layout.album_fragment_dialog, null)

    override val themeId: Int = R.style.BottomDialog

    private lateinit var albumUiBundle: AlbumDialogUiBundle
    private lateinit var albumBundle: AlbumBundle

    private lateinit var albumFragment: AlbumFragment
    private lateinit var prevFragment: PrevFragment
    private var cropFragment: UCropFragment? = null

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
            if (keyCode == KeyEvent.KEYCODE_BACK && (hasShowPrevFragment() || cropFragment != null)) {
                cropFragment?.let {
                    childFragmentManager.beginTransaction().show(albumFragment).remove(it).commitAllowingStateLoss()
                    return@setOnKeyListener true
                }
                albumFragment.onDialogResultPreview(prevFragment.isDialogRefreshAlbumUI(albumUiBundle.previewBackRefresh))
                childFragmentManager.beginTransaction().show(albumFragment).remove(prevFragment).commitAllowingStateLoss()
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        albumUiBundle = bundle.getParcelable(EXTRA_ALBUM_UI_OPTIONS) ?: AlbumDialogUiBundle()
        albumBundle = bundle.getParcelable(EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        album_dialog_fragment.layoutParams.height = resources.displayMetrics.heightPixels / 2

        album_dialog_title.setTitle(albumUiBundle.toolbarText)
        album_dialog_title.setTitleTextColor(ContextCompat.getColor(mActivity, albumUiBundle.toolbarTextColor))
        val drawable = ContextCompat.getDrawable(mActivity, albumUiBundle.toolbarIcon)
        drawable?.setColorFilter(ContextCompat.getColor(mActivity, albumUiBundle.toolbarIconColor), PorterDuff.Mode.SRC_ATOP)
        album_dialog_title.navigationIcon = drawable
        album_dialog_title.setBackgroundColor(ContextCompat.getColor(mActivity, albumUiBundle.toolbarBackground))
        if (hasL()) {
            album_dialog_title.elevation = albumUiBundle.toolbarElevation
        }

        album_dialog_title.setNavigationOnClickListener {
            if (albumFragment.isVisible) {
                dismiss()
                return@setNavigationOnClickListener
            }
            albumFragment.onDialogResultPreview(prevFragment.isDialogRefreshAlbumUI(albumUiBundle.previewFinishRefresh))
            childFragmentManager.beginTransaction().show(albumFragment).remove(prevFragment).commitAllowingStateLoss()
        }

        album_dialog_bottom_view_ok.setOnClickListener {
            if (hasShowPrevFragment()) {
                Toast.makeText(mActivity, "目前在预览阶段", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (albumFragment.getSelectEntity().isEmpty()) {
                Album.instance.albumListener?.onAlbumSelectEmpty()
                return@setOnClickListener
            }
            Album.instance.albumListener?.onAlbumResources(albumFragment.getSelectEntity())
            dismiss()
        }

        album_dialog_bottom_view_preview.setOnClickListener {
            if (albumFragment.getSelectEntity().isEmpty()) {
                Album.instance.albumListener?.onAlbumPreviewEmpty()
                return@setOnClickListener
            }
            if (hasShowPrevFragment()) {
                Toast.makeText(mActivity, "正在预览阶段", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            initPrevFragment(albumFragment.getSelectEntity(), 0, AlbumScan.PREV_PARENT)
        }

        album_dialog_bottom_view_crop.setOnClickListener { cropFragment?.cropAndSaveImage() }

        initAlbumFragment()
    }

    override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long) {
        initPrevFragment(multiplePreviewList, if (parent == AlbumScan.ALL_PARENT && !albumBundle.hideCamera) position - 1 else position, parent)
    }

    override fun onChangedCheckBoxCount(view: View, currentMaxCount: Int, albumEntity: AlbumEntity) {
    }

    override fun onAlbumScreenChanged(currentMaxCount: Int) {
    }

    override fun onPrevChangedCount(currentMaxCount: Int) {
    }

    override fun onAlbumCustomCrop(path: String): Boolean {
        openUCrop(path)
        return true
    }

    private fun hasShowPrevFragment() = ::prevFragment.isInitialized && prevFragment.isVisible

    private fun initPrevFragment(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, parent: Long) {
        val bundle = Bundle().apply {
            putParcelableArrayList(TYPE_PREVIEW_KEY, multiplePreviewList)
            putInt(TYPE_PREVIEW_POSITION_KEY, position)
            putLong(TYPE_PREVIEW_PARENT, parent)
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
        }
        if (::prevFragment.isInitialized && !prevFragment.isRemoving) {
            childFragmentManager.beginTransaction().remove(prevFragment).commitAllowingStateLoss()
        }
        prevFragment = PrevFragment.newInstance(bundle)
        childFragmentManager.beginTransaction().apply { add(R.id.album_dialog_fragment, prevFragment, PrevFragment::class.java.simpleName) }.hide(albumFragment).commitAllowingStateLoss()
    }

    private fun initAlbumFragment() {
        val fragment = childFragmentManager.findFragmentByTag(AlbumFragment::class.java.simpleName)
        if (fragment != null) {
            albumFragment = fragment as AlbumFragment
            childFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss()
        } else {
            albumFragment = AlbumFragment.newInstance(albumBundle)
            childFragmentManager
                    .beginTransaction()
                    .apply { add(R.id.album_dialog_fragment, albumFragment, AlbumFragment::class.java.simpleName) }
                    .commitAllowingStateLoss()
        }
        albumFragment.albumParentListener = this
    }

    private fun openUCrop(path: String) {
        cropFragment?.let { childFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss() }
        cropFragment = UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(mActivity.getCameraFile(albumBundle.uCropPath, albumBundle.scanType == AlbumScan.VIDEO)))
                .withOptions(Album.instance.options ?: UCrop.Options()).fragment
        cropFragment?.let {
            childFragmentManager.beginTransaction().hide(albumFragment).add(R.id.album_dialog_fragment, it, UCropFragment.TAG).commitAllowingStateLoss()
            Handler().postDelayed({
                it.setCallback(object : UCropFragmentCallback {

                    override fun onCropFinish(result: UCropFragment.UCropResult) {
                        childFragmentManager.beginTransaction().show(albumFragment).remove(it).commitAllowingStateLoss()
                        cropFragment = null
                        when (result.mResultCode) {
                            UCrop.RESULT_ERROR -> Album.instance.albumListener?.onAlbumUCropError(UCrop.getError(result.mResultData.orEmpty()))
                            RESULT_OK -> {
                                val cropPath = result.mResultData.extras?.getParcelable<Uri>(UCrop.EXTRA_OUTPUT_URI)?.path.orEmpty()
                                Album.instance.albumListener?.onAlbumUCropResources(cropPath.toFile())
                                albumFragment.refreshMedia(TYPE_RESULT_CROP, cropPath)
                            }
                        }
                    }

                    override fun loadingProgress(showLoader: Boolean) {
                    }
                })
            }, 500)
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        albumFragment.disconnectMediaScanner()
        super.onDismiss(dialog)
        Album.instance.albumListener?.onAlbumContainerFinish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Album.instance.destroy()
    }
}
