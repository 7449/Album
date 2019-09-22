@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.content.Context
import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.album.core.AlbumScanConst
import com.album.core.view.AlbumView

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
 */
class AlbumScanImpl(private val albumView: AlbumView) {

    companion object {
        private const val ALBUM_LOADER_ID = 111
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(albumView.getAlbumContext())

    private val activity: Context = albumView.getAlbumContext()

    private val scanType: Int = albumView.currentScanType()

    private var finderList = ArrayList<AlbumEntity>()

    fun scanAll(parent: Long) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(ALBUM_LOADER_ID, Bundle().apply {
            putLong(AlbumColumns.PARENT, parent)
            putInt(AlbumColumns.SCAN_TYPE, scanType)
        }, AlbumScanFileTask(activity) {
            if (parent == AlbumScanConst.ALL && !it.isNullOrEmpty()) {
                refreshFinder(it)
            }
            albumView.scanSuccess(it.mergeEntity(albumView.getSelectEntity()))
            albumView.scanFinderSuccess(finderList)
            loaderManager.destroyLoader(ALBUM_LOADER_ID)
        })
    }

    fun scanResult(path: String) {
        loaderManager.restartLoader(ALBUM_LOADER_ID, Bundle().apply {
            putString(AlbumColumns.DATA, path)
            putInt(AlbumColumns.SCAN_TYPE, scanType)
        }, AlbumScanFileTask(activity) {
            albumView.resultSuccess(if (it.isEmpty()) null else it[0])
            loaderManager.destroyLoader(ALBUM_LOADER_ID)
        })
    }

    fun refreshResultFinder(finderList: ArrayList<AlbumEntity>, albumEntity: AlbumEntity) {
        finderList.find { it.parent == AlbumScanConst.ALL }?.let {
            it.duration = albumEntity.duration
            it.path = albumEntity.path
            it.mediaType = albumEntity.mediaType
            it.mimeType = albumEntity.mimeType
            it.id = albumEntity.id
            it.count = it.count + 1
        }
        finderList.find { it.parent == albumEntity.parent }?.let {
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

    private fun refreshFinder(list: ArrayList<AlbumEntity>) {
        finderList.clear()
        list.forEach { item -> if (finderList.find { it.parent == item.parent } == null) finderList.add(item.apply { count = list.count { it.parent == item.parent } }) }
        val first = finderList.first()
        finderList.add(0, AlbumEntity(
                parent = AlbumScanConst.ALL,
                duration = first.duration,
                path = first.path,
                mediaType = first.mediaType,
                mimeType = first.mimeType,
                id = first.id,
                count = list.size))
    }
}