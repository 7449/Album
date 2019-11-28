package com.gallery.core.ui.adapter.vh

import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.Album
import com.gallery.core.R
import com.gallery.core.ext.addChildView
import com.gallery.scan.ScanEntity

class PrevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val container: FrameLayout = itemView.findViewById(R.id.galleryPrevContainer)

    fun photo(albumEntity: ScanEntity) {
        container.addChildView(Album.instance.albumImageLoader?.displayAlbumPreview(albumEntity, container))
    }
}