package com.album.sample.kt

import com.album.core.scan.AlbumSingleMediaScanner


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