package com.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.databinding.GalleryItemFinderWechatBinding

class WeChatFinderAdapter(
        private val galleryUiBundle: GalleryUiBundle,
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<WeChatFinderAdapter.ViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutViewHolder = ViewHolder(GalleryItemFinderWechatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        layoutViewHolder.itemView.setOnClickListener {
            adapterFinderListener.onGalleryAdapterItemClick(it, layoutViewHolder.bindingAdapterPosition, list[layoutViewHolder.bindingAdapterPosition])
        }
        return layoutViewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val finderEntity: ScanEntity = list[position]
        adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.viewBinding.ivGalleryFinderIcon)
        holder.bind(galleryUiBundle, finderEntity)
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    class ViewHolder(val viewBinding: GalleryItemFinderWechatBinding) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(galleryUiBundle: GalleryUiBundle, finderEntity: ScanEntity) {
            viewBinding.tvGalleryFinderName.text = "%s".format(finderEntity.bucketDisplayName)
            viewBinding.tvGalleryFinderName.setTextColor(galleryUiBundle.finderItemTextColor)
            viewBinding.tvGalleryFinderFileCount.text = "(%s)".format(finderEntity.count.toString())
            viewBinding.tvGalleryFinderFileCount.setTextColor(galleryUiBundle.finderItemTextCountColor)
            viewBinding.ivGalleryFinderFileCheck.visibility = if (finderEntity.isSelected) View.VISIBLE else View.GONE
        }
    }

}
