package com.gallery.core.callback

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.text.safeToastExpand
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.delegate.adapter.GalleryAdapter.PhotoViewHolder
import com.gallery.core.delegate.impl.ScanDelegateImpl
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.PermissionCode
import com.gallery.core.extensions.isImageScanExpand
import com.gallery.core.extensions.isVideoScanExpand

interface IGalleryCallback {

    /**
     * [ScanDelegateImpl.onCreate]触发
     * 为[recyclerView]设置布局管理器
     * 必须实现
     */
    fun onGalleryCreated(fragment: Fragment, recyclerView: RecyclerView, galleryBundle: GalleryBundle, savedInstanceState: Bundle?)

    /**
     * 单选非裁剪状态下,点击[Adapter]item返回的那条数据
     * [ScanEntity.delegate]为获取的数据
     */
    fun onGalleryResource(context: Context, scanEntity: ScanEntity) {}

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickCheckBoxMaxCount(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_check_max).safeToastExpand(context)
    }

    /**
     * 点击CheckBox时该文件已经被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * 这里存在一个问题，在小米手机上发现了，小米手机云端未下载的图片也会扫描出来
     * 但是这个时候文件是不存在的..
     * [PhotoViewHolder.photo]
     */
    fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_file_deleted).safeToastExpand(context)
    }

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * 这个方法优先级高于单选和视频播放，裁剪等功能
     */
    fun onClickItemFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_item_file_deleted).safeToastExpand(context)
    }

    /**
     * 点击CheckBox时会触发
     * [PhotoViewHolder.photo]
     */
    fun onChangedCheckBox(position: Int, scanEntity: ScanEntity) {}

    /**
     * 刷新预览页数据之后触发
     */
    fun onChangedResultCount(selectCount: Int) {}

    /**
     * 如果图片存在,并且不是视频模式,不是单选的情况下触发这个方法
     * 可以跳转到预览页
     */
    fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {}

    /**
     * 每次扫描之后数据非空触发
     * [scanEntities]已经过滤掉了相机Type,且是在Adapter数据已经合并完成之后调用
     */
    fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {}

    /**
     * 单个文件扫描成功
     */
    fun onResultSuccess(context: Context?, scanEntity: ScanEntity) {}

    /**
     * 取消拍照
     */
    fun onCameraCanceled(context: Context?, galleryBundle: GalleryBundle) {
        if (galleryBundle.isVideoScanExpand) {
            context?.getString(R.string.gallery_video_canceled).safeToastExpand(context)
        } else if (galleryBundle.isImageScanExpand) {
            context?.getString(R.string.gallery_camera_canceled).safeToastExpand(context)
        }
    }

    /**
     * 拍照或者摄像或者扫描单个数据失败
     */
    fun onResultError(context: Context?, galleryBundle: GalleryBundle) {
        context?.getString(R.string.gallery_error).safeToastExpand(context)
    }

    /**
     * 打开相机返回的状态
     * [CameraStatus.SUCCESS] 成功
     * [CameraStatus.ERROR] 失败
     * [CameraStatus.PERMISSION] 权限被拒
     */
    fun onCameraOpenStatus(context: Context?, status: CameraStatus) {
        when (status) {
            CameraStatus.ERROR -> context?.getString(R.string.gallery_open_camera_error).safeToastExpand(context)
            CameraStatus.SUCCESS -> context?.getString(R.string.gallery_open_camera_success).safeToastExpand(context)
            CameraStatus.PERMISSION -> {
            }
        }
    }

    /**
     * 没有扫描到任何数据
     */
    fun onScanSuccessEmpty(context: Context?) {
        context?.getString(R.string.gallery_scan_success_empty).safeToastExpand(context)
    }

    /**
     * 视频播放异常
     */
    fun onOpenVideoPlayError(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_open_video_error).safeToastExpand(context)
    }

    /**
     * 权限被拒
     * [PermissionCode.READ]
     * [PermissionCode.WRITE]
     */
    fun onPermissionsDenied(context: Context?, type: PermissionCode) {
        context?.getString(R.string.gallery_permissions_denied).safeToastExpand(context)
    }
}