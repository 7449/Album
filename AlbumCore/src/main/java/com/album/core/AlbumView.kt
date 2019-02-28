package com.album.core

import android.view.View

/**
 * @author y
 * @create 2019/2/28
 */
object AlbumView {

    fun View.show() {
        if (visibility == View.VISIBLE) {
            return
        }
        visibility = View.VISIBLE
    }

    fun View.hide() {
        if (visibility == View.GONE) {
            return
        }
        visibility = View.GONE
    }

}