package com.gallery.core.ext

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
    finderList.add(0, finderList.first().copy(parent = SCAN_ALL, count = this.size))
    finderList.find { it.bucketDisplayName == "0" }?.bucketDisplayName = sdName
    finderList.find { it.parent.isScanAll() }?.bucketDisplayName = allName
    Log.i("findFinder", this.size.toString() + "  " + finderList.toString())
    return finderList
}