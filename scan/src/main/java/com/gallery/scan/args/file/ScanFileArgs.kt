package com.gallery.scan.args.file

import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.types.ResultType
import com.gallery.scan.types.SCAN_ALL
import com.gallery.scan.types.Sort
import kotlinx.android.parcel.Parcelize

/**
 * 文件扫描
 * 根据[MediaStore.Files.FileColumns.DATE_MODIFIED]字段[Sort.DESC]排序
 */
@Parcelize
class ScanFileArgs(
        private val scanTypeArray: Array<String>?,
        private val scanSortField: String = MediaStore.Files.FileColumns.DATE_MODIFIED,
        private val scanSort: String = Sort.DESC,
) : CursorLoaderArgs(FileColumns.uri, FileColumns.columns, "$scanSortField $scanSort") {

    override fun createSelection(args: Bundle): String? {
        val parent: Long = args.getLong(MediaStore.Files.FileColumns.PARENT)
        scanTypeArray ?: return null
        return when (args.getSerializable(MediaStore.Files.FileColumns.MIME_TYPE) as ResultType) {
            ResultType.SINGLE -> resultSelection(args.getLong(MediaStore.Files.FileColumns._ID), scanTypeArray)
            ResultType.MULTIPLE -> if (parent == SCAN_ALL) scanTypeSelection(scanTypeArray) else parentSelection(parent, scanTypeArray)
        }
    }

    override fun createSelectionArgs(args: Bundle): Array<String>? = scanTypeArray

    companion object {

        private const val APPEND = " ${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR"

        private fun parentSelection(parent: Long, scanTypeArray: Array<String>) = "${MediaStore.Files.FileColumns.PARENT}=$parent AND (${scanTypeSelection(scanTypeArray)})"

        private fun resultSelection(id: Long, scanTypeArray: Array<String>) = "${BaseColumns._ID}=$id AND (${scanTypeSelection(scanTypeArray)})"

        private fun scanTypeSelection(scanTypeArray: Array<String>): String {
            val defaultSelection = StringBuilder(" ${MediaStore.Files.FileColumns.SIZE} > 0 AND ")
            if (scanTypeArray.isNullOrEmpty()) {
                return defaultSelection.toString().removeSuffix("AND ")
            }
            scanTypeArray.forEach { _ -> defaultSelection.append(APPEND) }
            return defaultSelection.toString().removeSuffix("OR")
        }
    }
}