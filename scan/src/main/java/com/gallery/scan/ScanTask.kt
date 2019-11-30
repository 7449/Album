package com.gallery.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.gallery.scan.args.Columns
import com.gallery.scan.args.CursorArgs
import com.gallery.scan.args.ScanConst

/**
 * @author y
 * @create 2019/3/4
 *
 * 图片扫描
 */
class ScanTask(private val context: Context, private val loaderSuccess: (ArrayList<ScanEntity>) -> Unit) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val PARENT_DEFAULT = 0L
        private const val ID_DEFAULT = 0L
    }

    private val arrayList = ArrayList<ScanEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val parent = args?.getLong(Columns.PARENT) ?: PARENT_DEFAULT
        val fileId = args?.getLong(Columns.ID) ?: ID_DEFAULT
        val scanType = args?.getInt(Columns.SCAN_TYPE) ?: ScanConst.IMAGE
        val selection = when {
            fileId != ID_DEFAULT -> CursorArgs.getResultSelection(fileId)
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