package com.gallery.ui.wechat

import com.gallery.core.delegate.entity.ScanEntity
import com.gallery.ui.result.GalleryListener

interface WeChatGalleryCallback : GalleryListener {

    fun onWeChatGalleryResources(entities: List<ScanEntity>, fullImage: Boolean)
}