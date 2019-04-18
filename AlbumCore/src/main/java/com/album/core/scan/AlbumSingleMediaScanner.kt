package com.album.core.scan

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri

/**
 * @author y
 * @create 2019/2/27
 * 更新系统图库信息，有可能会造成内存泄露，属于系统bug
 */
class AlbumSingleMediaScanner private constructor(context: Context, private val path: String, private val type: Int, private val listener: SingleScannerListener) : MediaScannerConnection.MediaScannerConnectionClient {

    interface SingleScannerListener {
        fun onScanStart()

        fun onScanCompleted(type: Int, path: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context, path: String, type: Int, listener: SingleScannerListener) = AlbumSingleMediaScanner(context, path, type, listener)

        fun newInstance(context: Context, path: String, type: Int, albumSingleMediaScannerKt: AlbumSingleMediaScannerKt.() -> Unit) = AlbumSingleMediaScanner(context, path, type, AlbumSingleMediaScannerKt().also(albumSingleMediaScannerKt).build())
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

class AlbumSingleMediaScannerKt {

    private var onScanStart: (() -> Unit)? = null
    private var onScanCompleted: ((type: Int, path: String) -> Unit)? = null

    fun onScanStart(onScanStart: () -> Unit) {
        this.onScanStart = onScanStart
    }

    fun onScanCompleted(onScanCompleted: (type: Int, path: String) -> Unit) {
        this.onScanCompleted = onScanCompleted
    }

    internal fun build(): AlbumSingleMediaScanner.SingleScannerListener {
        return object : AlbumSingleMediaScanner.SingleScannerListener {
            override fun onScanStart() {
                onScanStart?.invoke()
            }

            override fun onScanCompleted(type: Int, path: String) {
                onScanCompleted?.invoke(type, path)
            }
        }
    }
}