package com.album.util

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import com.album.AlbumResultType
import java.io.File

/**
 * by y on 14/08/2017.
 *
 *
 *
 *
 * https://issuetracker.google.com/issues/37046656
 *
 *
 * https://github.com/square/leakcanary/issues/26
 */

class SingleMediaScanner(context: Context, private val file: File, private val listener: SingleScannerListener, @param:AlbumResultType private val type: Int) : MediaScannerConnection.MediaScannerConnectionClient {

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


interface SingleScannerListener {
    fun onScanStart()

    fun onScanCompleted(@AlbumResultType type: Int)
}