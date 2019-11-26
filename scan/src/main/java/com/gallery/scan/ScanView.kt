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
    fun getScanContext(): FragmentActivity

    /**
     * 获取已经选择数据
     */
    fun getSelectEntity(): ArrayList<ScanEntity>

    /**
     * 当前扫描类型
     */
    fun currentScanType(): Int

    /**
     * 刷新数据
     */
    fun refreshUI()

    /**
     * 扫描成功
     * 扫描文件夹成功
     */
    fun scanSuccess(arrayList: ArrayList<ScanEntity>, finderList: ArrayList<ScanEntity>)

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(scanEntity: ScanEntity?)
}
