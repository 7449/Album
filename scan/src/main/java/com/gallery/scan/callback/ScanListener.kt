package com.gallery.scan.callback

/**
 * 数据回调
 */
interface ScanListener<E> {

    /**
     * 扫描成功
     */
    fun scanMultipleSuccess(entities: ArrayList<E>) {}

    /**
     * 扫描异常
     */
    fun scanMultipleError() {
        scanMultipleSuccess(arrayListOf())
    }

    /**
     * 单个扫描成功
     */
    fun scanSingleSuccess(entity: E?) {}

    /**
     * 单个扫描失败
     */
    fun scanSingleError() {
        scanSingleSuccess(null)
    }

}