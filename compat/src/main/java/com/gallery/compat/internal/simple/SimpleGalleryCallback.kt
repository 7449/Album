package com.gallery.compat.internal.simple

import android.os.Bundle
import com.gallery.core.GalleryConfigs
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.CameraStatus
import com.gallery.core.extensions.PermissionCode

interface SimpleGalleryCallback : IGalleryCallback {

    override fun onGalleryCreated(
        delegate: IScanDelegate,
        configs: GalleryConfigs,
        saveState: Bundle?
    ) {
    }

    override fun onGalleryResource(entity: ScanEntity) {
    }

    override fun onSelectMultipleMaxCount() {
    }

    override fun onSelectMultipleFileNotExist(entity: ScanEntity) {
    }

    override fun onClickItemFileNotExist(entity: ScanEntity) {
    }

    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
    }

    override fun onRefreshResultChanged() {
    }

    override fun onPhotoItemClick(entity: ScanEntity, position: Int, parentId: Long) {
    }

    override fun onScanMultipleSuccess() {
    }

    override fun onScanSingleSuccess(entity: ScanEntity) {
    }

    override fun onCameraCanceled() {
    }

    override fun onScanSingleFailure() {
    }

    override fun onCameraOpenStatus(status: CameraStatus) {
    }

    override fun onScanMultipleResultEmpty() {
    }

    override fun onOpenVideoError(entity: ScanEntity) {
    }

    override fun onPermissionsDenied(type: PermissionCode) {
    }

}