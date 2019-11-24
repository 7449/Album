package com.album.scan

import android.content.Context
import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.album.scan.args.Columns
import com.album.scan.args.ScanConst

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
 */
class ScanImpl(private val scanView: ScanView) {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.getScanContext())

    private val context: Context = scanView.getScanContext()

    private val scanType: Int = scanView.currentScanType()

    private var finderList = ArrayList<ScanEntity>()

    fun scanAll(parent: Long) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, Bundle().apply {
            putLong(Columns.PARENT, parent)
            putInt(Columns.SCAN_TYPE, scanType)
        }, ScanTask(context) {
            if (parent == ScanConst.ALL && !it.isNullOrEmpty()) {
                refreshFinder(it)
            }
            scanView.scanSuccess(it.mergeEntity(scanView.getSelectEntity()))
            scanView.scanFinderSuccess(finderList)
            loaderManager.destroyLoader(SCAN_LOADER_ID)
        })
    }

    fun scanResult(path: String) {
        loaderManager.restartLoader(SCAN_LOADER_ID, Bundle().apply {
            putString(Columns.DATA, path)
            putInt(Columns.SCAN_TYPE, scanType)
        }, ScanTask(context) {
            scanView.resultSuccess(if (it.isEmpty()) null else it[0])
            loaderManager.destroyLoader(SCAN_LOADER_ID)
        })
    }

    fun refreshResultFinder(finderList: ArrayList<ScanEntity>, scanEntity: ScanEntity) {
        finderList.find { it.parent == ScanConst.ALL }?.let {
            it.duration = scanEntity.duration
            it.path = scanEntity.path
            it.mediaType = scanEntity.mediaType
            it.mimeType = scanEntity.mimeType
            it.id = scanEntity.id
            it.count = it.count + 1
        }
        finderList.find { it.parent == scanEntity.parent }?.let {
            it.id = scanEntity.id
            it.path = scanEntity.path
            it.size = scanEntity.size
            it.duration = scanEntity.duration
            it.mimeType = scanEntity.mimeType
            it.displayName = scanEntity.displayName
            it.orientation = scanEntity.orientation
            it.bucketId = scanEntity.bucketId
            it.bucketDisplayName = scanEntity.bucketDisplayName
            it.mediaType = scanEntity.mediaType
            it.width = scanEntity.width
            it.height = scanEntity.height
            it.dataModified = scanEntity.dataModified
            it.count = it.count + 1
        }
    }

    private fun refreshFinder(list: ArrayList<ScanEntity>) {
        finderList.clear()
        list.forEach { item -> if (finderList.find { it.parent == item.parent } == null) finderList.add(item.apply { count = list.count { it.parent == item.parent } }) }
        val first = finderList.first()
        finderList.add(0, ScanEntity(
                parent = ScanConst.ALL,
                duration = first.duration,
                path = first.path,
                mediaType = first.mediaType,
                mimeType = first.mimeType,
                id = first.id,
                count = list.size))
    }
}