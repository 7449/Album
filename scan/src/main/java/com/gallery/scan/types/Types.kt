package com.gallery.scan.types

/**
 * 扫描类型
 */
object ScanType {
    const val SCAN_ALL = (-111111111).toLong()
    const val SCAN_NONE = (-11111112).toLong()
}

/**
 * 排序方式
 */
object Sort {
    const val DESC = "DESC"
    const val ASC = "ASC"
}

/**
 * 用作区分是扫描还是单个文件
 */
object ResultType {
    const val MULTIPLE = "MULTIPLE"
    const val SINGLE = "SINGLE"
}