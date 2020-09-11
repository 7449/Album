package com.gallery.scan.args

import android.database.Cursor

/**
 * 自定义实体工厂
 * 调用[onCreateCursor]可避免泛型强制转换
 */
interface ScanEntityFactory {

    companion object

    fun onCreateCursor(cursor: Cursor): ScanEntityFactory

    @Suppress("UNCHECKED_CAST")
    fun <ENTITY : ScanEntityFactory> onCreateCursorGeneric(cursor: Cursor): ENTITY = onCreateCursor(cursor) as ENTITY

}