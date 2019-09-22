package com.album.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.album.R
import com.album.core.scan.AlbumEntity
import com.album.ui.adapter.vh.PrevViewHolder

class PrevAdapter : RecyclerView.Adapter<PrevViewHolder>() {

    var albumList: ArrayList<AlbumEntity> = ArrayList()

    var multipleList: ArrayList<AlbumEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevViewHolder = PrevViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_item_album_prev, parent, false))

    override fun onBindViewHolder(holder: PrevViewHolder, position: Int) = holder.photo(albumList[position])

    override fun getItemCount(): Int = albumList.size

    fun addAll(newList: ArrayList<AlbumEntity>) {
        albumList.clear()
        albumList.addAll(newList)
        notifyDataSetChanged()
    }
}

