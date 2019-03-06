package com.album.ui.wechat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.album.Album
import com.album.AlbumBundle
import com.album.EXTRA_ALBUM_OPTIONS
import com.album.EXTRA_ALBUM_UI_OPTIONS
import com.album.listener.AlbumImageLoader

/**
 * @author y
 * @create 2019/3/5
 */

fun Album.weChatUI(context: Context, cls: Class<*>) {
    weChatUI(context, AlbumBundle(), cls)
}

fun Album.weChatUI(context: Context, albumBundle: AlbumBundle, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply { putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle) })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

fun Album.weChatUI(context: Context, albumBundle: AlbumBundle, uiBundle: Parcelable, cls: Class<*>) {
    context.startActivity(Intent(context, cls).apply {
        putExtras(Bundle().apply {
            putParcelable(EXTRA_ALBUM_OPTIONS, albumBundle)
            putParcelable(EXTRA_ALBUM_UI_OPTIONS, uiBundle)
        })
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

fun AlbumImageLoader.AlbumWeChatTouchImageView(container: FrameLayout): AlbumWeChatImageView {
    return if (container.childCount > 0) {
        container.getChildAt(0) as AlbumWeChatImageView
    } else {
        AlbumWeChatImageView(container.context)
    }
}