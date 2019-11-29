package com.gallery.core.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.Gallery
import com.gallery.core.R
import com.gallery.scan.ScanEntity

class PrevAdapter : RecyclerView.Adapter<PrevAdapter.PrevViewHolder>() {

    var galleryList: ArrayList<ScanEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevViewHolder = PrevViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery_prev, parent, false))

    override fun onBindViewHolder(holder: PrevViewHolder, position: Int) = holder.photo(galleryList[position])

    override fun getItemCount(): Int = galleryList.size

    fun addAll(newList: ArrayList<ScanEntity>) {
        galleryList.clear()
        galleryList.addAll(newList)
        notifyDataSetChanged()
    }

    class PrevViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val container: FrameLayout = itemView.findViewById(R.id.galleryPrevContainer)

        fun photo(galleryEntity: ScanEntity) {
            Gallery.instance.galleryImageLoader?.displayGalleryPreview(galleryEntity, container)
        }
    }
}

