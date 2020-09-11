package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.safeToastExpand
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.ui.wechat.WeChatGalleryCallback

class WeChatGalleryCallback(private val activity: FragmentActivity) : WeChatGalleryCallback {

    override fun onGalleryCropResource(uri: Uri) {
        uri.toString().safeToastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanFileEntity) {
        scanEntity.toString().safeToastExpand(activity)
    }

    override fun onWeChatGalleryResources(entities: List<ScanFileEntity>, fullImage: Boolean) {
        ("$entities,fullImage:$fullImage").safeToastExpand(activity)
    }

    override fun onGalleryCancel() {
        "取消选择".safeToastExpand(activity)
    }

}