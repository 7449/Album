package com.gallery.scan.args

import android.database.Cursor

/**
 * 自定义实体工厂
 * 调用[cursorMoveToNext]可避免泛型强制转换
 */
interface ScanEntityFactory {

    companion object

    fun cursorMoveToNext(cursor: Cursor): ScanEntityFactory

    @Suppress("UNCHECKED_CAST")
    fun <ENTITY : ScanEntityFactory> cursorMoveToNextGeneric(cursor: Cursor): ENTITY = cursorMoveToNext(cursor) as ENTITY

}