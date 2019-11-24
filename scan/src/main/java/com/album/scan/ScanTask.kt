package com.album.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.album.scan.args.Columns
import com.album.scan.args.CursorArgs
import com.album.scan.args.ScanConst

/**
 * @author y
 * @create 2019/3/4
 *
 * 图片扫描
 */
class ScanTask(private val context: Context, private val loaderSuccess: (ArrayList<ScanEntity>) -> Unit) : LoaderManager.LoaderCallbacks<Cursor> {

    private val arrayList = ArrayList<ScanEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        val selection: String

        val parent = args?.getLong(Columns.PARENT) ?: 0
        val path = args?.getString(Columns.DATA) ?: ""
        val scanType = args?.getInt(Columns.SCAN_TYPE) ?: ScanConst.IMAGE

        selection = when {
            path.isNotEmpty() -> CursorArgs.getPathSelection(path)
            parent == ScanConst.ALL -> CursorArgs.ALL_SELECTION
            else -> CursorArgs.getParentSelection(parent)
        }
        return CursorLoader(context,
                CursorArgs.FILE_URI,
                CursorArgs.ALL_COLUMNS,
                selection,
                CursorArgs.getSelectionArgs(scanType),
                CursorArgs.ORDER_BY)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return

        val idColumnIndex = cursor.getColumnIndex(Columns.ID)
        val dataColumnIndex = cursor.getColumnIndex(Columns.DATA)
        val sizeColumnIndex = cursor.getColumnIndex(Columns.SIZE)
        val durationColumnIndex = cursor.getColumnIndex(Columns.DURATION)
        val parentColumnIndex = cursor.getColumnIndex(Columns.PARENT)
        val mimeTypeColumnIndex = cursor.getColumnIndex(Columns.MIME_TYPE)
        val displayNameColumnIndex = cursor.getColumnIndex(Columns.DISPLAY_NAME)
        val orientationColumnIndex = cursor.getColumnIndex(Columns.ORIENTATION)
        val bucketIdColumnIndex = cursor.getColumnIndex(Columns.BUCKET_ID)
        val bucketDisplayNameColumnIndex = cursor.getColumnIndex(Columns.BUCKET_DISPLAY_NAME)
        val mediaTypeColumnIndex = cursor.getColumnIndex(Columns.MEDIA_TYPE)
        val widthColumnIndex = cursor.getColumnIndex(Columns.WIDTH)
        val heightColumnIndex = cursor.getColumnIndex(Columns.HEIGHT)
        val dateModifiedColumnIndex = cursor.getColumnIndex(Columns.DATE_MODIFIED)

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
            arrayList.add(ScanEntity(id, path, size, duration, parent, mimeType, displayName, orientation, bucketId, bucketDisplayName, mediaType, width, height, dataModified, 0, false))
        }

        loaderSuccess(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}