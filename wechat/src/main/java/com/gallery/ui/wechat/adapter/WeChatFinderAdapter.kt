package com.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gallery_item_finder_wechat.*

class WeChatFinderAdapter(
        private val galleryUiBundle: GalleryUiBundle,
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener,
) : RecyclerView.Adapter<WeChatFinderAdapter.ViewHolder>() {

    private val list: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_finder_wechat, parent, false))
        layoutViewHolder.itemView.setOnClickListener {
            adapterFinderListener.onGalleryAdapterItemClick(it, layoutViewHolder.bindingAdapterPosition, list[layoutViewHolder.bindingAdapterPosition])
        }
        return layoutViewHolder
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val finderEntity: ScanEntity = list[position]
        adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.ivGalleryFinderIcon)
        holder.bind(galleryUiBundle, finderEntity)
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View
            get() = itemView

        fun bind(galleryUiBundle: GalleryUiBundle, finderEntity: ScanEntity) {
            tvGalleryFinderName.text = "%s".format(finderEntity.bucketDisplayName)
            tvGalleryFinderName.setTextColor(galleryUiBundle.finderItemTextColor)
            tvGalleryFinderFileCount.text = "(%s)".format(finderEntity.count.toString())
            tvGalleryFinderFileCount.setTextColor(galleryUiBundle.finderItemTextCountColor)
            ivGalleryFinderFileCheck.visibility = if (finderEntity.isSelected) View.VISIBLE else View.GONE
        }
    }

}
