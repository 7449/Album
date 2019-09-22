package com.album.ui.adapter.vh

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.album.Album
import com.album.R
import com.album.core.addChildView
import com.album.core.scan.AlbumEntity

class PrevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val container: FrameLayout = itemView.findViewById(R.id.albumPrevContainer)

    fun photo(albumEntity: AlbumEntity) {
        container.addChildView(Album.instance.albumImageLoader?.displayAlbumPreview(albumEntity, container))
    }
}