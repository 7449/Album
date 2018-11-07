package com.album.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.album.*
import com.album.ui.AlbumImageView

/**
 *   @author y
 */
class FinderAdapter(private val list: List<FinderEntity>) : BaseAdapter() {

    private val albumConfig: AlbumConfig = Album.instance.config
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = ViewHolder()
            viewHolder.convertView = LayoutInflater.from(parent.context).inflate(R.layout.album_item_finder, parent, false)
            viewHolder.frameLayout = viewHolder.convertView.findViewById(R.id.iv_album_finder_icon)
            viewHolder.appCompatTextView = viewHolder.convertView.findViewById(R.id.tv_album_finder_name)
            val imageView: View = if (albumConfig.isFrescoImageLoader) {
                Album.instance.albumImageLoader.frescoView(parent.context, AlbumConstant.TYPE_FRESCO_ALBUM)!!
            } else {
                AlbumImageView(parent.context)
            }
            viewHolder.frameLayout.addView(imageView)
            viewHolder.convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        val finderEntity = getFinder(position)
        viewHolder.appCompatTextView.setTextColor(ContextCompat.getColor(parent.context, albumConfig.albumListPopupItemTextColor))
        viewHolder.appCompatTextView.text = String.format("%s(%s)", finderEntity.dirName, finderEntity.count.toString())
        Album.instance.albumImageLoader.displayAlbumThumbnails(viewHolder.frameLayout.getChildAt(0), finderEntity)
        return viewHolder.convertView
    }

    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = list.size
    fun getFinder(position: Int): FinderEntity = list[position]

    private class ViewHolder {
        internal lateinit var convertView: View
        internal lateinit var frameLayout: FrameLayout
        internal lateinit var appCompatTextView: AppCompatTextView
    }
}