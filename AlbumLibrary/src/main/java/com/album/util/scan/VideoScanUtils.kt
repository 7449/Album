package com.album.util.scan

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.util.ArrayMap
import android.text.TextUtils
import com.album.AlbumConstant
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.ui.view.ScanView
import com.album.ui.widget.ScanCallBack
import com.album.util.FileUtils
import java.util.*

/**
 * by y on 01/09/2017.
 */

class VideoScanUtils private constructor(private val contentResolver: ContentResolver) : ScanView {

    companion object {
        private const val FINDER_VIDEO_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? "
        private val VIDEO_COUNT_PROJECTION = arrayOf(MediaStore.Video.Media.BUCKET_ID)
        private val VIDEO_PROJECTION = arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID)
        private val VIDEO_FINDER_PROJECTION = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA)

        operator fun get(contentResolver: ContentResolver): VideoScanUtils {
            return VideoScanUtils(contentResolver)
        }
    }

    override fun start(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int) {
        val albumEntityList = ArrayList<AlbumEntity>()
        val finderEntityList = ArrayList<FinderEntity>()
        val cursor = getCursor(bucketId, page, count)
        if (cursor != null) {
            val dataColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            val idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID)
            val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
            while (cursor.moveToNext()) {
                scanCursor(albumEntityList, dataColumnIndex, idColumnIndex, sizeColumnIndex, cursor)
            }
            cursor.close()
            cursorFinder(finderEntityList)
            scanCallBack.scanSuccess(albumEntityList, finderEntityList)
        }
    }

    override fun resultScan(scanCallBack: ScanCallBack, path: String) {
        var albumEntity: AlbumEntity? = null
        val finderEntityList = ArrayList<FinderEntity>()
        val query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,
                MediaStore.Video.Media.DATA + "= ? ",
                arrayOf(path),
                MediaStore.Video.Media.DATE_MODIFIED)
        if (query != null) {
            val dataColumnIndex = query.getColumnIndex(MediaStore.Video.Media.DATA)
            val idColumnIndex = query.getColumnIndex(MediaStore.Video.Media._ID)
            while (query.moveToNext()) {
                val resultPath = query.getString(dataColumnIndex)
                val id = query.getLong(idColumnIndex)
                if (FileUtils.getPathFile(resultPath) != null) {
                    albumEntity = AlbumEntity("", "", resultPath, id, false)
                }
            }
            cursorFinder(finderEntityList)
            query.close()
        }
        scanCallBack.resultSuccess(albumEntity!!, finderEntityList)
    }

    override fun scanCursor(albumEntityList: ArrayList<AlbumEntity>, dataColumnIndex: Int, idColumnIndex: Int, sizeColumnIndex: Int, cursor: Cursor) {
        val path = cursor.getString(dataColumnIndex)
        val id = cursor.getLong(idColumnIndex)
        if (FileUtils.getPathFile(path) != null) {
            albumEntityList.add(AlbumEntity("", "", path, id, false))
        }
    }

    override fun cursorFinder(finderEntityList: ArrayList<FinderEntity>) {
        val finderEntityMap = ArrayMap<String, FinderEntity>()
        val finderCursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_FINDER_PROJECTION, null, null,
                MediaStore.Video.Media.DATE_MODIFIED + " desc")
        if (finderCursor != null) {
            val bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)
            val finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media.DATA)
            val idColumnIndex = finderCursor.getColumnIndex(MediaStore.Video.Media._ID)
            while (finderCursor.moveToNext()) {
                val bucketId = finderCursor.getString(bucketIdColumnIndex)
                val finderName = finderCursor.getString(finderNameColumnIndex)
                val finderPath = finderCursor.getString(finderPathColumnIndex)
                val id = finderCursor.getLong(idColumnIndex)
                val finderEntity = finderEntityMap[finderName]
                if (finderEntity == null && FileUtils.getPathFile(finderPath) != null) {
                    finderEntityMap[finderName] = FinderEntity(finderName, finderPath, id, bucketId, cursorCount(bucketId))
                }
            }
            finderCursor.close()
        }
        if (finderEntityMap.isEmpty) {
            return
        }
        val finderEntity = FinderEntity(AlbumConstant.ALL_ALBUM_NAME, "", 0, "", 0)
        var count = 0
        for ((_, value) in finderEntityMap) {
            finderEntityList.add(value)
            count += value.count
        }
        finderEntity.count = count
        if (finderEntityList.size > 0) {
            finderEntity.thumbnailsPath = finderEntityList[0].thumbnailsPath
            finderEntity.thumbnailsId = finderEntityList[0].thumbnailsId
        }
        finderEntityList.add(0, finderEntity)
        finderEntityMap.clear()
    }

    override fun cursorCount(bucketId: String): Int {
        val query = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_COUNT_PROJECTION,
                FINDER_VIDEO_SELECTION,
                arrayOf(bucketId),
                MediaStore.Video.Media.DATE_MODIFIED + " desc") ?: return 0
        val count = query.count
        query.close()
        return count
    }

    override fun getCursor(bucketId: String, page: Int, count: Int): Cursor? {
        val sortOrder = if (count == -1) MediaStore.Video.Media.DATE_MODIFIED + " desc" else MediaStore.Video.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count
        val selection = if (TextUtils.isEmpty(bucketId)) null else FINDER_VIDEO_SELECTION
        val args = if (TextUtils.isEmpty(bucketId)) null else arrayOf(bucketId)
        return contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,
                selection,
                args,
                sortOrder)
    }

    override fun getSelectionArgs(bucketId: String): Array<String> {
        return emptyArray()
    }

}
