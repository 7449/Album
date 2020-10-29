package com.gallery.core.delegate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.R
import com.gallery.core.entity.ScanEntity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gallery_item_gallery_prev.*

class PrevAdapter(private val displayPreview: (scanEntity: ScanEntity, container: FrameLayout) -> Unit) : RecyclerView.Adapter<PrevAdapter.ViewHolder>() {

    private val galleryList: ArrayList<ScanEntity> = arrayListOf()
    private val selectList: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_gallery_prev, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        displayPreview.invoke(galleryList[position], holder.galleryPrevContainer)
    }

    override fun getItemCount(): Int = galleryList.size

    fun addAll(newList: ArrayList<ScanEntity>) {
        galleryList.clear()
        galleryList.addAll(newList)
    }

    fun addSelectAll(newList: ArrayList<ScanEntity>) {
        selectList.clear()
        selectList.addAll(newList)
    }

    fun updateEntity() {
        galleryList.forEach { it.isSelected = false }
        selectList.forEach { select -> galleryList.find { it.id == select.id }?.isSelected = true }
        notifyDataSetChanged()
    }

    fun isCheck(position: Int) = galleryList[position].isSelected

    fun containsSelect(selectEntity: ScanEntity) = selectList.contains(selectEntity)

    fun removeSelectEntity(removeEntity: ScanEntity) = selectList.remove(removeEntity)

    fun addSelectEntity(addEntity: ScanEntity) = selectList.add(addEntity)

    val currentSelectList: ArrayList<ScanEntity>
        get() = selectList

    val allItem: ArrayList<ScanEntity>
        get() = galleryList

    @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View
            get() = itemView
    }
}

