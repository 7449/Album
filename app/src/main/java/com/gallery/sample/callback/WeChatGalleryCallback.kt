package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.safeToastExpand
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.wechat.result.WeChatGalleryCallback

class WeChatGalleryCallback(private val activity: FragmentActivity) : WeChatGalleryCallback {

    override fun onGalleryCropResource(uri: Uri) {
        uri.toString().safeToastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanEntity) {
        scanEntity.toString().safeToastExpand(activity)
    }

    override fun onWeChatGalleryResources(entities: List<ScanEntity>, fullImage: Boolean) {
        ("$entities,fullImage:$fullImage").safeToastExpand(activity)
    }

    override fun onGalleryCancel() {
        "取消选择".safeToastExpand(activity)
    }

}