package com.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.args.WeChatGalleryBundle
import com.gallery.ui.wechat.databinding.WechatGalleryItemFinderBinding

class WeChatFinderAdapter(
    private val weChatGalleryBundle: WeChatGalleryBundle,
    private val listener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<WeChatFinderAdapter.ViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WechatGalleryItemFinderBinding.inflate(
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
        listener.onGalleryFinderThumbnails(
            entity,
            holder.binding.ivGalleryFinderIcon
        )
        holder.bind(weChatGalleryBundle, entity)
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: WechatGalleryItemFinderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiGalleryBundle: WeChatGalleryBundle, entity: ScanEntity) {
            binding.tvGalleryFinderName.text = "%s".format(entity.bucketDisplayName)
            binding.tvGalleryFinderName.setTextColor(uiGalleryBundle.finderItemTextColor)
            binding.tvGalleryFinderFileCount.text = "(%s)".format(entity.count.toString())
            binding.tvGalleryFinderFileCount.setTextColor(uiGalleryBundle.finderItemTextCountColor)
            binding.ivGalleryFinderFileCheck.visibility =
                if (entity.isSelected) View.VISIBLE else View.GONE
        }

    }

}
