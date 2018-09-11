package com.album.util.task

import android.os.HandlerThread

/**
 * by y on 21/08/2017.
 */

class AlbumTask : AlbumTaskCallBack {
    companion object {
        val instance by lazy { AlbumTask() }
    }

    private var handlerThread: HandlerThread? = null
    override fun start(call: AlbumTaskCallBack.Call) {
        quit()
        handlerThread = object : HandlerThread("Album") {
            override fun onLooperPrepared() {
                super.onLooperPrepared()
                call.start()
            }
        }
        handlerThread?.start()
    }

    override fun quit() {
        handlerThread?.quit()
    }


}
