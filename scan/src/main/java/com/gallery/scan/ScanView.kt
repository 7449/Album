package com.gallery.scan

import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.IScanEntityFactory
import com.gallery.scan.args.ScanParameter

/**
 * @author y
 * @create 2019/2/27
 */
interface ScanView<ENTITY : IScanEntityFactory> {

    /**
     *  [LoaderManager.getInstance]
     */
    val scanContext: FragmentActivity

    /**
     * 扫描所需参数
     */
    val scanParameter: ScanParameter

    /**
     * 自定义参数
     */
    val scanFactoryCreate: IScanEntityFactory

    /**
     * 扫描成功
     */
    fun scanSuccess(arrayList: ArrayList<ENTITY>) {}

    /**
     * 扫描异常
     */
    fun scanError() {
        scanSuccess(arrayListOf())
    }

    /**
     * 单个文件扫描成功
     */
    fun resultSuccess(scanEntity: ENTITY?) {}

    /**
     * 单个文件扫描失败
     */
    fun resultError() {
        resultSuccess(null)
    }
}
