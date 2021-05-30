package com.gallery.ui.wechat.result

import android.net.Uri
import com.gallery.core.entity.ScanEntity

interface WeChatGalleryCallback {
    /**
     * 裁剪成功
     */
    fun onGalleryCropResource(uri: Uri) {}

    /**
     * 单选不裁剪
     */
    fun onGalleryResource(scanEntity: ScanEntity) {}

    /**
     * 选择图片
     */
    fun onWeChatGalleryResources(entities: List<ScanEntity>, fullImage: Boolean)

    /**
     * 取消图片选择
     */
    fun onGalleryCancel() {}

}