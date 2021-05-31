package com.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.args.GalleryWeChatBundle
import com.gallery.ui.wechat.databinding.GalleryWechatItemFinderBinding

class WeChatFinderAdapter(
    private val weChatBundle: GalleryWeChatBundle,
    private val listener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<WeChatFinderAdapter.ViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GalleryWechatItemFinderBinding.inflate(
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
        holder.bind(weChatBundle, entity)
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: GalleryWechatItemFinderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uiBundle: GalleryWeChatBundle, entity: ScanEntity) {
            binding.tvGalleryFinderName.text = "%s".format(entity.bucketDisplayName)
            binding.tvGalleryFinderName.setTextColor(uiBundle.finderItemTextColor)
            binding.tvGalleryFinderFileCount.text = "(%s)".format(entity.count.toString())
            binding.tvGalleryFinderFileCount.setTextColor(uiBundle.finderItemTextCountColor)
            binding.ivGalleryFinderFileCheck.visibility =
                if (entity.isSelected) View.VISIBLE else View.GONE
        }

    }

}
