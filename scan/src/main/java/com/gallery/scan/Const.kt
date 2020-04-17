package com.gallery.scan

internal const val SCAN_LOADER_ID = 111

const val SCAN_ALL = (-111111111).toLong()

enum class ScanType(val type: Int) {
    IMAGE(0),
    VIDEO(1),
    MIX(2),
}