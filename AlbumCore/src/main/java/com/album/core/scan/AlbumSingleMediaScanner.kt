package com.album.core.scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import java.io.File

/**
 * @author y
 * @create 2019/2/27
 * 更新系统图库信息，有可能会造成内存泄露，属于系统bug
 */
class AlbumSingleMediaScanner(context: Context, private val file: File, private val listener: SingleScannerListener, private val type: Int) : MediaScannerConnection.MediaScannerConnectionClient {

    interface SingleScannerListener {
        fun onScanStart()

        fun onScanCompleted(type: Int)
    }

    private var connection: MediaScannerConnection = MediaScannerConnection(context.applicationContext, this)

    init {
        connection.connect()
        listener.onScanStart()
    }

    override fun onMediaScannerConnected() {
        connection.scanFile(file.absolutePath, null)
    }

    fun disconnect() {
        connection.disconnect()
    }

    override fun onScanCompleted(path: String, uri: Uri) {
        disconnect()
        listener.onScanCompleted(type)
    }
}
