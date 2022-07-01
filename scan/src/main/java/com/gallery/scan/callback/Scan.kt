package com.gallery.scan.callback

import android.os.Bundle

interface Scan<E> {

    companion object;

    /** 扫描多个数据 */
    fun scanMultiple(args: Bundle)

    /** 扫描单个数据 */
    fun scanSingle(args: Bundle)

    /** 释放资源 */
    fun cleared()

}