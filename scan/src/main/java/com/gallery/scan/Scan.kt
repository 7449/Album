package com.gallery.scan

import android.os.Bundle

interface Scan {

    /** 扫描多个数据 */
    fun scanMultiple(bundle: Bundle)

    /** 扫描单个数据 */
    fun scanSingle(bundle: Bundle)

}