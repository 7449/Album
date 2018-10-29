package com.album

import android.content.Context
import android.content.Intent
import com.album.ui.activity.AlbumActivity
import com.album.ui.activity.PreviewActivity
import com.yalantis.ucrop.UCrop

/**
 * by y on 14/08/2017.
 */

class Album {

    companion object {
        val instance by lazy { Album() }
        fun reset() {
            instance.apply {
                config = AlbumConfig()
                albumImageLoader = SimpleGlideAlbumImageLoader()
                options = UCrop.Options()
                albumListener = SimpleAlbumListener()
                albumCameraListener = null
                albumVideoListener = null
                emptyClickListener = null
                albumEntityList = null
                albumClass = AlbumActivity::class.java
                previewClass = PreviewActivity::class.java
            }
        }
    }

    var config = AlbumConfig()
    var albumImageLoader: AlbumImageLoader = SimpleGlideAlbumImageLoader()
    var options = UCrop.Options()
    var albumListener: AlbumListener = SimpleAlbumListener()
    var albumCameraListener: AlbumCameraListener? = null
    var albumVideoListener: AlbumVideoListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var albumEntityList: ArrayList<AlbumEntity>? = null
    var albumClass: Class<*> = AlbumActivity::class.java
    var previewClass: Class<*> = PreviewActivity::class.java

    fun start(context: Context) {
        val intent = Intent(context, albumClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
