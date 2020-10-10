package com.gallery.scan.types

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.gallery.scan.ScanImpl

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = (-11111112).toLong()

/** 注册回调 */
fun <E : Parcelable> ScanImpl<E>.registerMultipleLiveData(owner: LifecycleOwner, action: (args: Bundle, result: ArrayList<E>) -> Unit) = also {
    multipleLiveData.observe(owner) { action.invoke(it.bundle, it.entities) }
}

/** 注册回调 */
fun <E : Parcelable> ScanImpl<E>.registerSingleLiveData(owner: LifecycleOwner, action: (args: Bundle, result: E?) -> Unit) = also {
    singleLiveData.observe(owner) { action.invoke(it.bundle, it.entity) }
}

/** 注册回调 */
fun <E : Parcelable> ScanImpl<E>.registerErrorLiveData(owner: LifecycleOwner, action: (type: ResultType) -> Unit) = also {
    errorLiveData.observe(owner) { action.invoke(it.type) }
}

/** postValue */
fun <T> MutableLiveData<T>.postValueExpand(value: T): Boolean {
    if (hasObservers()) {
        postValue(value)
        return true
    }
    return false
}

/** setValue */
fun <T> MutableLiveData<T>.setValueExpand(valueT: T): Boolean {
    if (hasObservers()) {
        value = valueT
        return true
    }
    return false
}

/** 是否是扫描全部的Id */
fun Long.isScanAllExpand(): Boolean = this == SCAN_ALL

/** 是否是空扫描 */
fun Long.isScanNoNeExpand(): Boolean = this == SCAN_NONE