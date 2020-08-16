package com.gallery.ui.wechat.adapter

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.ScanEntity
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.R
import com.xadapter.vh.LayoutViewHolder
import com.xadapter.vh.XViewHolder

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
        val frameLayout: FrameLayout = holder.frameLayout(R.id.prevSelectFrame)
        frameLayout.setBackgroundResource(if (finderEntity.isSelected) R.drawable.wechat_selector_gallery_select else 0)
        adapterFinderListener.onGalleryFinderThumbnails(finderEntity, frameLayout)
    }

    fun updateSelect(entities: List<ScanEntity>) {
        list.clear()
        entities.forEach { list.add(it.copy(isSelected = false)) }
        notifyDataSetChanged()
    }

    fun findPosition(scanEntity: ScanEntity): Int {
        return list.indexOfFirst { it.id == scanEntity.id }
    }

    fun addSelect(entity: ScanEntity) {
        list.forEach { it.isSelected = false }
        list.find { it.id == entity.id }?.let {
            it.isSelected = true
        } ?: list.add(entity.copy(isSelected = true))
        notifyDataSetChanged()
    }

    fun refreshItem(scanEntity: ScanEntity) {
        list.forEach { it.isSelected = it.id == scanEntity.id }
        notifyDataSetChanged()
    }
}