package com.gallery.core.expand

import com.gallery.core.delegate.ScanEntity
import com.gallery.scan.types.SCAN_ALL
import com.gallery.scan.types.isScanAllExpand
import org.jetbrains.annotations.TestOnly

//获取当前页的文件夹数据
//目标List为扫描成功之后的数据，返回Finder数据
fun ArrayList<ScanEntity>.findFinder(sdName: String, allName: String): ArrayList<ScanEntity> {
    val finderList = ArrayList<ScanEntity>()
    forEach { item ->
        if (finderList.find { it.parent == item.parent } == null) {
            finderList.add(ScanEntity(item.delegate, count = this.count { it.parent == item.parent }))
        }
    }
    if (finderList.isNotEmpty()) {
        finderList.add(0, finderList.first().copy(delegate = finderList.first().delegate.copy(parent = SCAN_ALL, bucketDisplayName = allName), count = this.size))
        finderList.find { it.bucketDisplayName == "0" }?.let {
            finderList[finderList.indexOf(it)] = it.copy(delegate = it.delegate.copy(bucketDisplayName = sdName))
        }
    }
    return finderList
}

//裁剪或者拍照之后更新文件夹数据
//如果现有的文件夹数据找不到parent相同的数据则是一个新的文件夹
//添加数据并更新第一条数据
//否则更新第一条数据和文件夹数据
//sortDesc true 倒序排列
@TestOnly
fun ArrayList<ScanEntity>.updateResultFinder(scanEntity: ScanEntity, sortDesc: Boolean) {
    if (isEmpty()) {
        return
    }
    val find: ScanEntity? = find { it.parent == scanEntity.parent }
    if (find == null) {
        this.add(1, ScanEntity(scanEntity.delegate, 1))
        val first: ScanEntity = first()
        this[indexOf(first)] = first.copy(delegate = (if (sortDesc) scanEntity.delegate else first.delegate).copy(parent = SCAN_ALL), count = first.count + 1)
    } else {
        find { it.parent.isScanAllExpand() }?.let {
            this[indexOf(it)] = it.copy(delegate = (if (sortDesc) scanEntity.delegate else it.delegate).copy(parent = SCAN_ALL), count = it.count + 1)
        }
        find { it.parent == scanEntity.parent }?.let {
            this[indexOf(it)] = it.copy(delegate = if (sortDesc) scanEntity.delegate else it.delegate, count = it.count + 1)
        }
    }
}