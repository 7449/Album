package com.gallery.scan.impl.file

import android.os.Bundle
import android.provider.MediaStore
import com.gallery.scan.Types
import com.gallery.scan.args.MediaCursorLoaderArgs

class FileScanArgs(
    private val typeArray: Array<String> = arrayOf(),
    modified: String = MediaStore.Files.FileColumns.DATE_MODIFIED,
    sort: String = Types.Sort.DESC,
) : MediaCursorLoaderArgs(FileColumns.uri, FileColumns.columns, "$modified $sort") {

    override fun createSelection(args: Bundle): String {
        val parent = args.getLong(MediaStore.Files.FileColumns.PARENT)
        val id = args.getLong(MediaStore.Files.FileColumns._ID, DEFAULT_NULL_ID)
        return if (id != DEFAULT_NULL_ID) {
            idSql(id, typeArray)
        } else if (parent == Types.Id.ALL) {
            uniqueSql(typeArray)
        } else {
            parentSql(parent, typeArray)
        }
    }

    companion object {

        private const val AND = " AND "
        private const val OR = " OR "
        private const val MEDIA_TYPE = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=%s"
        private const val SIZE = "${MediaStore.Files.FileColumns.SIZE} > 0"

        /*
        * parent=[parent] AND [uniqueSql]
        **/
        private fun parentSql(parent: Long, typeArray: Array<String>) =
            "${MediaStore.Files.FileColumns.PARENT}=$parent$AND(${uniqueSql(typeArray)})"

        /*
        * _id=[id] AND [uniqueSql]
        **/
        private fun idSql(id: Long, typeArray: Array<String>) =
            "${MediaStore.Files.FileColumns._ID}=$id$AND(${uniqueSql(typeArray)})"

        /*
        * _size > 0
        * or
        * _size > 0 AND media_type=1
        * or
        * _size > 0 AND media_type=1 OR media_type=3
        **/
        private fun uniqueSql(typeArray: Array<String>): String {
            if (typeArray.isEmpty()) return SIZE
            val selection = StringBuilder(SIZE).append(AND)
            typeArray.forEach { value -> selection.append(MEDIA_TYPE.format(value)).append(OR) }
            return selection.toString().removeSuffix(OR)
        }

    }

}