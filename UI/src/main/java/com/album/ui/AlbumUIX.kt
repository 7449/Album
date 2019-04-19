package com.album.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS
import com.album.ui.activity.AlbumActivity

/**
 * @author y
 * @create 2019/3/1
 */

fun Album.ui(context: Context) = ui(context, AlbumBundle())

fun Album.ui(context: Context, albumBundle: AlbumBundle) = ui(context, albumBundle, AlbumUiBundle())

fun Album.ui(context: Context, albumBundle: AlbumBundle, uiBundle: AlbumUiBundle) = apply {
    context.startActivity(Intent(context, AlbumActivity::class.java).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

