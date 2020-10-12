@file:Suppress("UNCHECKED_CAST")

package com.gallery.scan.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.gallery.scan.ScanImpl
import com.gallery.scan.result.Result

/** 注册回调 */
fun <E> ScanImpl<E>.registerLiveData(owner: LifecycleOwner, action: (type: Result<E>) -> Unit) = also {
    resultLiveData.observe(owner) { action.invoke(it as Result<E>) }
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