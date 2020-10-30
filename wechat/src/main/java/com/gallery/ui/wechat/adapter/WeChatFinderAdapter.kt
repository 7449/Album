package com.gallery.ui.wechat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.GalleryUiBundle
import com.gallery.core.entity.ScanEntity
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.ui.wechat.R
import com.xadapter.vh.LayoutViewHolder
import com.xadapter.vh.XViewHolder

class WeChatFinderAdapter(
        private val galleryUiBundle: GalleryUiBundle,
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<XViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder {
        val layoutViewHolder = LayoutViewHolder(parent, R.layout.gallery_item_finder_wechat)
        layoutViewHolder.itemView.setOnClickListener {
            adapterFinderListener.onGalleryAdapterItemClick(it, layoutViewHolder.bindingAdapterPosition, list[layoutViewHolder.bindingAdapterPosition])
        }
        return layoutViewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: XViewHolder, position: Int) {
        val finderEntity: ScanEntity = list[position]
        adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.frameLayout(R.id.iv_gallery_finder_icon))
        holder
                .setText(R.id.tv_gallery_finder_name, "%s".format(finderEntity.bucketDisplayName))
                .setTextColor(R.id.tv_gallery_finder_name, galleryUiBundle.finderItemTextColor)
                .setText(R.id.tv_gallery_finder_file_count, "(%s)".format(finderEntity.count.toString()))
                .setTextColor(R.id.tv_gallery_finder_file_count, galleryUiBundle.finderItemTextCountColor)
                .setVisibility(R.id.iv_gallery_finder_file_check, finderEntity.isSelected)
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }
}
