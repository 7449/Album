package com.gallery.scan.types

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.gallery.scan.ScanImpl
import com.gallery.scan.args.ScanEntityFactory

const val SCAN_ALL = (-111111111).toLong()
const val SCAN_NONE = (-11111112).toLong()

/** 注册回调 */
fun <T : ScanEntityFactory> ScanImpl<T>.registerMultipleLiveData(owner: LifecycleOwner, action: (args: Bundle, result: ArrayList<T>) -> Unit) = also {
    multipleLiveData.observe(owner) { action.invoke(it.bundle, it.entities) }
}

/** 注册回调 */
fun <T : ScanEntityFactory> ScanImpl<T>.registerSingleLiveData(owner: LifecycleOwner, action: (args: Bundle, result: T?) -> Unit) = also {
    singleLiveData.observe(owner) { action.invoke(it.bundle, it.entity) }
}

/** 注册回调 */
fun <T : ScanEntityFactory> ScanImpl<T>.registerErrorLiveData(owner: LifecycleOwner, action: (type: Result) -> Unit) = also {
    errorLiveData.observe(owner) { action.invoke(it.type) }
}

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