@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.loader.app.LoaderManager
import com.album.core.index
import com.album.core.scan.task.AlbumScanFileTask
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
                if (parent == AlbumScan.ALL_PARENT) {
                    refreshFinder(it)
                }
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
                    albumView.resultSuccess(if (it.isEmpty()) null else it[0])
                    destroyResultLoaderManager()
                })
    }

    fun refreshResultFinder(list: ArrayList<AlbumEntity>, albumEntity: AlbumEntity) {
        list.forEach {
            if (it.parent == albumEntity.parent) {
                it.id = albumEntity.id
                it.path = albumEntity.path
                it.size = albumEntity.size
                it.duration = albumEntity.duration
                it.mimeType = albumEntity.mimeType
                it.displayName = albumEntity.displayName
                it.orientation = albumEntity.orientation
                it.bucketId = albumEntity.bucketId
                it.bucketDisplayName = albumEntity.bucketDisplayName
                it.mediaType = albumEntity.mediaType
                it.width = albumEntity.width
                it.height = albumEntity.height
                it.dataModified = albumEntity.dataModified
                it.count = it.count + 1
            }
        }
        val allEntity = list.find { it.parent == AlbumScan.ALL_PARENT }
        allEntity?.let {
            it.count = it.count + 1
            it.path = albumEntity.path
            it.duration = albumEntity.duration
            it.mediaType = albumEntity.mediaType
            it.mimeType = albumEntity.mimeType
            it.id = albumEntity.id
        }
    }

    private fun refreshFinder(list: ArrayList<AlbumEntity>) {
        if (list.isEmpty()) {
            return
        }
        val finderList = ArrayList<AlbumEntity>()
        list.forEach { item ->
            if (finderList.index { it.parent == item.parent } == -1) {
                finderList.add(AlbumEntity(
                        item.id,
                        item.path,
                        item.size,
                        item.duration,
                        item.parent,
                        item.mimeType,
                        item.displayName,
                        item.orientation,
                        item.bucketId,
                        item.bucketDisplayName,
                        item.mediaType,
                        item.width,
                        item.height,
                        item.dataModified,
                        list.count { it.parent == item.parent },
                        false))
            }
        }
        val first = finderList.first()
        finderList.add(0, AlbumEntity(
                parent = AlbumScan.ALL_PARENT,
                duration = first.duration,
                path = first.path,
                mediaType = first.mediaType,
                mimeType = first.mimeType,
                id = first.id,
                count = list.size))
        albumView.scanFinderSuccess(finderList)
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
}