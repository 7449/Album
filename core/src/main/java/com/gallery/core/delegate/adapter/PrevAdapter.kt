package com.gallery.core.delegate.adapter

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.entity.ScanEntity

class PrevAdapter(private val displayPreview: (scanEntity: ScanEntity, container: FrameLayout) -> Unit) :
        RecyclerView.Adapter<PrevAdapter.ViewHolder>() {

    private val galleryList: ArrayList<ScanEntity> = arrayListOf()
    private val selectList: ArrayList<ScanEntity> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FrameLayout(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        displayPreview.invoke(galleryList[position], holder.view)
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

    class ViewHolder(val view: FrameLayout) : RecyclerView.ViewHolder(view)

}

