package com.album.util.scan

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import android.text.TextUtils
import androidx.collection.ArrayMap
import com.album.AlbumEntity
import com.album.FINDER_ALL_DIR_NAME
import com.album.FinderEntity
import com.album.util.getParentFile
import java.util.*

/**
 * by y on 01/09/2017.
 */

class VideoScanUtils private constructor(private val contentResolver: ContentResolver) : ScanBase(contentResolver) {

    companion object {
        private const val FINDER_VIDEO_SELECTION = MediaStore.Video.Media.BUCKET_ID + "= ? "
        private val VIDEO_COUNT_PROJECTION = arrayOf(MediaStore.Video.Media.BUCKET_ID)
        private val VIDEO_PROJECTION = arrayOf(MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID)
        private val VIDEO_FINDER_PROJECTION = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA)

        operator fun get(contentResolver: ContentResolver): VideoScanUtils = VideoScanUtils(contentResolver)
    }

    override fun resultScan(scanCallBack: ScanCallBack, path: String, filterImage: Boolean, sdName: String) {
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
                if (getParentFile(resultPath) != null) {
                    albumEntity = AlbumEntity("", "", resultPath, id, false)
                }
            }
            cursorFinder(finderEntityList, filterImage, sdName)
            query.close()
        }
        scanCallBack.resultCallBack(albumEntity!!, finderEntityList)
    }

    override fun scanCursor(albumEntityList: ArrayList<AlbumEntity>, dataColumnIndex: Int, idColumnIndex: Int, sizeColumnIndex: Int, filterImage: Boolean, cursor: Cursor) {
        val path = cursor.getString(dataColumnIndex)
        val id = cursor.getLong(idColumnIndex)
        if (getParentFile(path) != null) {
            albumEntityList.add(AlbumEntity("", "", path, id, false))
        }
    }

    override fun cursorFinder(finderEntityList: ArrayList<FinderEntity>, filterImage: Boolean, sdName: String) {
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
                if (finderEntity == null && getParentFile(finderPath) != null) {
                    finderEntityMap[finderName] = FinderEntity(finderName, finderPath, id, bucketId, cursorCount(bucketId, filterImage))
                }
            }
            finderCursor.close()
        }
        if (finderEntityMap.isEmpty) {
            return
        }
        val finderEntity = FinderEntity(FINDER_ALL_DIR_NAME, "", 0, "", 0)
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

    @SuppressLint("Recycle")
    override fun cursorCount(bucketId: String, filterImage: Boolean): Int {
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

    override fun getCursor(bucketId: String, page: Int, count: Int, filterImage: Boolean): Cursor? {
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
