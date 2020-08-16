package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.toastExpand
import com.gallery.scan.ScanEntity
import com.gallery.ui.wechat.WeChatGalleryCallback

class WeChatGalleryCallback(private val activity: FragmentActivity) : WeChatGalleryCallback {

    override fun onGalleryCropResource(uri: Uri) {
        uri.toString().toastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanEntity) {
        scanEntity.toString().toastExpand(activity)
    }

    override fun onWeChatGalleryResources(entities: List<ScanEntity>, fullImage: Boolean) {
        ("$entities,fullImage:$fullImage").toastExpand(activity)
    }

    override fun onGalleryCancel() {
        "取消选择".toastExpand(activity)
    }

}