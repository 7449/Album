package com.gallery.compat.internal.simple

import android.content.Context
import android.os.Bundle
import com.gallery.compat.R
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.PermissionCode
import com.gallery.core.extensions.safeToastExpand

interface SimpleGalleryCallback : IGalleryCallback {

    override fun onGalleryCreated(
            delegate: IScanDelegate,
            bundle: GalleryBundle,
            savedInstanceState: Bundle?
    ) {
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
    }

    override fun onClickItemMaxCount(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_check_max).safeToastExpand(context)
    }

    override fun onClickCheckBoxFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_file_deleted).safeToastExpand(context)
    }

    override fun onClickItemFileNotExist(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_item_file_deleted).safeToastExpand(context)
    }

    override fun onChangedItem(position: Int, scanEntity: ScanEntity) {
    }

    override fun onChangedResultCount(selectCount: Int) {
    }

    override fun onPhotoItemClick(
            context: Context,
            bundle: GalleryBundle,
            scanEntity: ScanEntity,
            position: Int,
            parentId: Long
    ) {
    }

    override fun onScanSuccess(scanEntities: ArrayList<ScanEntity>) {
    }

    override fun onResultSuccess(context: Context?, scanEntity: ScanEntity) {
    }

    override fun onCameraCanceled(context: Context?, galleryBundle: GalleryBundle) {
        if (galleryBundle.isVideoScanExpand) {
            context?.getString(R.string.gallery_compat_video_canceled).safeToastExpand(context)
        } else if (galleryBundle.isImageScanExpand) {
            context?.getString(R.string.gallery_compat_camera_canceled).safeToastExpand(context)
        }
    }

    override fun onResultError(context: Context?, galleryBundle: GalleryBundle) {
        context?.getString(R.string.gallery_compat_error).safeToastExpand(context)
    }

    override fun onCameraOpenStatus(context: Context?, status: CameraStatus) {
        when (status) {
            CameraStatus.ERROR -> context?.getString(R.string.gallery_compat_open_camera_error).safeToastExpand(context)
            CameraStatus.SUCCESS -> context?.getString(R.string.gallery_compat_open_camera_success).safeToastExpand(context)
            CameraStatus.PERMISSION -> {
            }
        }
    }

    override fun onScanSuccessEmpty(context: Context?) {
        context?.getString(R.string.gallery_compat_scan_success_empty).safeToastExpand(context)
    }

    override fun onOpenVideoPlayError(context: Context, scanEntity: ScanEntity) {
        context.getString(R.string.gallery_compat_open_video_error).safeToastExpand(context)
    }

    override fun onPermissionsDenied(context: Context?, type: PermissionCode) {
        context?.getString(R.string.gallery_compat_permissions_denied).safeToastExpand(context)
    }

}