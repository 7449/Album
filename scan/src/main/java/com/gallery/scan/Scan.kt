package com.gallery.scan

interface Scan {

    /** 扫描文件夹 */
    fun scanParent(parentId: Long)

    /** 扫描拍照 */
    fun scanResult(id: Long)

}