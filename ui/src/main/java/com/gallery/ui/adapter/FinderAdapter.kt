package com.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R

class FinderAdapter(private val galleryUiBundle: GalleryUiBundle, private val displayFinder: (finderEntity: ScanEntity, container: FrameLayout) -> Unit) : BaseAdapter() {

    private val list: ArrayList<ScanEntity> = ArrayList()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val finderEntity = getItem(position)
        val viewHolder: ViewHolder
        val rootView = if (convertView == null) {
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_finder, parent, false).apply {
                viewHolder = ViewHolder(this)
                this.tag = viewHolder
            }
        } else {
            viewHolder = convertView.tag as ViewHolder
            convertView
        }
        viewHolder.appCompatTextView.setTextColor(galleryUiBundle.finderItemTextColor)
        viewHolder.appCompatTextView.text = "%s(%s)".format(finderEntity.bucketDisplayName, finderEntity.count.toString())
        displayFinder.invoke(finderEntity, viewHolder.frameLayout)
        return rootView
    }

    override fun getItem(position: Int): ScanEntity = list[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = list.size

    fun updateFinder(entities: ArrayList<ScanEntity>) {
        list.clear()
        list.addAll(entities)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) {
        val frameLayout: FrameLayout = view.findViewById(R.id.iv_gallery_finder_icon)
        val appCompatTextView: AppCompatTextView = view.findViewById(R.id.tv_gallery_finder_name)
    }
}