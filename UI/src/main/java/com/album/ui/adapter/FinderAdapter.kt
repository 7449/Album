package com.album.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.album.Album
import com.album.core.scan.FinderEntity
import com.album.ui.AlbumUiBundle
import com.album.ui.R

/**
 *   @author y
 */
class FinderAdapter(private val list: ArrayList<FinderEntity>, private val albumUiBundle: AlbumUiBundle) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val finderEntity = getFinder(position)
        val viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = ViewHolder()
            viewHolder.convertView = LayoutInflater.from(parent.context).inflate(R.layout.album_item_finder, parent, false)
            viewHolder.frameLayout = viewHolder.convertView.findViewById(R.id.iv_album_finder_icon)
            viewHolder.appCompatTextView = viewHolder.convertView.findViewById(R.id.tv_album_finder_name)
            viewHolder.frameLayout.addView(Album.instance.albumImageLoader?.displayAlbumThumbnails(finderEntity, viewHolder.frameLayout))
            viewHolder.convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.appCompatTextView.setTextColor(ContextCompat.getColor(parent.context, albumUiBundle.listPopupItemTextColor))
        viewHolder.appCompatTextView.text = String.format("%s(%s)", finderEntity.dirName, finderEntity.count.toString())
        return viewHolder.convertView
    }

    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = list.size
    fun getFinder(position: Int): FinderEntity = list[position]
    fun refreshData(finderEntity: List<FinderEntity>) {
        list.clear()
        list.addAll(finderEntity)
        notifyDataSetChanged()
    }

    private class ViewHolder {
        lateinit var convertView: View
        lateinit var frameLayout: FrameLayout
        lateinit var appCompatTextView: AppCompatTextView
    }
}