package com.album.sample.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.album.Album
import com.album.AlbumEntity
import com.album.SimpleAlbumListener
import com.album.sample.R
import com.album.ui.fragment.AlbumFragment
import java.io.File

class SimpleDialogFragment : DialogFragment() {

    private lateinit var mActivity: Activity
    private lateinit var albumFragment: AlbumFragment
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
        albumFragment = AlbumFragment.newInstance()
        childFragmentManager
                .beginTransaction()
                .apply {
                    add(R.id.dialog_fragment, albumFragment, AlbumFragment::class.java.simpleName)
                }
                .commit()
        Album
                .instance
                .apply {
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
                    config.apply {
                        selectImageFinish = false
                        spanCount = 4
                        noPreview = true
                        cropFinish = false
//                        isRadio = true
//                        isCrop = true
//                        cameraCrop = true
                    }
                }
    }
}
