package com.gallery.ui.wechat

import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.ui.GalleryListener

interface WeChatGalleryCallback : GalleryListener {

    fun onWeChatGalleryResources(entities: List<ScanMinimumEntity>, fullImage: Boolean)
}