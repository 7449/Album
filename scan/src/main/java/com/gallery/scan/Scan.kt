package com.gallery.scan

interface Scan {

    /**
     * 扫描全部
     */
    fun scanAll()

    /**
     * 扫描文件夹
     */
    fun scanParent(parent: Long)

    /**
     * 扫描拍照
     */
    fun scanResult(id: Long)

}