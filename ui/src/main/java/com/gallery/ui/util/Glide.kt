package com.gallery.ui.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.core.ext.externalUri
import com.gallery.core.ui.widget.GalleryImageView
import com.gallery.scan.ScanEntity
import com.gallery.ui.R
import com.gallery.ui.widget.WeChatGalleryItem
import java.text.SimpleDateFormat

private val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.ic_gallery_default_loading).error(R.drawable.ic_gallery_default_loading).centerCrop()

@SuppressLint("SimpleDateFormat")
private val formatter = SimpleDateFormat("mm:ss")

internal fun FrameLayout.displayGalleryWeChat(width: Int, height: Int, selectAll: ArrayList<ScanEntity>, galleryEntity: ScanEntity, selectView: TextView) {
    removeAllViews()
    val weChatGalleryItem = WeChatGalleryItem(context)
    weChatGalleryItem.update(galleryEntity)
    selectView.gravity = Gravity.CENTER
    selectView.setTextColor(Color.WHITE)
    if (galleryEntity.isCheck) {
        selectView.text = (selectAll.indexOf(galleryEntity) + 1).toString()
    } else {
        selectView.text = ""
    }
    Glide.with(context).asBitmap().load(galleryEntity.externalUri()).apply(requestOptions.override(width, height)).into(weChatGalleryItem.imageView)
    addView(weChatGalleryItem, FrameLayout.LayoutParams(width, height))
}

internal fun Long.formatTime(): String {
    if (toInt() == 0) {
        return "--:--"
    }
    val format = formatter.format(this)
    if (!format.startsWith("0")) {
        return format
    }
    return format.substring(1)
}

internal fun FrameLayout.displayGallery(width: Int, height: Int, galleryEntity: ScanEntity) {
    removeAllViews()
    val imageView = GalleryImageView(context)
    Glide.with(context).load(galleryEntity.externalUri()).apply(requestOptions.override(width, height)).into(imageView)
    addView(imageView, FrameLayout.LayoutParams(width, height))
}

internal fun FrameLayout.displayGalleryThumbnails(finderEntity: ScanEntity) {
    removeAllViews()
    val imageView = GalleryImageView(context)
    Glide.with(context).load(finderEntity.externalUri()).apply(requestOptions).into(imageView)
    addView(imageView)
}

internal fun FrameLayout.displayGalleryPrev(scanEntity: ScanEntity) {
    removeAllViews()
    val imageView = GalleryImageView(context)
    Glide.with(context).load(scanEntity.externalUri()).into(imageView)
    addView(imageView)
}