package com.album.sample.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.album.*
import com.album.sample.R
import com.album.sample.imageloader.SimpleFrescoAlbumImageLoader
import com.album.ui.AlbumUiBundle
import com.album.ui.fragment.AlbumFragment
import com.album.ui.fragment.PrevFragment
import java.io.File

/**
 * 只是简单的示例,如果digloa显示需要判断更多的逻辑,例如预览返回需要更新选中数据
 */
class SimpleDialogFragment : DialogFragment(), AlbumPreviewParentListener {

    override fun onChangedToolbarCount(currentPos: Int, maxPos: Int) {
    }

    override fun onChangedCount(currentCount: Int) {
    }

    private lateinit var mActivity: Activity
    private lateinit var albumFragment: AlbumFragment
    private var prevFragment: PrevFragment? = null
    private lateinit var rootView: View

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
            if (keyCode == KeyEvent.KEYCODE_BACK && prevFragment != null && prevFragment?.isVisible == true) {
                childFragmentManager.beginTransaction().show(albumFragment).hide(prevFragment!!).commit()
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as? Activity ?: activity!!
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mActivity, R.style.BottomDialog)
        rootView = View.inflate(activity, R.layout.album_fragment_dialog, null)
        rootView.findViewById<View>(R.id.dialog_ok).setOnClickListener { albumFragment.multipleSelect() }
        builder.setView(rootView)
        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Album
                .instance
                .apply {
                    albumImageLoader = SimpleFrescoAlbumImageLoader()
                    albumListener = object : SimpleAlbumListener() {
                        override fun onAlbumResources(list: List<AlbumEntity>) {
                            super.onAlbumResources(list)
                            Toast.makeText(mActivity, list.toString(), Toast.LENGTH_SHORT).show()
                            dismiss()
                        }

                        override fun onAlbumUCropResources(scannerFile: File) {
                            super.onAlbumUCropResources(scannerFile)
                            Toast.makeText(mActivity, scannerFile.toString(), Toast.LENGTH_SHORT).show()
                            dismiss()
                        }
                    }
                }

        val albumBundle = AlbumBundle(
                selectImageFinish = false,
                spanCount = 4,
                cropFinish = false,
                cameraCrop = true
        )

        albumFragment = AlbumFragment.newInstance(albumBundle)
        albumFragment.albumParentListener = object : AlbumParentListener {
            override fun onAlbumItemClick(multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String) {
                prevFragment = PrevFragment.newInstance(start(albumBundle, AlbumUiBundle(), multiplePreviewList, if (TextUtils.isEmpty(bucketId) && !albumBundle.hideCamera) position - 1 else position, bucketId))
                prevFragment?.albumParentListener = this@SimpleDialogFragment
                childFragmentManager.beginTransaction().hide(albumFragment).add(R.id.dialog_fragment, prevFragment!!, PrevFragment::class.java.simpleName).commit()
            }
        }
        childFragmentManager.beginTransaction().add(R.id.dialog_fragment, albumFragment, AlbumFragment::class.java.simpleName).commit()
    }

    fun start(albumBundle: AlbumBundle, uiBundle: AlbumUiBundle, multiplePreviewList: ArrayList<AlbumEntity>, position: Int, bucketId: String): Bundle {
        return Bundle().apply {
            putParcelableArrayList(TYPE_PREVIEW_KEY, multiplePreviewList)
            putInt(TYPE_PREVIEW_POSITION_KEY, position)
            putString(TYPE_PREVIEW_BUCKET_ID, bucketId)
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        }
    }
}
