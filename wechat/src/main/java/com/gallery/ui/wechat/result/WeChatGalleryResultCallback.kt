package com.gallery.ui.wechat.result

import android.os.Bundle
import com.gallery.compat.GalleryConfig
import com.gallery.compat.extensions.parcelableArrayList
import com.gallery.compat.internal.call.GalleryListener
import com.gallery.compat.internal.call.GalleryResultCallback
import com.gallery.ui.wechat.WeChatConfig

class WeChatGalleryResultCallback(
    private val galleryListener: GalleryListener,
) : GalleryResultCallback(galleryListener) {

    override fun onMultipleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResources(
            bundle.parcelableArrayList(GalleryConfig.GALLERY_MULTIPLE_DATA),
            bundle.getBoolean(WeChatConfig.FULL_IMAGE)
        )
    }

}