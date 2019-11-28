package com.gallery.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.R
import com.gallery.core.ui.adapter.vh.PrevViewHolder
import com.gallery.scan.ScanEntity

class PrevAdapter : RecyclerView.Adapter<PrevViewHolder>() {

    var galleryList: ArrayList<ScanEntity> = ArrayList()

    var multipleList: ArrayList<ScanEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevViewHolder = PrevViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery_prev, parent, false))

    override fun onBindViewHolder(holder: PrevViewHolder, position: Int) = holder.photo(galleryList[position])

    override fun getItemCount(): Int = galleryList.size

    fun addAll(newList: ArrayList<ScanEntity>) {
        galleryList.clear()
        galleryList.addAll(newList)
        notifyDataSetChanged()
    }
}

