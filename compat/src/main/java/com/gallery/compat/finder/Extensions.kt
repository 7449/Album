package com.gallery.compat.finder

import com.gallery.core.entity.ScanEntity
import com.gallery.scan.Types
import com.gallery.scan.extensions.isScanAllMedia

//获取当前页的文件夹数据
//目标List为扫描成功之后的数据，返回Finder数据
fun ArrayList<ScanEntity>.findFinder(sdName: String, allName: String): ArrayList<ScanEntity> {
    val finderList = ArrayList<ScanEntity>()
    forEach { item ->
        if (finderList.find { it.parent == item.parent } == null) {
            finderList.add(
                ScanEntity(
                    item.delegate,
                    count = this.count { it.parent == item.parent })
            )
        }
    }
    if (finderList.isNotEmpty()) {
        finderList.add(
            0,
            finderList.first().copy(
                delegate = finderList.first().delegate.copy(
                    parent = Types.Id.ALL,
                    bucketDisplayName = allName
                ), count = this.size
            )
        )
        //TODO(2021/5/22) 有的根目录是空....
        finderList.find { it.bucketDisplayName == "0" || it.bucketDisplayName.isEmpty() }?.let {
            finderList[finderList.indexOf(it)] =
                it.copy(delegate = it.delegate.copy(bucketDisplayName = sdName))
        }
    }
    return finderList
}

//裁剪或者拍照之后更新文件夹数据
//如果现有的文件夹数据找不到parent相同的数据则是一个新的文件夹
//添加数据并更新第一条数据
//否则更新第一条数据和文件夹数据
//sortDesc true 倒序排列
fun ArrayList<ScanEntity>.updateResultFinder(entity: ScanEntity, sortDesc: Boolean) {
    if (isEmpty()) return
    val find: ScanEntity? = find { it.parent == entity.parent }
    if (find == null) {
        this.add(1, ScanEntity(entity.delegate, 1)) //新文件夹，添加一条finder item 并更新第一条数据
        val first: ScanEntity = first() //第一条finder item , 全部数据的item
        this[0] = first.copy(
            delegate = (if (sortDesc) entity.delegate else first.delegate).copy(
                parent = Types.Id.ALL,
                bucketDisplayName = first.bucketDisplayName
            ),
            count = first.count + 1
        )
    } else {
        find { it.parent.isScanAllMedia }?.let {
            this[indexOf(it)] = it.copy(
                delegate = (if (sortDesc) entity.delegate else it.delegate).copy(
                    parent = Types.Id.ALL,
                    bucketDisplayName = it.bucketDisplayName
                ),
                count = it.count + 1
            )
        }
        find { it.parent == entity.parent }?.let {
            this[indexOf(it)] = it.copy(
                delegate = if (sortDesc) entity.delegate else it.delegate,
                count = it.count + 1
            )
        }
    }
}