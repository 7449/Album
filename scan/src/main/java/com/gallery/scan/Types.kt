package com.gallery.scan

/**
 * 类型Type
 */
object Types {

    /**
     * 扫描类型
     */
    object Scan {
        /**
         * 扫描全部
         */
        const val SCAN_ALL = (-111111111).toLong()

        /**
         * 不扫描
         */
        const val SCAN_NONE = (-11111112).toLong()
    }

    /**
     * 排序方式
     */
    object Sort {
        /**
         * 降序
         */
        const val DESC = "DESC"

        /**
         * 升序
         */
        const val ASC = "ASC"
    }

    /**
     * 用作区分是扫描还是单个文件
     */
    object Result {
        /**
         * 扫描多文件
         */
        const val MULTIPLE = "MULTIPLE"

        /**
         * 扫描单文件
         */
        const val SINGLE = "SINGLE"
    }

}