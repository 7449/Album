package com.gallery.scan

import android.provider.MediaStore
import androidx.annotation.Size
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.gallery.scan.annotation.SortDef
import com.gallery.scan.annotation.SortFieldDef
import com.gallery.scan.args.Columns
import com.gallery.scan.types.Sort

/**
 * @author y
 * @create 2019/2/27
 */
//https://stackoverflow.com/questions/35976002/how-to-use-android-support-typedef-annotations-in-kotlin
//kotlin注解目前对StringDef IntDef支持有限
interface ScanView {

    /**
     *  [LoaderManager.getInstance]
     */
    val scanContext: FragmentActivity

    /**
     * 扫描类型
     * 根据[MediaStore.Files.FileColumns.MEDIA_TYPE]搜索
     * 可输入以下Type,最少一个类型type,最多三个(去掉NONE)
     * [MediaStore.Files.FileColumns.MEDIA_TYPE_NONE]
     * [MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE]
     * [MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO]
     * [MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO]
     */
    @Size(min = 1, max = 3)
    fun scanType(): IntArray

    /**
     * 排序类型
     * [Sort.DESC]
     * [Sort.ASC]
     */
    @SortDef
    fun scanSort(): String = Sort.DESC

    /**
     * 排序字段
     * [Columns.SIZE]
     * [Columns.PARENT]
     * [Columns.MIME_TYPE]
     * [Columns.DISPLAY_NAME]
     * [Columns.MEDIA_TYPE]
     * [Columns.DATE_ADDED]
     * [Columns.DATE_MODIFIED]
     * [Columns.BUCKET_ID]
     * [Columns.BUCKET_DISPLAY_NAME]
     */
    @SortFieldDef
    fun scanSortField(): String = Columns.DATE_MODIFIED

    /**
     * 扫描成功
     */
    fun scanSuccess(arrayList: ArrayList<ScanEntity>) {}

    /**
     * 扫描异常
     */
    fun scanError() {
        scanSuccess(arrayListOf())
    }

    /**
     * 单个文件扫描成功
     */
    fun resultSuccess(scanEntity: ScanEntity?) {}

    /**
     * 单个文件扫描失败
     */
    fun resultError() {
        resultSuccess(null)
    }
}
