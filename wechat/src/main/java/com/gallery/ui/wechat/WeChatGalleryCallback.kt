package com.gallery.ui.wechat

import androidx.fragment.app.FragmentActivity
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryListener

interface WeChatGalleryCallback : GalleryListener {

    fun onWeChatGalleryResources(activity: FragmentActivity, entities: List<ScanEntity>, fullImage: Boolean)
}