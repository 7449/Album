package com.gallery.scan

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.Columns

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
 */
class ScanImpl(private val scanView: ScanView) : ViewModel(), Scan {

    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.scanContext)
    private val context: Context = scanView.scanContext.applicationContext
    private val scanType: Int = scanView.scanType()
    private val scanField: String = scanView.scanSortField()
    private val scanSort: String = scanView.scanSort()

    override fun scanParent(parentId: Long) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, Bundle().apply {
            putLong(Columns.PARENT, parentId)
            putString(Columns.SORT, scanSort)
            putString(Columns.SORT_FIELD, scanField)
            putInt(Columns.SCAN_TYPE, scanType)
        }, ScanTask(context) {
            scanView.scanSuccess(it)
            onCleared()
        })
    }

    override fun scanResult(id: Long) {
        loaderManager.restartLoader(SCAN_LOADER_ID, Bundle().apply {
            putLong(Columns.ID, id)
            putString(Columns.SORT, scanSort)
            putString(Columns.SORT_FIELD, scanField)
            putInt(Columns.SCAN_TYPE, scanType)
        }, ScanTask(context) {
            scanView.resultSuccess(if (it.isEmpty()) null else it[0])
            onCleared()
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }
}