package com.gallery.core.callback

import android.net.Uri
import android.os.Bundle
import com.gallery.core.ResultType
import com.gallery.core.callback.InternalConfig.CAMERA_PARENT_ID
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity

/**
 *
 */
interface IGallery {
    /**
     * 当前扫描的数据
     * 数据已经过滤了[CAMERA_PARENT_ID]
     */
    val currentEntities: ArrayList<ScanEntity>

    /**
     * 当前选中的数据,单选无效
     */
    val selectEntities: ArrayList<ScanEntity>

    /**
     * 当前选中的数据是否为空
     * true 空
     */
    val selectEmpty: Boolean

    /**
     * 当前选中的数据个数
     */
    val selectCount: Int

    /**
     * 当前扫描的数据个数
     * 已经过滤了CAMERA
     */
    val itemCount: Int

    /**
     * 打开相机
     */
    fun openCamera()

    /**
     * 打开裁剪
     */
    fun openCrop(uri: Uri)

    /**
     * 刷新图库
     * [ResultType.CROP]
     * [ResultType.CAMERA]
     */
    fun scanFile(type: ResultType, path: String)

    /**
     * 扫描设备
     * [result] 是否是拍照或者裁剪之后的扫描
     */
    fun onScanGallery(parent: Long = SCAN_ALL, result: Boolean = false)

    /**
     * 刷新预览之后的数据
     * [Bundle.getParcelableArrayList] 选中的数据
     * [Bundle.getBoolean] 是否刷新数据(合并选中的数据)
     */
    fun onUpdatePrevResult(bundle: Bundle)

    /**
     * 用于刷新单个Item
     */
    fun notifyItemChanged(position: Int)

    /**
     * 刷新全部数据
     */
    fun notifyDataSetChanged()

    /**
     * 取消裁剪
     */
    fun onCropCanceled()

    /**
     * 裁剪异常
     */
    fun onCropError(throwable: Throwable?)

    /**
     * 裁剪成功
     */
    fun onCropSuccess(uri: Uri)

}