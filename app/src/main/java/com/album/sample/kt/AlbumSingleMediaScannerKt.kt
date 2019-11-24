package com.album.sample.kt

import com.album.scan.SingleMediaScanner


class AlbumSingleMediaScannerKt {

    private var onScanStart: (() -> Unit)? = null
    private var onScanCompleted: ((type: Int, path: String) -> Unit)? = null

    fun onScanStart(onScanStart: () -> Unit) {
        this.onScanStart = onScanStart
    }

    fun onScanCompleted(onScanCompleted: (type: Int, path: String) -> Unit) {
        this.onScanCompleted = onScanCompleted
    }

    internal fun build(): SingleMediaScanner.SingleScannerListener {
        return object : SingleMediaScanner.SingleScannerListener {
            override fun onScanStart() {
                onScanStart?.invoke()
            }

            override fun onScanCompleted(type: Int, path: String) {
                onScanCompleted?.invoke(type, path)
            }
        }
    }
}