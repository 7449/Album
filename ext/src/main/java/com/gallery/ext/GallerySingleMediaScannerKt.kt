package com.gallery.ext

import android.net.Uri
import com.gallery.scan.SingleMediaScanner


class GallerySingleMediaScannerKt {

    private var onScanStart: (() -> Unit)? = null
    private var onScanCompleted: ((type: Int, path: String?, uri: Uri?) -> Unit)? = null

    fun onScanStart(onScanStart: () -> Unit) {
        this.onScanStart = onScanStart
    }

    fun onScanCompleted(onScanCompleted: (type: Int, path: String?, uri: Uri?) -> Unit) {
        this.onScanCompleted = onScanCompleted
    }

    internal fun build(): SingleMediaScanner.SingleScannerListener {
        return object : SingleMediaScanner.SingleScannerListener {
            override fun onScanStart() {
                onScanStart?.invoke()
            }

            override fun onScanCompleted(type: Int, path: String?, uri: Uri?) {
                onScanCompleted?.invoke(type, path, uri)
            }
        }
    }
}