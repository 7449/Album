@file:Suppress("PrivatePropertyName")

package com.album.core.scan

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.collection.ArrayMap
import androidx.loader.app.LoaderManager
import com.album.core.AlbumFile.parentFile
import com.album.core.view.AlbumView

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
 */
class AlbumScanImpl(private val albumView: AlbumView,
                    private val scanType: Int,
                    private val scanCount: Int,
                    private val allName: String,
                    private val sdName: String,
                    private val filterImg: Boolean) {

    companion object {
        const val KEY_BUCKET = "bucket"
        const val KEY_PAGE = "page"
        fun newInstance(
                albumView: AlbumView,
                scanType: Int,
                scanCount: Int,
                allName: String,
                sdName: String,
                filterImg: Boolean
        ) = AlbumScanImpl(albumView, scanType, scanCount, allName, sdName, filterImg)
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(albumView.getAlbumActivity())

    private val finderEntityMap = ArrayMap<String, FinderEntity>()

    private val finderList = ArrayList<FinderEntity>()

    private val activity: Context = albumView.getAlbumActivity()

    fun scanAll(bucketId: String, page: Int) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        albumView.showProgress()
        loaderManager.restartLoader(AlbumScan.ALBUM_LOADER_ID, Bundle().apply {
            putString(KEY_BUCKET, bucketId)
            putInt(KEY_PAGE, page)
        }, AlbumScanLoader(activity, scanType, scanCount, filterImg) {
            albumView.hideProgress()
            if (it.isEmpty()) {
                if (albumView.getPage() == 0) {
                    albumView.onAlbumEmpty()
                } else {
                    albumView.onAlbumNoMore()
                }
            } else {
                refreshFinder(finderList)
                mergeEntity(it, albumView.getSelectEntity())
                albumView.scanSuccess(it)
                albumView.scanFinderSuccess(finderList)
            }
            finderList.clear()
            destroyLoaderManager()
        })
    }

    fun scanResult(path: String) {
        loaderManager.restartLoader(
                AlbumScan.RESULT_LOADER_ID,
                null,
                AlbumScanResultLoader(activity, scanType, path) {
                    refreshFinder(finderList)
                    albumView.resultSuccess(it)
                    albumView.scanFinderSuccess(finderList)
                    finderList.clear()
                    destroyLoaderManager()
                })
    }

    private fun refreshFinder(finderList: ArrayList<FinderEntity>) {
        val cursor = (if (scanType == AlbumScan.VIDEO) activity.albumVideoFinderScanCursor(filterImg)
        else activity.albumImageFinderScanCursor(filterImg))
                ?: return
        val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)
        val finderNameColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        val finderPathColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID)
        val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        while (cursor.moveToNext()) {
            val bucketId = cursor.getString(bucketIdColumnIndex)
            val finderName = cursor.getString(finderNameColumnIndex)
            val finderPath = cursor.getString(finderPathColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val finderEntity = finderEntityMap[finderName]
            if (finderEntity == null && finderPath.parentFile() != null) {
                if (filterImg && size <= 0) {
                    continue
                }
                finderEntityMap[finderName] = FinderEntity(if (TextUtils.equals(finderName, "0")) sdName else finderName, finderPath, id, bucketId,
                        if (scanType == AlbumScan.VIDEO) activity.albumVideoFinderCursorCount(bucketId, filterImg)
                        else activity.albumImageFinderCursorCount(bucketId, filterImg))
            }
        }
        cursor.close()
        if (finderEntityMap.isEmpty) {
            return
        }
        val finderEntity = FinderEntity(dirName = allName)
        var count = 0
        for ((_, value) in finderEntityMap) {
            finderList.add(value)
            count += value.count
        }
        finderEntity.count = count
        if (!finderList.isEmpty()) {
            finderEntity.thumbnailsPath = finderList[0].thumbnailsPath
            finderEntity.thumbnailsId = finderList[0].thumbnailsId
        }
        finderList.add(0, finderEntity)
        finderEntityMap.clear()
    }

    fun mergeEntity(albumList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>) {
        albumList.forEach { it.isCheck = false }
        selectEntity.forEach { select -> albumList.filter { it.path == select.path }.forEach { it.isCheck = true } }
    }

    private fun destroyLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.ALBUM_LOADER_ID)
        loaderManager.destroyLoader(AlbumScan.RESULT_LOADER_ID)
        loaderManager.destroyLoader(AlbumScan.FINDER_LOADER_ID)
    }
}