package com.gallery.scan.extensions

import android.database.Cursor

internal fun Cursor?.getIntOrDefault(columnName: String, defaultValue: Int = 0): Int =
        getValueOrDefault(columnName, { defaultValue }) { it.getInt(it.getColumnIndex(columnName)) }

internal fun Cursor?.getLongOrDefault(columnName: String, defaultValue: Long = 0.toLong()): Long =
        getValueOrDefault(columnName, { defaultValue }) { it.getLong(it.getColumnIndex(columnName)) }

internal fun Cursor?.getStringOrDefault(columnName: String, defaultValue: String = ""): String =
        getValueOrDefault(columnName, { defaultValue }) { it.getString(it.getColumnIndex(columnName)) }

internal inline fun <T> Cursor?.getValueOrDefault(name: String, valueNull: () -> T, a: (c: Cursor) -> T): T =
        if (this?.isNull(getColumnIndex(name)) == false) a.invoke(this) else valueNull.invoke()