package com.gallery.core.action

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.gallery.scan.ScanEntity

internal interface ScanInterface {
    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 裁剪
     */
    fun openCrop(uri: Uri)

    /**
     * 刷新图库
     */
    fun scanFile(type: Int, path: String)

    /**
     * 断掉MediaScanner
     */
    fun disconnect()

    /**
     * 扫描设备
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanGallery(parent: Long, isFinder: Boolean, result: Boolean)

    /**
     * 全部数据
     */
    val allGalleryList: ArrayList<ScanEntity>

    /**
     * 刷新[FragmentActivity.onActivityResult]数据
     */
    fun onResultPreview(bundle: Bundle)
}
