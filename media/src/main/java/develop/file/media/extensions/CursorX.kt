package develop.file.media.extensions

import android.database.Cursor

internal fun Cursor?.intOrDefault(name: String, defaultValue: Int = 0): Int =
    value(name, { defaultValue }) { it.getInt(it.columnIndex(name)) }

internal fun Cursor?.longOrDefault(columnName: String, defaultValue: Long = 0.toLong()): Long =
    value(columnName, { defaultValue }) { it.getLong(it.columnIndex(columnName)) }

internal fun Cursor?.stringOrDefault(columnName: String, defaultValue: String = ""): String =
    value(columnName, { defaultValue }) { it.getString(it.columnIndex(columnName)) }

internal inline fun <T> Cursor?.value(
    name: String,
    valueNull: () -> T,
    a: (c: Cursor) -> T
): T = if (this?.isNull(columnIndex(name)) == false) a.invoke(this) else valueNull.invoke()

internal fun Cursor?.columnIndex(columnName: String): Int {
    return this?.getColumnIndex(columnName) ?: 0
}