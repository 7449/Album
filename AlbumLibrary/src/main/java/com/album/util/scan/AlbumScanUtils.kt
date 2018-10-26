package com.album.util.scan

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import android.text.TextUtils
import androidx.collection.ArrayMap
import com.album.Album
import com.album.AlbumConstant
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.util.FileUtils
import java.util.*

/**
 * by y on 11/08/2017.
 */
class AlbumScanUtils private constructor(private val contentResolver: ContentResolver) : ScanView {

    companion object {
        private const val ALL_ALBUM_SELECTION = MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ? "
        private const val FINDER_ALBUM_SELECTION = MediaStore.Images.Media.BUCKET_ID + "= ? %s and  (" + ALL_ALBUM_SELECTION + " ) "
        private val ALBUM_NO_BUCKET_ID_SELECTION_ARGS = arrayOf("image/jpeg", "image/png", "image/jpg", "image/gif")
        private val ALBUM_COUNT_PROJECTION = arrayOf(MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.SIZE)
        private val ALBUM_PROJECTION = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.SIZE)
        private val ALBUM_FINDER_PROJECTION = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE)

        operator fun get(contentResolver: ContentResolver): AlbumScanUtils {
            return AlbumScanUtils(contentResolver)
        }
    }

    override fun start(scanCallBack: ScanCallBack, bucketId: String, page: Int, count: Int) {
        val albumEntityList = ArrayList<AlbumEntity>()
        val finderEntityList = ArrayList<FinderEntity>()
        val cursor = getCursor(bucketId, page, count)
        if (cursor != null) {
            val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
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
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_PROJECTION,
                MediaStore.Images.Media.DATA + "= ? ",
                arrayOf(path),
                MediaStore.Images.Media.DATE_MODIFIED)
        if (query != null) {
            val dataColumnIndex = query.getColumnIndex(MediaStore.Images.Media.DATA)
            val idColumnIndex = query.getColumnIndex(MediaStore.Images.Media._ID)
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
        scanCallBack.resultSuccess(albumEntity, finderEntityList)
    }

    override fun scanCursor(albumEntityList: ArrayList<AlbumEntity>, dataColumnIndex: Int, idColumnIndex: Int, sizeColumnIndex: Int, cursor: Cursor) {
        val path = cursor.getString(dataColumnIndex)
        val id = cursor.getLong(idColumnIndex)
        val size = cursor.getLong(sizeColumnIndex)
        if (FileUtils.getPathFile(path) != null) {
            if (Album.instance.config.filterImg && size <= 0) {
                return
            }
            albumEntityList.add(AlbumEntity("", "", path, id, false))
        }
    }

    override fun cursorFinder(finderEntityList: ArrayList<FinderEntity>) {
        val finderEntityMap = ArrayMap<String, FinderEntity>()
        val finderCursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_FINDER_PROJECTION, ALL_ALBUM_SELECTION, ALBUM_NO_BUCKET_ID_SELECTION_ARGS,
                MediaStore.Images.Media.DATE_MODIFIED + " desc")
        if (finderCursor != null) {
            val bucketIdColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
            val finderNameColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val finderPathColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val idColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media._ID)
            val sizeColumnIndex = finderCursor.getColumnIndex(MediaStore.Images.Media.SIZE)
            while (finderCursor.moveToNext()) {
                val bucketId = finderCursor.getString(bucketIdColumnIndex)
                val finderName = if (TextUtils.equals(finderCursor.getString(finderNameColumnIndex), "0")) Album.instance.config.sdName else finderCursor.getString(finderNameColumnIndex)
                val finderPath = finderCursor.getString(finderPathColumnIndex)
                val size = finderCursor.getLong(sizeColumnIndex)
                val id = finderCursor.getLong(idColumnIndex)
                val finderEntity = finderEntityMap[finderName]
                if (finderEntity == null && FileUtils.getPathFile(finderPath) != null) {
                    if (Album.instance.config.filterImg && size <= 0) {
                        continue
                    }
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
        val selection = if (Album.instance.config.filterImg) String.format(FINDER_ALBUM_SELECTION, " and _size > 0") else String.format(FINDER_ALBUM_SELECTION, "")
        val query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_COUNT_PROJECTION,
                selection,
                getSelectionArgs(bucketId),
                MediaStore.Images.Media.DATE_MODIFIED + " desc") ?: return 0
        val count = query.count
        query.close()
        return count
    }

    override fun getCursor(bucketId: String, page: Int, count: Int): Cursor? {
        val sortOrder = if (count == -1) MediaStore.Images.Media.DATE_MODIFIED + " desc" else MediaStore.Images.Media.DATE_MODIFIED + " desc limit " + page * count + "," + count
        val selection = if (TextUtils.isEmpty(bucketId)) ALL_ALBUM_SELECTION else if (Album.instance.config.filterImg) String.format(FINDER_ALBUM_SELECTION, " and _size > 0") else String.format(FINDER_ALBUM_SELECTION, "")
        val args = if (TextUtils.isEmpty(bucketId)) ALBUM_NO_BUCKET_ID_SELECTION_ARGS else getSelectionArgs(bucketId)
        return contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                ALBUM_PROJECTION,
                selection,
                args,
                sortOrder)
    }

    override fun getSelectionArgs(bucketId: String): Array<String> {
        return arrayOf(bucketId, "image/jpeg", "image/png", "image/jpg", "image/gif")
    }
}



