package com.gallery.sample.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.sample.R

class GallerySelectIconDialog(private val action: (item: Pair<String, Int>) -> Unit) :
    DialogFragment(R.layout.simple_layout_gallery_select_icon_dialog) {

    companion object {
        fun show(activity: Context, action: (item: Pair<String, Int>) -> Unit) {
            activity as FragmentActivity
            GallerySelectIconDialog(action).show(activity.supportFragmentManager, "tag")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = GallerySelectIconAdapter {
            action.invoke(it)
            dismissAllowingStateLoss()
        }
    }

}