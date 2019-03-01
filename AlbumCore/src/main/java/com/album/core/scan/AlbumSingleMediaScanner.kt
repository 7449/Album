package com.album.core.scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

/**
 * @author y
 * @create 2019/2/27
 * 更新系统图库信息，有可能会造成内存泄露，属于系统bug
 */
class AlbumSingleMediaScanner(context: Context, private val path: String, private val listener: SingleScannerListener, private val type: Int) : MediaScannerConnection.MediaScannerConnectionClient {

    interface SingleScannerListener {
        fun onScanStart()

        fun onScanCompleted(type: Int, path: String)
    }

    private var connection: MediaScannerConnection = MediaScannerConnection(context.applicationContext, this)

    init {
        connection.connect()
        listener.onScanStart()
    }

    override fun onMediaScannerConnected() {
        connection.scanFile(path, null)
    }

    fun disconnect() {
        connection.disconnect()
    }

    override fun onScanCompleted(path: String, uri: Uri) {
        disconnect()
        listener.onScanCompleted(type, path)
    }
}
