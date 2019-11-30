package com.gallery.core.action

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.gallery.scan.ScanEntity

internal interface SimpleGalleryFragmentInterface {
    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 扫描设备
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanGallery(parent: Long, isFinder: Boolean, result: Boolean)

    /**
     * 裁剪
     */
    fun openUCrop(uri: Uri)

    /**
     * 刷新图库
     */
    fun refreshMedia(type: Int, path: String)

    /**
     * 选择选中的数据
     */
    fun selectPreview(): ArrayList<ScanEntity>

    /**
     * 确定数据
     */
    fun multipleSelect()

    /**
     * 全部数据
     */
    fun allPreview(): ArrayList<ScanEntity>

    /**
     * 刷新[FragmentActivity.onActivityResult]数据
     */
    fun onResultPreview(bundle: Bundle)

    /**
     * 断掉MediaScanner
     */
    fun disconnectMediaScanner()
}
