package com.gallery.ui.wechat

import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.ui.GalleryListener

interface WeChatGalleryCallback : GalleryListener {

    fun onWeChatGalleryResources(entities: List<ScanFileEntity>, fullImage: Boolean)
}