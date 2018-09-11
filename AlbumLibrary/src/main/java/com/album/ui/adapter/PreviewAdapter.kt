package com.album.ui.adapter

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.album.Album
import com.album.AlbumConstant
import com.album.entity.AlbumEntity
import com.album.ui.widget.TouchImageView

/**
 *   @author y
 */
class PreviewAdapter(private val list: ArrayList<AlbumEntity>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val frameLayout = FrameLayout(container.context)
        val imageView: ImageView
        imageView = if (!Album.instance.config.isFrescoImageLoader) {
            TouchImageView(frameLayout.context)
        } else {
            Album.instance.albumImageLoader.frescoView(frameLayout.context, AlbumConstant.TYPE_FRESCO_ALBUM)!!
        }
        frameLayout.addView(imageView)
        Album.instance.albumImageLoader.displayPreview(imageView, list[position])
        container.addView(frameLayout)
        return frameLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`
    override fun getCount(): Int = list.size

    fun getAlbumPath(position: Int): String {
        return list[position].path
    }

    fun getAlbumEntity(position: Int): AlbumEntity {
        return list[position]
    }
}