/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.album.sample.camera

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.cameraview.AspectRatio
import java.util.*


/**
 * A simple dialog that allows user to pick an aspect ratio.
 */
@Suppress("UNCHECKED_CAST", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SimpleAspectRatioFragment : DialogFragment() {

    private var mListener: Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as Listener?
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments
        val ratios = args!!.getParcelableArray(ARG_ASPECT_RATIOS) as Array<AspectRatio>
        Arrays.sort(ratios)
        val current = args.getParcelable<AspectRatio>(ARG_CURRENT_ASPECT_RATIO)
        val adapter = AspectRatioAdapter(ratios, current)
        return AlertDialog.Builder(activity!!)
                .setAdapter(adapter) { _, position -> mListener?.onAspectRatioSelected(ratios[position]) }
                .create()
    }

    private class AspectRatioAdapter internal constructor(private val mRatios: Array<AspectRatio>, private val mCurrentRatio: AspectRatio) : BaseAdapter() {

        override fun getCount(): Int {
            return mRatios.size
        }

        override fun getItem(position: Int): AspectRatio {
            return mRatios[position]
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).hashCode().toLong()
        }

        override fun getView(position: Int, view: View?, parent: ViewGroup): View? {
            val holder: AspectRatioAdapter.ViewHolder
            if (view == null) {
                holder = ViewHolder()
                holder.itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
                holder.text = holder.itemView.findViewById(android.R.id.text1)
                holder.itemView.tag = holder
            } else {
                holder = view.tag as ViewHolder
            }
            val ratio = getItem(position)
            val sb = StringBuilder(ratio.toString())
            if (ratio == mCurrentRatio) {
                sb.append(" *")
            }
            holder.text.text = sb
            return holder.itemView
        }

        private class ViewHolder {
            internal lateinit var text: TextView
            internal lateinit var itemView: View
        }

    }

    internal interface Listener {
        fun onAspectRatioSelected(ratio: AspectRatio)
    }

    companion object {

        private const val ARG_ASPECT_RATIOS = "aspect_ratios"
        private const val ARG_CURRENT_ASPECT_RATIO = "current_aspect_ratio"

        fun newInstance(ratios: Set<AspectRatio>, currentRatio: AspectRatio): SimpleAspectRatioFragment {
            return SimpleAspectRatioFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_ASPECT_RATIOS, ratios.toTypedArray())
                    putParcelable(ARG_CURRENT_ASPECT_RATIO, currentRatio)
                }
            }
        }
    }

}
