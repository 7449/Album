package com.album.core.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.album.core.AlbumCore.orEmpty

/**
 * @author y
 * @create 2019/2/27
 */
abstract class AlbumBaseDialogFragment : DialogFragment() {

    lateinit var bundle: Bundle
    lateinit var mActivity: FragmentActivity
    lateinit var rootView: View

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as? FragmentActivity ?: activity!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundle = arguments.orEmpty()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mActivity, themeId)
        rootView = viewLayout()
        builder.setView(rootView)
        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = rootView

    protected abstract val themeId: Int
    protected abstract fun viewLayout(): View

}
