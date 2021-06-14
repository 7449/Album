package com.gallery.ui.wechat.result

import android.os.Bundle
import com.gallery.compat.GalleryConfig
import com.gallery.compat.extensions.parcelableArrayListExpand
import com.gallery.compat.internal.call.GalleryListener
import com.gallery.compat.internal.call.GalleryResultCallback
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.extension.getBooleanExpand

class WeChatGalleryResultCallback(
    private val galleryListener: GalleryListener,
) : GalleryResultCallback(galleryListener) {

    override fun onMultipleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResources(
            bundle.parcelableArrayListExpand(GalleryConfig.GALLERY_MULTIPLE_DATA),
            bundle.getBooleanExpand(WeChatConfig.FULL_IMAGE)
        )
    }

}