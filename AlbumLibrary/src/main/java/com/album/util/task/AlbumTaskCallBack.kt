package com.album.util.task

/**
 * by y on 21/08/2017.
 */

interface AlbumTaskCallBack {
    fun start(call: Call)

    fun quit()

    interface Call {
        fun start()
    }
}
