package com.gallery.scan.extensions

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import com.gallery.scan.types.ScanType

/** 是否是扫描全部的Id */
fun Long.isScanAllExpand(): Boolean = this == ScanType.SCAN_ALL

/** 是否是空扫描 */
fun Long.isScanNoNeExpand(): Boolean = this == ScanType.SCAN_NONE

/** 获取可使用的多个扫描Bundle */
fun Long.multipleScanExpand(): Bundle = Bundle().apply { putLong(MediaStore.Files.FileColumns.PARENT, this@multipleScanExpand) }

/** 获取可使用的单个扫描Bundle */
fun Long.singleScanExpand(): Bundle = Bundle().apply { putLong(MediaStore.Files.FileColumns._ID, this@singleScanExpand) }

fun Cursor?.getIntOrDefault(columnName: String, defaultValue: Int = 0): Int =
        getValueOrDefault(columnName, { defaultValue }) { it.getInt(it.getColumnIndex(columnName)) }

fun Cursor?.getLongOrDefault(columnName: String, defaultValue: Long = 0.toLong()): Long =
        getValueOrDefault(columnName, { defaultValue }) { it.getLong(it.getColumnIndex(columnName)) }

fun Cursor?.getStringOrDefault(columnName: String, defaultValue: String = ""): String =
        getValueOrDefault(columnName, { defaultValue }) { it.getString(it.getColumnIndex(columnName)) }

inline fun <T> Cursor?.getValueOrDefault(name: String, valueNull: () -> T, a: (c: Cursor) -> T): T =
        if (this?.isNull(getColumnIndex(name)) == false) a.invoke(this) else valueNull.invoke()