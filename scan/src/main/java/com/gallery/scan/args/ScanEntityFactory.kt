package com.gallery.scan.args

import android.database.Cursor
import com.gallery.scan.task.ScanTask

/**
 * 自定义实体工厂
 * 调用[cursorMoveToNext]可避免泛型强制转换
 * [ScanTask]
 */
interface ScanEntityFactory {

    companion object {
        fun action(action: (cursor: Cursor) -> Any) = object : ScanEntityFactory {
            override fun cursorMoveToNext(cursor: Cursor): Any {
                return action.invoke(cursor)
            }
        }
    }

    fun cursorMoveToNext(cursor: Cursor): Any

    @Suppress("UNCHECKED_CAST")
    fun <E> cursorMoveToNextGeneric(cursor: Cursor): E = cursorMoveToNext(cursor) as E

}