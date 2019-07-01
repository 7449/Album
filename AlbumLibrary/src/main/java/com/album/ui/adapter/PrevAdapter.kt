package com.album.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.album.Album
import com.album.R
import com.album.core.scan.AlbumEntity
import com.album.listener.addChildView

class PrevAdapter : RecyclerView.Adapter<PrevAdapter.PhotoViewHolder>() {

    var albumList: ArrayList<AlbumEntity> = ArrayList()

    var multipleList: ArrayList<AlbumEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder = PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.album_item_album_prev, parent, false))

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) = holder.photo(albumList[position])

    override fun getItemCount(): Int = albumList.size

    fun addAll(newList: ArrayList<AlbumEntity>) {
        albumList.clear()
        albumList.addAll(newList)
        notifyDataSetChanged()
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val container: FrameLayout = itemView.findViewById(R.id.albumPrevContainer)

        fun photo(albumEntity: AlbumEntity) {
            container.addChildView(Album.instance.albumImageLoader?.displayAlbumPreview(albumEntity, container))
        }
    }
}

