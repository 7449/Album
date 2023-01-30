package com.gallery.compat.internal.simple

import android.content.Context
import android.os.Bundle
import com.gallery.compat.R
import com.gallery.core.GalleryConfigs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.PermissionCode
import com.gallery.core.extensions.toast

interface SimpleGalleryCallback : IGalleryCallback {

    override fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    ) {
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
    }

    override fun onClickItemMaxCount(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_check_max).toast(context)
    }

    override fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_file_deleted).toast(context)
    }

    override fun onClickItemFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_item_file_deleted).toast(context)
    }

    override fun onSelectItemChanged(position: Int, scanEntity: ScanEntity) {
    }

    override fun onRefreshResultChanged(selectCount: Int) {
    }

    override fun onPhotoItemClick(
        context: Context,
        bundle: GalleryConfigs,
        scanEntity: ScanEntity,
        position: Int,
        parentId: Long
    ) {
    }

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
    }

    override fun onResultSuccess(context: Context, scanEntity: ScanEntity) {
    }

    override fun onCameraCanceled(context: Context, galleryBundle: GalleryConfigs) {
        if (galleryBundle.isScanVideoMedia) {
            context.getString(R.string.gallery_compat_video_canceled).toast(context)
        } else if (galleryBundle.isScanImageMedia) {
            context.getString(R.string.gallery_compat_camera_canceled).toast(context)
        }
    }

    override fun onResultError(context: Context, galleryBundle: GalleryConfigs) {
        context.getString(R.string.gallery_compat_error).toast(context)
    }

    override fun onCameraOpenStatus(context: Context, status: CameraStatus) {
        when (status) {
            CameraStatus.ERROR -> context.getString(R.string.gallery_compat_open_camera_error)
                .toast(context)

            CameraStatus.SUCCESS -> context.getString(R.string.gallery_compat_open_camera_success)
                .toast(context)

            CameraStatus.PERMISSION -> {
            }
        }
    }

    override fun onScanSuccessEmpty(context: Context) {
        context.getString(R.string.gallery_compat_scan_success_empty).toast(context)
    }

    override fun onOpenVideoPlayError(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_open_video_error).toast(context)
    }

    override fun onPermissionsDenied(context: Context?, type: PermissionCode) {
        context?.getString(R.string.gallery_compat_permissions_denied).toast(context)
    }

}