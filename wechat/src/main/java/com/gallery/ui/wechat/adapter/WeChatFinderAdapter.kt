package com.gallery.ui.wechat.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.adapter.GalleryFinderAdapter
import com.gallery.ui.wechat.R
import com.xadapter.vh.*

class WeChatFinderAdapter(private val galleryUiBundle: GalleryUiBundle,
                          private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener,
                          private val weChatAdapterListener: WeChatAdapterListener
) : RecyclerView.Adapter<XViewHolder>() {

    interface WeChatAdapterListener {
        val currentFinderId: Long
    }

    private val list: ArrayList<ScanEntity> = ArrayList()

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
        holder.setText(R.id.tv_gallery_finder_name, "%s".format(finderEntity.bucketDisplayName))
        holder.textView(R.id.tv_gallery_finder_name).setTextColor(galleryUiBundle.finderItemTextColor)
        holder.setText(R.id.tv_gallery_finder_file_count, "(%s)".format(finderEntity.count.toString()))
        holder.textView(R.id.tv_gallery_finder_file_count).setTextColor(galleryUiBundle.finderItemTextCountColor)
        holder.setGone(R.id.iv_gallery_finder_file_check, finderEntity.parent != weChatAdapterListener.currentFinderId)
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
