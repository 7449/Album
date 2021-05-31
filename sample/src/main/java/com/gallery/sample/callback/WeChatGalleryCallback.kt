package com.gallery.sample.callback

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.internal.call.GalleryListener
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.safeToastExpand

class WeChatGalleryCallback(private val activity: FragmentActivity) : GalleryListener {

    override fun onGalleryCropResource(uri: Uri, vararg args: Any) {
        uri.toString().safeToastExpand(activity)
    }

    override fun onGalleryResource(scanEntity: ScanEntity, vararg args: Any) {
        scanEntity.toString().safeToastExpand(activity)
    }

    override fun onGalleryResources(entities: List<ScanEntity>, vararg args: Any) {
        ("$entities,fullImage:${args[0]}").safeToastExpand(activity)
    }

    override fun onGalleryCancel(vararg args: Any) {
        "取消选择".safeToastExpand(activity)
    }

}