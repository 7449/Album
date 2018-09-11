package com.album.util.scanner

import com.album.annotation.AlbumResultType

interface SingleScannerListener {
    fun onScanStart()

    fun onScanCompleted(@AlbumResultType type: Int)
}