package com.album.simple

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.album.core.scan.AlbumEntity

internal interface SimpleAlbumFragmentInterface {
    /**
     * 打开相机
     */
    fun startCamera()

    /**
     * 扫描设备
     * [isFinder] 是否点击文件夹扫描
     * [result] 是否是拍照之后的扫描
     */
    fun onScanAlbum(parent: Long, isFinder: Boolean, result: Boolean)

    /**
     * 扫描裁剪之后的信息
     */
    fun onScanCropAlbum(path: String)

    /**
     * 裁剪
     */
    fun openUCrop(path: String)

    /**
     * 刷新图库
     */
    fun refreshMedia(type: Int, path: String)

    /**
     * 选择选中的数据
     */
    fun selectPreview(): ArrayList<AlbumEntity>

    /**
     * 确定数据
     */
    fun multipleSelect()

    /**
     * 全部数据
     */
    fun allPreview(): ArrayList<AlbumEntity>

    /**
     * 刷新[FragmentActivity.onActivityResult]数据
     */
    fun onResultPreview(bundle: Bundle)

    /**
     * 刷新数据
     */
    fun onDialogResultPreview(bundle: Bundle)

    /**
     * 断掉MediaScanner
     */
    fun disconnectMediaScanner()
}