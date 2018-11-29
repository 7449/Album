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
        fun destroy() {
            instance.apply {
                options = null
                albumListener = null
                customCameraListener = null
                emptyClickListener = null
                albumImageLoader = null
                initList = null
            }
        }
    }

    var options: UCrop.Options? = null
    var albumImageLoader: AlbumImageLoader? = null
    var albumListener: AlbumListener? = null
    var customCameraListener: AlbumCustomCameraListener? = null
    var emptyClickListener: OnEmptyClickListener? = null
    var initList: ArrayList<AlbumEntity>? = null
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

fun Album.start(context: Context, albumBundle: Parcelable, uiBundle: Parcelable, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}