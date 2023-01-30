package com.gallery.scan.args

import android.database.Cursor

interface MediaScanEntityFactory {

    companion object {
        fun action(action: (cursor: Cursor) -> Any) = object : MediaScanEntityFactory {
            override fun cursorMoveToNext(cursor: Cursor): Any {
                return action.invoke(cursor)
            }
        }
    }

    fun cursorMoveToNext(cursor: Cursor): Any

    @Suppress("UNCHECKED_CAST")
    fun <E> cursorMoveToNextGeneric(cursor: Cursor): E = cursorMoveToNext(cursor) as E

}