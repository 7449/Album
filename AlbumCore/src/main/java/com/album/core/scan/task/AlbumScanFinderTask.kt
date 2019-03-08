package com.album.core.scan.task

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.album.core.scan.*
import com.album.core.scan.AlbumScan.ALL_PARENT


/**
 * @author y
 * @create 2019/3/4
 */
class AlbumScanFinderTask(
        private val activity: Context,
        private val allName: String,
        private val sdName: String,
        private val loaderSuccess: (ArrayList<AlbumEntity>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    private val finderEntity = ArrayList<AlbumEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val scanType = args?.getInt(AlbumColumns.SCAN_TYPE) ?: AlbumScan.IMAGE
        return CursorLoader(activity,
                ALBUM_FILE_URI,
                ALBUM_FINDER_ALL_COLUMNS,
                ALBUM_FINDER_ALL_SELECTION,
                ALBUM_SELECTION_ARGS(scanType),
                ALBUM_ORDER_BY)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return

        val idColumnIndex = cursor.getColumnIndex(AlbumColumns.ID)
        val countColumnIndex = cursor.getColumnIndex(AlbumColumns.COUNT)
        val dataColumnIndex = cursor.getColumnIndex(AlbumColumns.DATA)
        val sizeColumnIndex = cursor.getColumnIndex(AlbumColumns.SIZE)
        val durationColumnIndex = cursor.getColumnIndex(AlbumColumns.DURATION)
        val parentColumnIndex = cursor.getColumnIndex(AlbumColumns.PARENT)
        val mimeTypeColumnIndex = cursor.getColumnIndex(AlbumColumns.MIME_TYPE)
        val displayNameColumnIndex = cursor.getColumnIndex(AlbumColumns.DISPLAY_NAME)
        val orientationColumnIndex = cursor.getColumnIndex(AlbumColumns.ORIENTATION)
        val bucketIdColumnIndex = cursor.getColumnIndex(AlbumColumns.BUCKET_ID)
        val bucketDisplayNameColumnIndex = cursor.getColumnIndex(AlbumColumns.BUCKET_DISPLAY_NAME)
        val mediaTypeColumnIndex = cursor.getColumnIndex(AlbumColumns.MEDIA_TYPE)
        val widthColumnIndex = cursor.getColumnIndex(AlbumColumns.WIDTH)
        val heightColumnIndex = cursor.getColumnIndex(AlbumColumns.HEIGHT)
        val dataModifiedColumnIndex = cursor.getColumnIndex(AlbumColumns.DATA_MODIFIED)

        var maxCount = 0

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumnIndex)
            val count = cursor.getInt(countColumnIndex)
            val path = cursor.getString(dataColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            val duration = cursor.getLong(durationColumnIndex)
            val parent = cursor.getLong(parentColumnIndex)
            val mimeType = cursor.getString(mimeTypeColumnIndex)
            val displayName = cursor.getString(displayNameColumnIndex)
            val orientation = cursor.getInt(orientationColumnIndex)
            val bucketId = cursor.getString(bucketIdColumnIndex)
            val bucketDisplayName = cursor.getString(bucketDisplayNameColumnIndex)
            val mediaType = cursor.getString(mediaTypeColumnIndex)
            val width = cursor.getInt(widthColumnIndex)
            val height = cursor.getInt(heightColumnIndex)
            val dataModified = cursor.getLong(dataModifiedColumnIndex)
            maxCount += count
            finderEntity.add(AlbumEntity(id, path, size, duration, parent, mimeType, displayName, orientation, bucketId, if (bucketDisplayName == "0") sdName else bucketDisplayName, mediaType, width, height, dataModified, count, false))
        }

        if (!finderEntity.isEmpty()) {
            val first = finderEntity.first()
            finderEntity.add(0, AlbumEntity(parent = ALL_PARENT, duration = first.duration, path = first.path, bucketDisplayName = allName, mediaType = first.mediaType, mimeType = first.mimeType, id = first.id, count = maxCount))
        }

        loaderSuccess(finderEntity)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}