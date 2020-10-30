package com.gallery.ui.wechat

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.kotlin.expand.os.getBooleanExpand
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableExpand
import androidx.kotlin.expand.os.orEmptyExpand
import com.gallery.compat.Config

class WeChatGalleryResultCallback(
        private val galleryListener: WeChatGalleryCallback,
) : ActivityResultCallback<ActivityResult> {

    override fun onActivityResult(intent: ActivityResult) {
        val bundleExpand: Bundle = intent.data?.extras.orEmptyExpand()
        when (intent.resultCode) {
            Config.RESULT_CODE_CROP -> galleryListener.onGalleryCropResource(bundleExpand.getParcelableExpand(Config.GALLERY_RESULT_CROP))
            Config.RESULT_CODE_SINGLE_DATA -> galleryListener.onGalleryResource(bundleExpand.getParcelableExpand(Config.GALLERY_SINGLE_DATA))
            Config.RESULT_CODE_MULTIPLE_DATA -> galleryListener.onWeChatGalleryResources(bundleExpand.getParcelableArrayListExpand(Config.GALLERY_MULTIPLE_DATA), bundleExpand.getBooleanExpand(WeChatUiResult.GALLERY_WE_CHAT_RESULT_FULL_IMAGE))
            Config.RESULT_CODE_TOOLBAR_BACK, Activity.RESULT_CANCELED -> galleryListener.onGalleryCancel()
        }
    }
}