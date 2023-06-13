package com.gallery.sample.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.sample.R
import com.gallery.sample.databinding.SimpleLayoutGallerySelectIconItemBinding

class GallerySelectIconAdapter(private val action: (item: Pair<String, Int>) -> Unit) :
    RecyclerView.Adapter<GallerySelectIconAdapter.ViewHolder>() {

    private val items = arrayListOf<Pair<String, Int>>().apply {
        add("ic_launcher" to R.mipmap.ic_launcher)
        add("ic_default_camera_drawable" to com.gallery.core.R.drawable.ic_default_camera_drawable)
        add("ic_simple_camera_2" to R.drawable.ic_simple_camera_2)
        add("ic_material_gallery_back" to com.gallery.ui.material.R.drawable.ic_material_gallery_back)
        add("ic_material_gallery_finder" to com.gallery.ui.material.R.drawable.ic_material_gallery_finder)
        add("ic_wechat_gallery_finder_action" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_finder_action)
        add("ic_wechat_gallery_finder_check" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_finder_check)
        add("ic_wechat_gallery_item_checkbox_true" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_item_checkbox_true)
        add("ic_wechat_gallery_item_gif" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_item_gif)
        add("ic_wechat_gallery_item_video" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_item_video)
        add("ic_wechat_gallery_prev_play" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_prev_play)
        add("ic_wechat_gallery_toolbar_back" to com.gallery.ui.wechat.R.drawable.ic_wechat_gallery_toolbar_back)
        add("ic_material_gallery_finder" to com.gallery.ui.material.R.drawable.ic_material_gallery_finder)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            SimpleLayoutGallerySelectIconItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                action.invoke(items[bindingAdapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pair = items[position]
        holder.viewBinding.tv.text = pair.first
        holder.viewBinding.iv.setBackgroundResource(pair.second)
    }

    class ViewHolder(val viewBinding: SimpleLayoutGallerySelectIconItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

}