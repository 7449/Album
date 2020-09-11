package com.gallery.scan.args

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.annotation.Size
import com.gallery.scan.annotation.SortDef
import com.gallery.scan.annotation.SortFieldDef
import com.gallery.scan.types.Sort
import kotlinx.android.parcel.Parcelize

/**
 * 扫描所需参数
 * 默认为File数据库，扫描图片，date_modified desc排序
 * 扫描[Columns.minimumColumns]字段
 */
//https://stackoverflow.com/questions/35976002/how-to-use-android-support-typedef-annotations-in-kotlin
//kotlin注解目前对StringDef IntDef支持有限
@Parcelize
class ScanParameter(
        /**
         * 扫描Uri
         * 默认为文件扫描
         */
        val scanUri: Uri = Columns.fileUri,
        /**
         * 扫描类型
         * 根据[MediaStore.Files.FileColumns.MEDIA_TYPE]搜索
         * [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]
         * [MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE]
         * [MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO]
         * [MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO]
         */
        @Size(min = 1)
        val scanType: IntArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
        /**
         * 排序类型，具体可见[SortDef]
         */
        @SortDef
        val scanSort: String = Sort.DESC,
        /**
         * 排序字段，具体可见[SortFieldDef]
         */
        @SortFieldDef
        val scanSortField: String = MediaStore.Files.FileColumns.DATE_MODIFIED,
        /**
         * 需要扫描的字段
         */
        @Size(min = 1)
        val scanColumns: Array<String> = Columns.minimumColumns,
) : Parcelable {
    companion object {

        private const val Key = "scanParameter"

        internal fun Bundle.putScanParameter(scanParameter: ScanParameter): Bundle {
            putParcelable(Key, scanParameter)
            return this
        }

        internal fun Bundle.getScanParameter(): ScanParameter {
            return getParcelable(Key) ?: ScanParameter()
        }

    }
}