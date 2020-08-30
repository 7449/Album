package com.gallery.scan.args

import android.net.Uri
import android.provider.MediaStore
import com.gallery.scan.ScanType
import com.gallery.scan.annotation.ScanTypeDef

internal object CursorArgs {

    /** Scan Uri */
    val FILE_URI: Uri = MediaStore.Files.getContentUri("external")

    /** 图片信息字段 */
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

    /** 图片信息条件 */
    const val ALL_SELECTION: String = "${Columns.SIZE} > 0 AND ${Columns.MEDIA_TYPE}=? OR ${Columns.MEDIA_TYPE}=?"

    /** 图片信息条件 */
    fun getParentSelection(parent: Long) = "${Columns.PARENT}=$parent AND ($ALL_SELECTION)"

    /** 图片信息条件 */
    fun getResultSelection(id: Long) = "${Columns.ID}=$id AND ($ALL_SELECTION)"

    /** 扫描条件 */
    fun getSelectionArgs(@ScanTypeDef scanType: Int): Array<String> = when (scanType) {
        ScanType.VIDEO -> arrayOf(Columns.VIDEO)
        ScanType.IMAGE -> arrayOf(Columns.IMAGE)
        ScanType.MIX -> arrayOf(Columns.IMAGE, Columns.VIDEO)
        ScanType.NONE -> emptyArray()
        else -> arrayOf(Columns.IMAGE)
    }
}