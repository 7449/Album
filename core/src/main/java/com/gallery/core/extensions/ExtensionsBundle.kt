package com.gallery.core.extensions

import android.os.Bundle
import android.os.Parcelable

fun Bundle?.getBooleanExpand(key: String): Boolean = getObjExpand(key) { false }

fun <T : Parcelable> Bundle?.getParcelableExpand(key: String): T = getParcelableOrDefault(key)

fun <T : Parcelable> Bundle?.getParcelableArrayListExpand(key: String): ArrayList<T> = getObjExpand(key) { arrayListOf() }

fun <T : Parcelable> Bundle?.getParcelableOrDefault(key: String, defaultValue: Parcelable = this?.getParcelable<T>(key)!!): T = getObjExpand(key) { defaultValue as T }

fun <T> Bundle?.getObjExpand(key: String, action: () -> T): T = this?.get(key) as? T ?: action.invoke()
