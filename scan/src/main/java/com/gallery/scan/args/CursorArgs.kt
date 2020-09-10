package com.gallery.scan.args

import android.net.Uri
import android.provider.MediaStore

internal object CursorArgs {

    /** Scan Uri */
    val FILE_URI: Uri = MediaStore.Files.getContentUri("external")

    /** 文件信息字段 */
    val ALL_COLUMNS: Array<String> = arrayOf(
            Columns.ID,
            Columns.SIZE,
            Columns.DURATION,
            Columns.PARENT,
            Columns.MIME_TYPE,
            Columns.DISPLAY_NAME,
            Columns.ORIENTATION,
            Columns.BUCKET_ID,
            Columns.BUCKET_DISPLAY_NAME,
            Columns.MEDIA_TYPE,
            Columns.WIDTH,
            Columns.HEIGHT,
            Columns.DATE_MODIFIED,
            Columns.DATE_ADDED
    )

    /** 追加搜索条件 */
    private const val APPEND = " ${Columns.MEDIA_TYPE}=? OR"

    /** 文件信息条件 */
    fun getScanTypeSelection(intArray: IntArray): String {
        val defaultSelection = StringBuilder(" ${Columns.SIZE} > 0 AND ")
        intArray.forEach { _ -> defaultSelection.append(APPEND) }
        return defaultSelection.toString().removeSuffix("OR")
    }

    /** 文件信息条件 */
    fun getParentSelection(parent: Long, intArray: IntArray) = "${Columns.PARENT}=$parent AND (${getScanTypeSelection(intArray)})"

    /** 文件信息条件 */
    fun getResultSelection(id: Long, intArray: IntArray) = "${Columns.ID}=$id AND (${getScanTypeSelection(intArray)})"
}