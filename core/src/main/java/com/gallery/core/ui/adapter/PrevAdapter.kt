package com.gallery.core.ui.adapter

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.R
import com.gallery.core.delegate.ScanEntity
import com.xadapter.vh.LayoutViewHolder
import com.xadapter.vh.XViewHolder

class PrevAdapter(private val displayPreview: (scanEntity: ScanEntity, container: FrameLayout) -> Unit) : RecyclerView.Adapter<XViewHolder>() {

    private val galleryList: ArrayList<ScanEntity> = ArrayList()
    private val selectList: ArrayList<ScanEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder = LayoutViewHolder(parent, R.layout.gallery_item_gallery_prev)

    override fun onBindViewHolder(holder: XViewHolder, position: Int) {
        displayPreview.invoke(galleryList[position], holder.frameLayout(R.id.galleryPrevContainer))
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
}

