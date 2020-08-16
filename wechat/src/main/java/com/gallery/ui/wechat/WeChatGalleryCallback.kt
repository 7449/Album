package com.gallery.ui.wechat

import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryListener

interface WeChatGalleryCallback : GalleryListener {

    fun onWeChatGalleryResources(entities: List<ScanEntity>, fullImage: Boolean)
}