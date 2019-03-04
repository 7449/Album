package com.album.core.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.album.core.AlbumFile.parentFile

/**
 * @author y
 * @create 2019/3/4
 */
class AlbumScanFileLoader(
        private val activity: Context,
        private val scanCount: Int,
        private val filePath: String,
        private val loaderSuccess: (ArrayList<AlbumEntity>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    private val albumList = ArrayList<AlbumEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val page = args?.getInt(AlbumScanImpl.KEY_PAGE) ?: 0
        val sortOrder = when (scanCount) {
            SCAN_ALL -> ALBUM_SORT_ORDER
            else -> ALBUM_SORT_ORDER + " limit " + page * scanCount + "," + scanCount
        }
        val selection = String.format(FILE_SELECTION, FILTER_SUFFIX)
        return CursorLoader(activity, ALBUM_FILE_URI, FILE_PROJECTION, selection, ALBUM_FILE_SELECTION_ARGS, sortOrder)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return
        val dataColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
        val mediaTypeIdColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
        while (cursor.moveToNext()) {
            val path = cursor.getString(dataColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val mediaType = cursor.getInt(mediaTypeIdColumnIndex)
            val parentFile = path.parentFile() ?: continue
            albumList.add(AlbumEntity(dirPath = parentFile.absolutePath, path = path, id = id, mediaType = mediaType))
        }
        loaderSuccess(albumList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}