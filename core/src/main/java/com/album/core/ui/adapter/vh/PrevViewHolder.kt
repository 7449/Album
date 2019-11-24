package com.album.core.ui.adapter.vh

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.album.core.Album
import com.album.core.R
import com.album.scan.addChildView
import com.album.scan.scan.AlbumEntity

class PrevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val container: FrameLayout = itemView.findViewById(R.id.albumPrevContainer)

    fun photo(albumEntity: AlbumEntity) {
        container.addChildView(Album.instance.albumImageLoader?.displayAlbumPreview(albumEntity, container))
    }
}