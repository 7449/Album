package develop.file.gallery.ui.wechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gallery.ui.wechat.databinding.WechatGalleryItemFinderBinding
import develop.file.gallery.compat.finder.GalleryFinderAdapter
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.ui.wechat.color76
import develop.file.gallery.ui.wechat.colorWhite

internal class WeChatFinderAdapter(
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
        holder.bind(entity)
    }

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    fun refreshFinder(parent: Long) {
        list.forEach { it.isSelected = it.parent == parent }
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: WechatGalleryItemFinderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: ScanEntity) {
            binding.tvGalleryFinderName.text = "%s".format(entity.bucketDisplayName)
            binding.tvGalleryFinderName.setTextColor(colorWhite)
            binding.tvGalleryFinderFileCount.text = "(%s)".format(entity.count.toString())
            binding.tvGalleryFinderFileCount.setTextColor(color76)
            binding.ivGalleryFinderFileCheck.visibility =
                if (entity.isSelected) View.VISIBLE else View.GONE
        }

    }

}
