package com.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import com.gallery.ui.wechat.databinding.WechatGalleryItemPrevSelectBinding

class WeChatPrevSelectAdapter(
    private val listener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<WeChatPrevSelectAdapter.ViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WechatGalleryItemPrevSelectBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                listener.onGalleryAdapterItemClick(
                    it,
                    bindingAdapterPosition,
                    list[bindingAdapterPosition]
                )
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity: ScanEntity = list[position]
        val frameLayout: FrameLayout = holder.binding.prevSelectFrame
        frameLayout.setBackgroundResource(if (entity.isSelected) R.drawable.wechat_gallery_selector_gallery_select else 0)
        listener.onGalleryFinderThumbnails(entity, frameLayout)
    }

    fun updateSelect(entities: List<ScanEntity>) {
        list.clear()
        list.addAll(entities.map { it.copy() })
        notifyDataSetChanged()
    }

    fun index(scanEntity: ScanEntity): Int {
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

    class ViewHolder(val binding: WechatGalleryItemPrevSelectBinding) :
        RecyclerView.ViewHolder(binding.root)

}