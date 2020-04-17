package com.gallery.scan

import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager

/**
 * @author y
 * @create 2019/2/27
 */
interface ScanView {
    /**
     *  [LoaderManager.getInstance]
     */
    val scanContext: FragmentActivity

    /**
     * 当前扫描类型
     */
    val currentScanType: ScanType

    /**
     * 扫描成功
     */
    fun scanSuccess(arrayList: ArrayList<ScanEntity>)

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(scanEntity: ScanEntity?)
}
