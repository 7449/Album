package com.gallery.core.ext

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.Columns

//是否是扫描全部的Id
fun Long.isScanAll() = this == SCAN_ALL

//获取文件的Uri
fun ScanEntity.externalUri(): Uri {
    return if (mediaType == Columns.IMAGE) {
        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    } else {
        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
    }
}

//获取当前页的文件夹数据
fun ArrayList<ScanEntity>.findFinder(sdName: String, allName: String): ArrayList<ScanEntity> {
    val finderList = ArrayList<ScanEntity>()
    this.forEach { item ->
        if (finderList.find { it.parent == item.parent } == null) {
            finderList.add(item.copy(count = this.count { it.parent == item.parent }))
        }
    }
    if (finderList.isNotEmpty()) {
        finderList.add(0, finderList.first().copy(parent = SCAN_ALL, count = this.size))
        finderList.find { it.bucketDisplayName == "0" }?.bucketDisplayName = sdName
        finderList.find { it.parent.isScanAll() }?.bucketDisplayName = allName
    }
    return finderList
}

//裁剪或者拍照之后更新文件夹数据
fun ArrayList<ScanEntity>.updateResultFinder(parentId: Long, scanEntity: ScanEntity) {
    if (isEmpty()) {
        return
    }
    val find = this.find { it.parent == scanEntity.parent }
    if (find == null) {
        this.add(1, scanEntity.copy(count = 1))
        this.first().also {
            it.id = scanEntity.id
            it.size = scanEntity.size
            it.duration = scanEntity.duration
            it.parent = SCAN_ALL
            it.mimeType = scanEntity.mimeType
            it.displayName = scanEntity.displayName
            it.orientation = scanEntity.orientation
            it.bucketId = scanEntity.bucketId
            it.bucketDisplayName = scanEntity.bucketDisplayName
            it.mediaType = scanEntity.mediaType
            it.width = scanEntity.width
            it.height = scanEntity.height
            it.dataModified = scanEntity.dataModified
            it.count += 1
            it.isCheck = scanEntity.isCheck
        }
    } else {
        this.forEach {
            if (it.parent.isScanAll() || it.parent == scanEntity.parent) {
                it.id = scanEntity.id
                it.size = scanEntity.size
                it.duration = scanEntity.duration
                it.parent = if (it.parent.isScanAll()) SCAN_ALL else scanEntity.parent
                it.mimeType = scanEntity.mimeType
                it.displayName = scanEntity.displayName
                it.orientation = scanEntity.orientation
                it.bucketId = scanEntity.bucketId
                it.bucketDisplayName = scanEntity.bucketDisplayName
                it.mediaType = scanEntity.mediaType
                it.width = scanEntity.width
                it.height = scanEntity.height
                it.dataModified = scanEntity.dataModified
                it.count += 1
                it.isCheck = scanEntity.isCheck
            }
        }
    }
}