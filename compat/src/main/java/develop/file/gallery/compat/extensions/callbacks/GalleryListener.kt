package develop.file.gallery.compat.extensions.callbacks

import android.net.Uri
import develop.file.gallery.entity.ScanEntity

interface GalleryListener {
    /*** 裁剪成功*/
    fun onGalleryCropResource(uri: Uri, vararg args: Any) {}

    /*** 单选不裁剪*/
    fun onGalleryResource(entity: ScanEntity, vararg args: Any) {}

    /*** 选择图片*/
    fun onGalleryResources(entities: List<ScanEntity>, vararg args: Any) {}

    /*** 取消图片选择*/
    fun onGalleryCancel(vararg args: Any) {}
}