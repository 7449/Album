package com.album.scan

import android.content.ContentUris
import android.provider.MediaStore

fun ScanEntity.uri() = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

fun ArrayList<ScanEntity>.mergeEntity(selectEntity: ArrayList<ScanEntity>) = apply {
    forEach { it.isCheck = false }
    selectEntity.forEach { select -> this.find { it.path == select.path }?.isCheck = true }
}