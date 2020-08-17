package com.gallery.scan

import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.gallery.scan.annotation.ScanTypeDef
import com.gallery.scan.annotation.SortDef
import com.gallery.scan.annotation.SortFieldDef
import com.gallery.scan.args.Columns

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
     * [ScanType.IMAGE]
     * [ScanType.VIDEO]
     * [ScanType.MIX]
     */
    @ScanTypeDef
    fun scanType(): Int

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
     * 拍照扫描成功
     */
    fun resultSuccess(scanEntity: ScanEntity?) {}
}
