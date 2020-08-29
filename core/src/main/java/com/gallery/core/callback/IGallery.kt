package com.gallery.core.callback

import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity

internal interface IGallery {
    /**
     * 当前扫描的数据
     * 已经过滤了CAMERA
     */
    val currentEntities: ArrayList<ScanEntity>

    /**
     * 当前选中的数据
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
     * 扫描设备
     */
    fun onScanGallery(parent: Long = SCAN_ALL, isCamera: Boolean = false)

    /**
     * 扫描单个数据
     */
    fun onScanResult(uri: Uri)

    /**
     * 刷新预览之后的数据
     * [Bundle.getParcelableArrayList] 选中的数据
     * [Bundle.getBoolean] 是否刷新数据(合并选中的数据)
     */
    fun onUpdateResult(bundle: Bundle)

    /**
     * 刷新单个Item
     */
    fun notifyItemChanged(position: Int)

    /**
     * 刷新全部数据
     */
    fun notifyDataSetChanged()

    /**
     * 监听滑动
     */
    fun addOnScrollListener(onScrollListener: RecyclerView.OnScrollListener)

    /**
     * 滑动到某个位置
     */
    fun scrollToPosition(position: Int)

    /**
     * 打开相机
     */
    fun openCamera()
}