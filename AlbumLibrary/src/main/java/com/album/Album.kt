package com.album

import android.content.Context
import android.content.Intent
import com.album.entity.AlbumEntity
import com.album.listener.*
import com.album.ui.activity.AlbumActivity
import com.album.ui.widget.SimpleAlbumListener
import com.album.ui.widget.SimpleGlideAlbumImageLoader
import com.yalantis.ucrop.UCrop

/**
 * by y on 14/08/2017.
 */

class Album {
    companion object {
        val instance by lazy { Album() }
    }
    var config = AlbumConfig()
    var albumImageLoader: AlbumImageLoader = SimpleGlideAlbumImageLoader()
    var options = UCrop.Options()
    var albumListener: AlbumListener = SimpleAlbumListener()
    var albumCameraListener: AlbumCameraListener? = null
    var albumVideoListener: AlbumVideoListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var albumEntityList: ArrayList<AlbumEntity>? = null
    var albumClass: Class<*>? = null
    var previewClass: Class<*>? = null
    fun start(context: Context) {
        if (albumClass == null) {
            albumClass = AlbumActivity::class.java
        }
        val intent = Intent(context, albumClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
