package com.gallery.core.ui.adapter

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.R
import com.gallery.scan.args.file.ScanFileEntity
import com.xadapter.vh.LayoutViewHolder
import com.xadapter.vh.XViewHolder

class PrevAdapter(private val displayPreview: (scanEntity: ScanFileEntity, container: FrameLayout) -> Unit) : RecyclerView.Adapter<XViewHolder>() {

    private val galleryList: ArrayList<ScanFileEntity> = ArrayList()
    private val selectList: ArrayList<ScanFileEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder = LayoutViewHolder(parent, R.layout.gallery_item_gallery_prev)

    override fun onBindViewHolder(holder: XViewHolder, position: Int) {
        displayPreview.invoke(galleryList[position], holder.frameLayout(R.id.galleryPrevContainer))
    }

    override fun getItemCount(): Int = galleryList.size

    fun addAll(newList: ArrayList<ScanFileEntity>) {
        galleryList.clear()
        galleryList.addAll(newList)
    }

    fun addSelectAll(newList: ArrayList<ScanFileEntity>) {
        selectList.clear()
        selectList.addAll(newList)
    }

    fun updateEntity() {
        galleryList.forEach { it.isSelected = false }
        selectList.forEach { select -> galleryList.find { it.id == select.id }?.isSelected = true }
        notifyDataSetChanged()
    }

    fun isCheck(position: Int) = galleryList[position].isSelected

    fun item(position: Int) = galleryList[position]

    fun containsSelect(selectEntity: ScanFileEntity) = selectList.contains(selectEntity)

    fun removeSelectEntity(removeEntity: ScanFileEntity) = selectList.remove(removeEntity)

    fun addSelectEntity(addEntity: ScanFileEntity) = selectList.add(addEntity)

    val currentSelectList: ArrayList<ScanFileEntity>
        get() = selectList

    val allItem: ArrayList<ScanFileEntity>
        get() = galleryList
}

