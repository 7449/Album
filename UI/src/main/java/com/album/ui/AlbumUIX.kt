package com.album.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS

/**
 * @author y
 * @create 2019/3/1
 */

fun Album.start(context: Context, cls: Class<*>) {
    start(context, AlbumBundle(), cls)
}

fun Album.start(context: Context, albumBundle: AlbumBundle, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply { putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle) })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

fun Album.start(context: Context, albumBundle: AlbumBundle, uiBundle: Parcelable, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}