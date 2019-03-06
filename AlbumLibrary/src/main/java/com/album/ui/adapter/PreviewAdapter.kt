package com.album.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import com.album.Album
import com.album.core.scan.AlbumEntity

/**
 *   @author y
 */
class PreviewAdapter(private val list: ArrayList<AlbumEntity>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val frameLayout = FrameLayout(container.context)
        val imageView = Album.instance.albumImageLoader?.displayPreview(list[position], frameLayout)
        imageView?.let { frameLayout.addView(it) }
        container.addView(frameLayout)
        return frameLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = list.size

    fun getAlbumPath(position: Int): String = list[position].path

    fun getAlbumEntity(position: Int): AlbumEntity = list[position]
}