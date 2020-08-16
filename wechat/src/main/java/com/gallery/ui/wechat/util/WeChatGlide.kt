package com.gallery.ui.wechat.util

import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.expand.externalUri
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.R
import com.gallery.ui.wechat.widget.WeChatGalleryItem
import com.gallery.ui.wechat.widget.WeChatPrevItem
import com.gallery.ui.wechat.widget.WeChatSelectItem

private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

internal fun FrameLayout.displayGalleryWeChat(width: Int, height: Int, selectAll: ArrayList<ScanEntity>, galleryEntity: ScanEntity, selectView: TextView) {
    removeAllViews()
    val weChatGalleryItem = WeChatGalleryItem(context)
    weChatGalleryItem.update(galleryEntity)
    selectView.gravity = Gravity.CENTER
    selectView.setTextColor(Color.WHITE)
    if (galleryEntity.isSelected) {
        selectView.text = (selectAll.indexOf(galleryEntity) + 1).toString()
    } else {
        selectView.text = ""
    }
    Glide.with(context).asBitmap().load(galleryEntity.externalUri()).apply(requestOptions.override(width, height)).into(weChatGalleryItem.imageView)
    addView(weChatGalleryItem, FrameLayout.LayoutParams(width, height))
}

internal fun FrameLayout.displayGalleryThumbnails(finderEntity: ScanEntity) {
    removeAllViews()
    val imageView = GalleryImageView(context)
    Glide.with(context).asBitmap().load(finderEntity.externalUri()).apply(requestOptions).into(imageView)
    addView(imageView)
}

internal fun FrameLayout.displayGalleryPrev(scanEntity: ScanEntity) {
    removeAllViews()
    val weChatPrevItem = WeChatPrevItem(context)
    weChatPrevItem.update(scanEntity)
    Glide.with(context).load(scanEntity.externalUri()).into(weChatPrevItem.imageView)
    addView(weChatPrevItem)
}

internal fun FrameLayout.displayGalleryPrevSelect(scanEntity: ScanEntity, idList: List<Long>, isPrev: Boolean) {
    removeAllViews()
    val weChatSelectItem = WeChatSelectItem(context)
    weChatSelectItem.update(scanEntity, idList, isPrev)
    Glide.with(context).asBitmap().load(scanEntity.externalUri()).apply(requestOptions.fitCenter()).into(weChatSelectItem.imageView)
    addView(weChatSelectItem)
}