package com.gallery.scan.types

import androidx.lifecycle.MutableLiveData

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = (-11111112).toLong()

/** postValue */
fun <T> MutableLiveData<T>.postValueExpand(value: T) {
    if (hasObservers()) {
        postValue(value)
    }
}

/** 是否是扫描全部的Id */
fun Long.isScanAllExpand(): Boolean = this == SCAN_ALL

/** 是否是空扫描 */
fun Long.isScanNoNeExpand(): Boolean = this == SCAN_NONE