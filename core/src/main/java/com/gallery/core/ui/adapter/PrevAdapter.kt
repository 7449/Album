package com.gallery.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.R
import com.gallery.core.ui.adapter.vh.PrevViewHolder
import com.gallery.scan.ScanEntity

class PrevAdapter : RecyclerView.Adapter<PrevViewHolder>() {

    var albumList: ArrayList<ScanEntity> = ArrayList()

    var multipleList: ArrayList<ScanEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevViewHolder = PrevViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_item_album_prev, parent, false))

    override fun onBindViewHolder(holder: PrevViewHolder, position: Int) = holder.photo(albumList[position])

    override fun getItemCount(): Int = albumList.size

    fun addAll(newList: ArrayList<ScanEntity>) {
        albumList.clear()
        albumList.addAll(newList)
        notifyDataSetChanged()
    }
}

