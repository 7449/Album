package com.album.util.scan

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.collection.ArrayMap
import com.album.AlbumEntity
import com.album.FINDER_ALL_DIR_NAME
import com.album.FinderEntity
import com.album.util.getParentFile
import java.util.*

/**
 * by y on 01/09/2017.
 */

class VideoScanUtils(contentResolver: ContentResolver) : ScanBase(contentResolver) {

    companion object {
        operator fun get(contentResolver: ContentResolver): VideoScanUtils = VideoScanUtils(contentResolver)
    }

    override fun startScan(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int, filterImage: Boolean, sdName: String) {
        val albumList = ArrayList<AlbumEntity>()
        val finderList = ArrayList<FinderEntity>()
        val cursor = videoScanCursor(bucketId, page, count, filterImage) ?: return
        val dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID)
        val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
        while (cursor.moveToNext()) {
            val path = cursor.getString(dataColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            if (filterImage && size <= 0 || getParentFile(path) == null) {
                continue
            }
            albumList.add(AlbumEntity("", "", path, id, false))
        }
        cursor.close()
        cursorFinder(finderList, filterImage, sdName)
        scanCallBack.scanCallBack(albumList, finderList)
    }

    override fun resultScan(scanCallBack: ScanCallBack, path: String, filterImage: Boolean, sdName: String) {
        var albumEntity: AlbumEntity? = null
        val finderList = ArrayList<FinderEntity>()
        val query = videoResultScanCursor(path) ?: return
        val dataColumnIndex = query.getColumnIndex(MediaStore.Video.Media.DATA)
        val idColumnIndex = query.getColumnIndex(MediaStore.Video.Media._ID)
        while (query.moveToNext()) {
            val resultPath = query.getString(dataColumnIndex)
            val id = query.getLong(idColumnIndex)
            if (getParentFile(resultPath) == null) {
                continue
            }
            albumEntity = AlbumEntity("", "", resultPath, id, false)
        }
        query.close()
        cursorFinder(finderList, filterImage, sdName)
        scanCallBack.resultCallBack(albumEntity, finderList)
    }

    override fun cursorFinder(finderList: ArrayList<FinderEntity>, filterImage: Boolean, sdName: String) {
        val finderEntityMap = ArrayMap<String, FinderEntity>()
        val cursor = videoFinderScanCursor(filterImage) ?: return
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
                if (finderEntity == null && getParentFile(finderPath) != null) {
                    if (filterImage && size <= 0) {
                        continue
                    }
                    finderEntityMap[finderName] = FinderEntity(finderName, finderPath, id, bucketId, videoFinderCursorCount(bucketId, filterImage))
                }
            }
        cursor.close()
        if (finderEntityMap.isEmpty) {
            return
        }
        val finderEntity = FinderEntity(FINDER_ALL_DIR_NAME, "", 0, "", 0)
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
}
