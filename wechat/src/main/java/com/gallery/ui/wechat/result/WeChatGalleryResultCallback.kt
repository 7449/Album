package com.gallery.ui.wechat.result

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import com.gallery.compat.GalleryConfig
import com.gallery.compat.extensions.getParcelableArrayListExpand
import com.gallery.compat.extensions.getParcelableExpand
import com.gallery.core.extensions.orEmptyExpand
import com.gallery.ui.wechat.WeChatConfig
import com.gallery.ui.wechat.extension.getBooleanExpand

class WeChatGalleryResultCallback(
    private val galleryListener: WeChatGalleryCallback,
) : ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundleExpand: Bundle = intent.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            GalleryConfig.RESULT_CODE_CROP -> galleryListener.onGalleryCropResource(
                bundleExpand.getParcelableExpand(
                    GalleryConfig.GALLERY_RESULT_CROP
                )
            )
            GalleryConfig.RESULT_CODE_SINGLE_DATA -> galleryListener.onGalleryResource(
                bundleExpand.getParcelableExpand(
                    GalleryConfig.GALLERY_SINGLE_DATA
                )
            )
            GalleryConfig.RESULT_CODE_MULTIPLE_DATA -> galleryListener.onWeChatGalleryResources(
                bundleExpand.getParcelableArrayListExpand(GalleryConfig.GALLERY_MULTIPLE_DATA),
                bundleExpand.getBooleanExpand(WeChatConfig.FULL_IMAGE)
            )
            GalleryConfig.RESULT_CODE_TOOLBAR_BACK, Activity.RESULT_CANCELED -> galleryListener.onGalleryCancel()
        }
    }
}