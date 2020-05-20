package com.gallery.core.expand

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.gallery.scan.SCAN_ALL
import com.gallery.scan.ScanEntity
import com.gallery.scan.args.Columns


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
        finderList.find { it.bucketDisplayName == "0" }?.let {
            finderList[finderList.indexOf(it)] = it.copy(bucketDisplayName = sdName)
        }
        finderList.find { it.parent.isScanAll() }?.let {
            finderList[finderList.indexOf(it)] = it.copy(bucketDisplayName = allName)
        }
    }
    return finderList
}

//裁剪或者拍照之后更新文件夹数据
fun ArrayList<ScanEntity>.updateResultFinder(scanEntity: ScanEntity) {
    if (isEmpty()) {
        return
    }
    val find: ScanEntity? = find { it.parent == scanEntity.parent }
    if (find == null) {
        this.add(1, scanEntity.copy(count = 1))
        val first: ScanEntity = first()
        this[indexOf(first)] = first.copy(scanEntity, SCAN_ALL, first.count + 1)
    } else {
        find { it.parent.isScanAll() }?.let {
            this[indexOf(it)] = it.copy(scanEntity, SCAN_ALL, count = it.count + 1)
        }
        find { it.parent == scanEntity.parent }?.let {
            this[indexOf(it)] = it.copy(scanEntity, scanEntity.parent, count = it.count + 1)
        }
    }
}

internal fun ScanEntity.copy(source: ScanEntity, parent: Long, count: Int): ScanEntity {
    return copy(
            id = source.id,
            size = source.size,
            duration = source.duration,
            mimeType = source.mimeType,
            displayName = source.displayName,
            orientation = source.orientation,
            bucketId = source.bucketId,
            bucketDisplayName = source.bucketDisplayName,
            mediaType = source.mediaType,
            width = source.width,
            height = source.height,
            dataModified = source.dataModified,
            isCheck = source.isCheck,
            parent = parent,
            count = count
    )
}