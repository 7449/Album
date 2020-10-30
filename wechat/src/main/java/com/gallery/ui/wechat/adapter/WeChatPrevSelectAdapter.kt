package com.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gallery_item_prev_select_wechat.*

class WeChatPrevSelectAdapter(
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<WeChatPrevSelectAdapter.ViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_prev_select_wechat, parent, false))
        layoutViewHolder.itemView.setOnClickListener {
            adapterFinderListener.onGalleryAdapterItemClick(it, layoutViewHolder.bindingAdapterPosition, list[layoutViewHolder.bindingAdapterPosition])
        }
        return layoutViewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val finderEntity: ScanEntity = list[position]
        val frameLayout: FrameLayout = holder.prevSelectFrame
        frameLayout.setBackgroundResource(if (finderEntity.isSelected) R.drawable.wechat_selector_gallery_select else 0)
        adapterFinderListener.onGalleryFinderThumbnails(finderEntity, frameLayout)
    }

    fun updateSelect(entities: List<ScanEntity>) {
        list.clear()
        list.addAll(entities.map { it.copy() })
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

    @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View
            get() = itemView
    }

}