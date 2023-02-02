package com.gallery.core.callback

import android.os.Bundle
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.PermissionCode

interface IGalleryCallback {

    /*** [IScanDelegate.onCreate]触发*/
    fun onGalleryCreated(delegate: IScanDelegate, saveState: Bundle?)

    /*** 单选非裁剪状态下选择数据返回*/
    fun onGalleryResource(entity: ScanEntity)

    /*** 多选时该文件已经被删除*/
    fun onSelectMultipleFileNotExist(entity: ScanEntity)

    /*** 已达到选择最大数*/
    fun onSelectMultipleMaxCount()

    /*** 点击CheckBox时会触发*/
    fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity)

    /*** 点击图片时该文件已被删除*/
    fun onClickItemFileNotExist(entity: ScanEntity)

    /*** 如果图片存在,并且不是视频模式,不是单选的情况下触发这个方法*/
    fun onPhotoItemClick(entity: ScanEntity, position: Int, parentId: Long)

    /*** 刷新预览页数据之后触发*/
    fun onRefreshResultChanged()

    /*** 没有扫描到任何数据*/
    fun onScanMultipleEmpty()

    /*** 每次扫描之后数据非空触发*/
    fun onScanMultipleSuccess()

    /*** 单个文件扫描成功*/
    fun onScanSingleSuccess(entity: ScanEntity)

    /*** 拍照或者摄像或者扫描单个数据失败*/
    fun onScanSingleFailure()

    /*** 打开相机返回的状态*/
    fun onCameraOpenStatus(status: CameraStatus)

    /*** 取消拍照*/
    fun onCameraCanceled()

    /*** 视频播放异常*/
    fun onOpenVideoError(entity: ScanEntity)

    /*** 权限被拒*/
    fun onPermissionsDenied(type: PermissionCode)

}