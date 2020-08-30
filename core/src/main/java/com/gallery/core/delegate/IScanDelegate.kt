package com.gallery.core.delegate

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.ScanArgs
import com.gallery.core.expand.PermissionCode
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity

internal interface IScanDelegate {

    /**
     * 当前Activity
     */
    val activity: FragmentActivity?

    /**
     * 当前Activity
     */
    val activityNotNull: FragmentActivity

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
     * 横竖屏切换
     */
    fun onSaveInstanceState(outState: Bundle)

    /**
     * 初始化
     */
    fun onCreate(savedInstanceState: Bundle?)

    /**
     * 销毁
     */
    fun onDestroy()

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
     * [ScanArgs.selectList] 选中的数据
     * [ScanArgs.isRefresh] 是否刷新数据(合并选中的数据)
     */
    fun onUpdateResult(scanArgs: ScanArgs?)

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
    fun cameraOpen()

    /**
     * 拍照成功
     */
    fun cameraSuccess()

    /**
     * 取消相机
     */
    fun cameraCanceled()

    /**
     * 允许权限
     */
    fun permissionsGranted(type: PermissionCode)

    /**
     * 权限被拒
     */
    fun permissionsDenied(type: PermissionCode)

}