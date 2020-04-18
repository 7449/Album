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

    var list: ArrayList<ScanEntity> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val finderEntity = getItem(position)
        val viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = ViewHolder()
            viewHolder.convertView = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_finder, parent, false)
            viewHolder.frameLayout = viewHolder.convertView.findViewById(R.id.iv_gallery_finder_icon)
            viewHolder.appCompatTextView = viewHolder.convertView.findViewById(R.id.tv_gallery_finder_name)
            viewHolder.convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.appCompatTextView.setTextColor(galleryUiBundle.finderItemTextColor)
        viewHolder.appCompatTextView.text = String.format("%s(%s)", finderEntity.bucketDisplayName, finderEntity.count.toString())
        displayFinder.invoke(finderEntity, viewHolder.frameLayout)
        return viewHolder.convertView
    }

    override fun getItem(position: Int): ScanEntity = list[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = list.size
    private class ViewHolder {
        lateinit var convertView: View
        lateinit var frameLayout: FrameLayout
        lateinit var appCompatTextView: AppCompatTextView
    }
}