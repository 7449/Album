package com.gallery.scan.args

import android.database.Cursor
import android.os.Parcelable

/**
 * 自定义实体工厂
 * 调用[cursorMoveToNext]可避免泛型强制转换
 */
interface ScanEntityFactory {

    companion object

    fun cursorMoveToNext(cursor: Cursor): Parcelable

    @Suppress("UNCHECKED_CAST")
    fun <E : Parcelable> cursorMoveToNextGeneric(cursor: Cursor): E = cursorMoveToNext(cursor) as E

}