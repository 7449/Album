package com.gallery.ui.wechat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.ScanEntity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.R
import com.xadapter.vh.LayoutViewHolder
import com.xadapter.vh.XViewHolder
import com.xadapter.vh.frameLayout

class WeChatPrevSelectAdapter(
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener
) : RecyclerView.Adapter<XViewHolder>() {

    private val list: ArrayList<ScanEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder {
        val layoutViewHolder = LayoutViewHolder(parent, R.layout.gallery_item_prev_select_wechat)
        layoutViewHolder.itemView.setOnClickListener {
            adapterFinderListener.onGalleryAdapterItemClick(it, layoutViewHolder.bindingAdapterPosition, list[layoutViewHolder.bindingAdapterPosition])
        }
        return layoutViewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: XViewHolder, position: Int) {
        val finderEntity: ScanEntity = list[position]
        adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.frameLayout(R.id.prevSelectFrame))
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    fun findItemIndex(entity: ScanEntity): Int {
        return list.indexOf(entity)
    }

}