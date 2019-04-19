package com.album.ui.wechat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS

/**
 * @author y
 * @create 2019/3/5
 */

fun Album.weChatUI(context: Context) = weChatUI(context, AlbumBundle())

fun Album.weChatUI(context: Context, albumBundle: AlbumBundle) = weChatUI(context, albumBundle, AlbumWeChatUiBundle())

fun Album.weChatUI(context: Context, albumBundle: AlbumBundle, uiBundle: AlbumWeChatUiBundle) = apply {
    context.startActivity(Intent(context, AlbumWeChatUiActivity::class.java).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

fun ViewGroup.AlbumWeChatTouchImageView(): AlbumWeChatImageView {
    return if (childCount > 0) {
        getChildAt(0) as AlbumWeChatImageView
    } else {
        AlbumWeChatImageView(context)
    }
}