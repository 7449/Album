package com.gallery.ui.wechat.result

import com.gallery.core.entity.ScanEntity
import com.gallery.ui.result.GalleryListener

interface WeChatGalleryCallback : GalleryListener {

    fun onWeChatGalleryResources(entities: List<ScanEntity>, fullImage: Boolean)
    
}