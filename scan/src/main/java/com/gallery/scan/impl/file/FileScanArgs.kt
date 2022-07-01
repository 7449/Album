package com.gallery.scan.impl.file

import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import com.gallery.scan.Types
import com.gallery.scan.args.CursorLoaderArgs
import kotlinx.parcelize.Parcelize

/**
 * 文件扫描
 * 根据[MediaStore.Files.FileColumns.DATE_MODIFIED]字段[Types.Sort.DESC]排序
 */
@Parcelize
class FileScanArgs(
        private val scanTypeArray: Array<String>?,
        private val scanSortField: String = MediaStore.Files.FileColumns.DATE_MODIFIED,
        private val scanSort: String = Types.Sort.DESC,
) : CursorLoaderArgs(FileColumns.uri, FileColumns.columns, "$scanSortField $scanSort") {

    override fun createSelection(args: Bundle): String? {
        val parent: Long = args.getLong(MediaStore.Files.FileColumns.PARENT)
        val mimeType = args.getString(MediaStore.Files.FileColumns.MIME_TYPE)
        scanTypeArray ?: return null
        return when (mimeType) {
            Types.Result.SINGLE -> resultSelection(
                    args.getLong(MediaStore.Files.FileColumns._ID),
                    scanTypeArray
            )
            Types.Result.MULTIPLE -> if (parent == Types.Scan.ALL) scanTypeSelection(
                    scanTypeArray
            ) else parentSelection(
                    parent,
                    scanTypeArray
            )
            else -> throw KotlinNullPointerException("mime_type == [$mimeType]")
        }
    }

    override fun createSelectionArgs(args: Bundle): Array<String>? = scanTypeArray

    companion object {

        private const val APPEND = " ${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR"

        private fun parentSelection(parent: Long, scanTypeArray: Array<String>) =
                "${MediaStore.Files.FileColumns.PARENT}=$parent AND (${scanTypeSelection(scanTypeArray)})"

        private fun resultSelection(id: Long, scanTypeArray: Array<String>) =
                "${BaseColumns._ID}=$id AND (${scanTypeSelection(scanTypeArray)})"

        private fun scanTypeSelection(scanTypeArray: Array<String>): String {
            val defaultSelection = StringBuilder(" ${MediaStore.Files.FileColumns.SIZE} > 0 AND ")
            if (scanTypeArray.isEmpty()) {
                return defaultSelection.toString().removeSuffix("AND ")
            }
            scanTypeArray.forEach { _ -> defaultSelection.append(APPEND) }
            return defaultSelection.toString().removeSuffix("OR")
        }

    }

}