package com.album.core.scan.task

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.album.core.scan.*

/**
 * @author y
 * @create 2019/3/4
 *
 * 文件扫描
 */
class AlbumScanFileTask private constructor(private val activity: Context, private val loaderSuccess: (ArrayList<AlbumEntity>) -> Unit) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        @JvmStatic
        fun newInstance(activity: Context, loaderSuccess: (ArrayList<AlbumEntity>) -> Unit): AlbumScanFileTask {
            return AlbumScanFileTask(activity, loaderSuccess)
        }
    }

    private val albumList = ArrayList<AlbumEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        val selection: String

        val page = args?.getInt(AlbumColumns.PAGE) ?: 0
        val parent = args?.getLong(AlbumColumns.PARENT) ?: 0
        val count = args?.getInt(AlbumColumns.COUNT) ?: 0
        val path = args?.getString(AlbumColumns.DATA) ?: ""
        val scanType = args?.getInt(AlbumColumns.SCAN_TYPE) ?: AlbumScan.IMAGE

        selection = when {
            path.isNotEmpty() -> ALBUM_PATH_SELECTION(path)
            parent == AlbumScan.ALL_PARENT -> ALBUM_ALL_SELECTION
            else -> ALBUM_PARENT_SELECTION(parent)
        }
        return CursorLoader(activity,
                ALBUM_FILE_URI,
                ALBUM_ALL_COLUMNS,
                selection,
                ALBUM_SELECTION_ARGS(scanType),
                if (count == SCAN_ALL) ALBUM_ORDER_BY else ALBUM_ORDER_BY_LIMIT(page, count))
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return

        val idColumnIndex = cursor.getColumnIndex(AlbumColumns.ID)
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
        val dateModifiedColumnIndex = cursor.getColumnIndex(AlbumColumns.DATE_MODIFIED)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumnIndex)
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
            val dataModified = cursor.getLong(dateModifiedColumnIndex)
            albumList.add(AlbumEntity(id, path, size, duration, parent, mimeType, displayName, orientation, bucketId, bucketDisplayName, mediaType, width, height, dataModified, 0, false))
        }

        loaderSuccess(albumList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}