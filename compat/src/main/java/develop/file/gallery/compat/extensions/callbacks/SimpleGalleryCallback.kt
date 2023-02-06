package develop.file.gallery.compat.extensions.callbacks

import android.os.Bundle
import develop.file.gallery.args.PermissionType
import develop.file.gallery.callback.IGalleryCallback
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.CameraStatus

interface SimpleGalleryCallback : IGalleryCallback {

    override fun onGalleryCreated(delegate: IScanDelegate, saveState: Bundle?) {
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

    override fun onScanMultipleEmpty() {
    }

    override fun onOpenVideoError(entity: ScanEntity) {
    }

    override fun onPermissionsDenied(type: PermissionType) {
    }

}