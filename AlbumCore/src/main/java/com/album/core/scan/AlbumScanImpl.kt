@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.content.Context
import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.album.core.scan.task.AlbumScanFileTask
import com.album.core.scan.task.AlbumScanFinderTask
import com.album.core.view.AlbumView
import com.album.core.view.AlbumViewKt

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
 */
class AlbumScanImpl private constructor(private val albumView: AlbumView) {

    companion object {
        @JvmStatic
        fun newInstance(albumView: AlbumView) = AlbumScanImpl(albumView)

        fun newInstance(albumViewKt: AlbumViewKt.() -> Unit) = newInstance(AlbumViewKt().also(albumViewKt).build())
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(albumView.getAlbumContext())

    private val activity: Context = albumView.getAlbumContext()

    private val scanType: Int = albumView.currentScanType()

    private val scanCount: Int = albumView.getScanCount()

    fun scanAll(parent: Long, page: Int) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        albumView.showProgress()
        loaderManager.restartLoader(AlbumScan.ALBUM_LOADER_ID, Bundle().apply {
            putLong(AlbumColumns.PARENT, parent)
            putInt(AlbumColumns.PAGE, page)
            putInt(AlbumColumns.COUNT, scanCount)
            putInt(AlbumColumns.SCAN_TYPE, scanType)
        }, AlbumScanFileTask.newInstance(activity) {
            albumView.hideProgress()
            if (it.isEmpty()) {
                albumView.onAlbumScanCallback(if (albumView.getPage() == 0) AlbumScan.SCAN_EMPTY else AlbumScan.SCAN_NO_MORE)
            } else {
                refreshFinder()
                mergeEntity(it, albumView.getSelectEntity())
                albumView.scanSuccess(it)
            }
            destroyAlbumLoaderManager()
        })
    }

    fun scanResult(path: String) {
        loaderManager.restartLoader(
                AlbumScan.RESULT_LOADER_ID,
                Bundle().apply {
                    putString(AlbumColumns.DATA, path)
                    putInt(AlbumColumns.SCAN_TYPE, scanType)
                    putInt(AlbumColumns.COUNT, scanCount)
                },
                AlbumScanFileTask.newInstance(activity) {
                    refreshFinder()
                    albumView.resultSuccess(if (it.isEmpty()) null else it[0])
                    destroyResultLoaderManager()
                })
    }

    private fun refreshFinder() {
        loaderManager.restartLoader(AlbumScan.FINDER_LOADER_ID, Bundle().apply { putInt(AlbumColumns.SCAN_TYPE, scanType) },
                AlbumScanFinderTask.newInstance(activity) {
                    albumView.scanFinderSuccess(it)
                    destroyFinderLoaderManager()
                })
    }

    fun mergeEntity(albumList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>) {
        albumList.forEach { it.isCheck = false }
        selectEntity.forEach { select -> albumList.find { it.path == select.path }?.isCheck = true }
    }

    private fun destroyAlbumLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.ALBUM_LOADER_ID)
    }

    private fun destroyResultLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.RESULT_LOADER_ID)
    }

    private fun destroyFinderLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.FINDER_LOADER_ID)
    }
}