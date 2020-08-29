package com.gallery.core.callback

import android.content.Context
import android.os.Bundle
import androidx.kotlin.expand.text.toastExpand
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.expand.CameraStatus
import com.gallery.core.expand.PermissionCode
import com.gallery.core.expand.isVideoScan
import com.gallery.core.ui.adapter.vh.PhotoViewHolder
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.scan.ScanEntity

interface IGalleryCallback {

    /**
     * [ScanFragment.onViewCreated]触发
     */
    fun onGalleryViewCreated(savedInstanceState: Bundle?) {}

    /**
     * 单选状态下,点击[Adapter]item返回的那条数据
     */
    fun onGalleryResource(context: Context, scanEntity: ScanEntity)

    /**
     * 已达到选择最大数
     * [GalleryBundle.multipleMaxCount]
     */
    fun onClickCheckBoxMaxCount(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_check_max).toastExpand(context)
    }

    /**
     * 点击CheckBox时该文件已经被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * [PhotoViewHolder.photo]
     */
    fun onClickCheckBoxFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_file_deleted).toastExpand(context)
    }

    /**
     * 点击图片时该文件已被删除
     * 适用场景:在图片选择页面返回桌面打开相册删除某张图片
     * 这个方法优先级高于单选和视频播放，裁剪等功能
     */
    fun onClickItemFileNotExist(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_item_file_deleted).toastExpand(context)
    }

    /**
     * 点击CheckBox时会触发
     * [PhotoViewHolder.photo]
     */
    fun onChangedCheckBox(position: Int, isSelect: Boolean, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {}

    /**
     * 刷新预览页数据之后触发
     */
    fun onChangedResultCount(selectCount: Int) {}

    /**
     * 如果图片存在,并且不是视频模式,不是单选的情况下触发这个方法
     * 可以跳转到预览页
     */
    fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long)

    /**
     * 每次扫描之后数据非空触发
     */
    fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {}

    /**
     * 单个文件扫描成功
     */
    fun onResultSuccess(context: Context?, galleryBundle: GalleryBundle, scanEntity: ScanEntity) {}

    /**
     * 取消拍照
     */
    fun onCameraCanceled(context: Context?, galleryBundle: GalleryBundle) {
        context ?: return
        if (galleryBundle.isVideoScan) {
            context.getString(R.string.gallery_video_canceled).toastExpand(context)
        } else {
            context.getString(R.string.gallery_camera_canceled).toastExpand(context)
        }
    }

    /**
     * 拍照或者摄像或者扫描单个数据失败
     */
    fun onResultError(context: Context?, galleryBundle: GalleryBundle) {
        context ?: return
        context.getString(R.string.gallery_error).toastExpand(context)
    }

    /**
     * 打开相机返回的状态
     * [CameraStatus.SUCCESS] 成功
     * [CameraStatus.ERROR] 失败
     * [CameraStatus.PERMISSION] 权限被拒
     */
    fun onCameraOpenStatus(context: Context?, status: CameraStatus, galleryBundle: GalleryBundle) {
        context ?: return
        when (status) {
            CameraStatus.ERROR -> context.getString(R.string.gallery_open_camera_error).toastExpand(context)
            CameraStatus.SUCCESS -> context.getString(R.string.gallery_open_camera_success).toastExpand(context)
            CameraStatus.PERMISSION -> {
            }
        }
    }

    /**
     * 没有扫描到任何数据
     */
    fun onScanSuccessEmpty(context: Context?, galleryBundle: GalleryBundle) {
        context ?: return
        context.getString(R.string.gallery_scan_success_empty).toastExpand(context)
    }

    /**
     * 视频播放异常
     */
    fun onOpenVideoPlayError(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_open_video_error).toastExpand(context)
    }

    /**
     * 权限被拒
     * [PermissionCode.READ]
     * [PermissionCode.WRITE]
     */
    fun onPermissionsDenied(context: Context?, type: PermissionCode) {
        context ?: return
        context.getString(R.string.gallery_permissions_denied).toastExpand(context)
    }
}