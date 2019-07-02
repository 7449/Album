package com.album.ui.dialog

import android.annotation.SuppressLint
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
import com.album.Album
import com.album.AlbumBundle
import com.album.AlbumConst
import com.album.callback.AlbumCallback
import com.album.core.*
import com.album.core.scan.AlbumEntity
import com.album.core.ui.AlbumBaseDialogFragment
import com.album.ui.fragment.AlbumFragment
import com.album.ui.fragment.PrevFragment
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropFragment
import com.yalantis.ucrop.UCropFragmentCallback
import kotlinx.android.synthetic.main.album_fragment_dialog.*
import java.io.File

class AlbumDialogFragment : AlbumBaseDialogFragment(), AlbumCallback {

    companion object {
        @JvmStatic
        fun newInstance(albumBundle: AlbumBundle, uiBundle: AlbumDialogUiBundle): AlbumDialogFragment {
            val albumDialogFragment = AlbumDialogFragment()
            albumDialogFragment.arguments = Bundle().apply {
                putParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS, albumBundle)
                putParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS, uiBundle)
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
        albumUiBundle = bundle.getParcelable(AlbumConst.EXTRA_ALBUM_UI_OPTIONS)
                ?: AlbumDialogUiBundle()
        albumBundle = bundle.getParcelable(AlbumConst.EXTRA_ALBUM_OPTIONS) ?: AlbumBundle()
    }

    @SuppressLint("NewApi")
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
                Album.instance.albumListener?.onAlbumPreEmpty()
                return@setOnClickListener
            }
            if (hasShowPrevFragment()) {
                Toast.makeText(mActivity, "正在预览阶段", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            initPrevFragment(albumFragment.getSelectEntity(), 0)
        }

        album_dialog_bottom_view_crop.setOnClickListener { cropFragment?.cropAndSaveImage() }

        initAlbumFragment()
    }

    override fun onAlbumItemClick(selectEntity: ArrayList<AlbumEntity>, position: Int, parentId: Long) {
        initPrevFragment(selectEntity, if (parentId == AlbumScanConst.ALL && !albumBundle.hideCamera) position - 1 else position)
    }

    override fun onChangedCheckBoxCount(view: View, selectCount: Int, albumEntity: AlbumEntity) {
    }

    override fun onAlbumScreenChanged(selectCount: Int) {
    }

    override fun onPrevChangedCount(selectCount: Int) {
    }

    override fun onAlbumCustomCrop(path: String): Boolean {
        openUCrop(path)
        return true
    }

    private fun hasShowPrevFragment() = ::prevFragment.isInitialized && prevFragment.isVisible

    private fun initPrevFragment(multiplePreviewList: ArrayList<AlbumEntity>, position: Int) {
        if (::prevFragment.isInitialized && !prevFragment.isRemoving) {
            childFragmentManager.beginTransaction().remove(prevFragment).commitAllowingStateLoss()
        }
        prevFragment = PrevFragment.newInstance(albumBundle, position, multiplePreviewList, albumFragment.allPreview())
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
    }

    private fun openUCrop(path: String) {
        cropFragment?.let { childFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss() }
        cropFragment = UCrop.of(Uri.fromFile(File(path)), Uri.fromFile(mActivity.cameraFile(albumBundle.uCropPath, System.currentTimeMillis().toString(), ".jpg")))
                .withOptions(Album.instance.options ?: UCrop.Options()).fragment
        cropFragment?.let { it ->
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
                                cropPath.toFile()?.let { Album.instance.albumListener?.onAlbumUCropResources(it) }
                                albumFragment.refreshMedia(AlbumConst.TYPE_RESULT_CROP, cropPath)
                            }
                        }
                    }

                    override fun loadingProgress(showLoader: Boolean) {
                    }
                })
            }, 500)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        albumFragment.disconnectMediaScanner()
        super.onDismiss(dialog)
        Album.instance.albumListener?.onAlbumContainerFinish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Album.destroy()
    }
}
