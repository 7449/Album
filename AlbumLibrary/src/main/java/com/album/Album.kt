package com.album

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.yalantis.ucrop.UCrop

/**
 * by y on 14/08/2017.
 */
class Album {
    companion object {
        val instance by lazy { Album() }
        fun reset() {
            instance.apply {
                albumImageLoader = SimpleGlideAlbumImageLoader()
                options = UCrop.Options()
                albumListener = SimpleAlbumListener()
                albumCustomListener = null
                emptyClickListener = null
                albumEntityList = null
            }
        }
    }
    var albumImageLoader: AlbumImageLoader = SimpleGlideAlbumImageLoader()
    var options = UCrop.Options()
    var albumListener: AlbumListener = SimpleAlbumListener()
    var albumCustomListener: AlbumCustomListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var albumEntityList: ArrayList<AlbumEntity>? = null
}

fun Album.start(context: Context, cls: Class<*>) {
    start(context, AlbumBundle(), cls)
}

fun Album.start(context: Context, albumBundle: Parcelable, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply { putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle) })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}


/**
 * 如果使用默认的UILibrary,则可以调用这个方法修改颜色
 */
fun Album.start(context: Context, albumBundle: Parcelable, uiBundle: Parcelable, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}