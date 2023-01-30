package com.gallery.core.callback

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.gallery.core.GalleryConfigs
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.delegate.adapter.GalleryAdapter.PhotoViewHolder
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.PermissionCode

interface IGalleryCallback {

    /**
     * [IScanDelegate.onCreate]触发
     */
    fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    )

    /**
     * 单选非裁剪状态下,点击[Adapter]item返回的数据
     * [ScanEntity.delegate]为获取的数据
     */
    fun onGalleryResource(context: Context, scanEntity: ScanEntity)

    /**
     * 已达到选择最大数
     * [GalleryConfigs.maxCount]
     */
    fun onClickItemMaxCount(context: Context, scanEntity: ScanEntity)

    /**
     * 点击CheckBox时该文件已经被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [PhotoViewHolder.photo]
     */
    fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity)

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * 这个方法优先级高于单选和视频播放，裁剪等功能
     */
    fun onClickItemFileNotExist(context: Context, scanEntity: ScanEntity)

    /**
     * 点击CheckBox时会触发
     * [PhotoViewHolder.photo]
     */
    fun onSelectItemChanged(position: Int, scanEntity: ScanEntity)

    /**
     * 刷新预览页数据之后触发
     */
    fun onRefreshResultChanged(selectCount: Int)

    /**
     * 如果图片存在,并且不是视频模式,不是单选的情况下触发这个方法
     * 可以跳转到预览页
     */
    fun onPhotoItemClick(
        context: Context,
        bundle: GalleryConfigs,
        scanEntity: ScanEntity,
        position: Int,
        parentId: Long
    )

    /**
     * 没有扫描到任何数据
     */
    fun onScanSuccessEmpty(context: Context)

    /**
     * 每次扫描之后数据非空触发
     * [scanEntities]已经过滤掉了相机Type,且是在Adapter数据已经合并完成之后调用
     */
    fun onScanSuccess(scanEntities: ArrayList<ScanEntity>)

    /**
     * 单个文件扫描成功
     */
    fun onResultSuccess(context: Context, scanEntity: ScanEntity)

    /**
     * 拍照或者摄像或者扫描单个数据失败
     */
    fun onResultError(context: Context, galleryBundle: GalleryConfigs)

    /**
     * 打开相机返回的状态
     * [CameraStatus.SUCCESS] 成功
     * [CameraStatus.ERROR] 失败
     * [CameraStatus.PERMISSION] 权限被拒
     */
    fun onCameraOpenStatus(context: Context, status: CameraStatus)

    /**
     * 取消拍照
     */
    fun onCameraCanceled(context: Context, galleryBundle: GalleryConfigs)

    /**
     * 视频播放异常
     */
    fun onOpenVideoPlayError(context: Context, scanEntity: ScanEntity)

    /**
     * 权限被拒
     * [PermissionCode.READ]
     * [PermissionCode.WRITE]
     */
    fun onPermissionsDenied(context: Context?, type: PermissionCode)

}