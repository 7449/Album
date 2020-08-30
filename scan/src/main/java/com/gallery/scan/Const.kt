package com.gallery.scan

internal const val SCAN_LOADER_ID = 111

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = -1111L

object Sort {
    const val DESC = "DESC"
    const val ASC = "ASC"
}

object ScanType {
    const val NONE = -1
    const val IMAGE = 0
    const val VIDEO = 1
    const val MIX = 2
}