package com.gallery.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.gallery.scan.args.Columns
import com.gallery.scan.args.CursorArgs

/**
 * @author y
 * @create 2019/3/4
 *
 * 图片扫描
 */
internal class ScanTask(private val context: Context, private val loaderSuccess: (ArrayList<ScanEntity>) -> Unit) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val ID_DEFAULT = 0L
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        args ?: throw KotlinNullPointerException("args == null")
        val parent = args.getLong(Columns.PARENT)
        val fileId = args.getLong(Columns.ID)
        val scanType = args.getSerializable(Columns.SCAN_TYPE) as ScanType
        val selection = when {
            fileId != ID_DEFAULT -> CursorArgs.getResultSelection(fileId)
            parent == SCAN_ALL -> CursorArgs.ALL_SELECTION
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

        val arrayList = ArrayList<ScanEntity>()
        while (cursor.moveToNext()) {
            arrayList.add(ScanEntity(
                    cursor.getLongOrDefault(idColumnIndex),
                    cursor.getLongOrDefault(sizeColumnIndex),
                    cursor.getLongOrDefault(durationColumnIndex),
                    cursor.getLongOrDefault(parentColumnIndex),
                    cursor.getStringOrDefault(mimeTypeColumnIndex),
                    cursor.getStringOrDefault(displayNameColumnIndex),
                    cursor.getIntOrDefault(orientationColumnIndex),
                    cursor.getStringOrDefault(bucketIdColumnIndex),
                    cursor.getStringOrDefault(bucketDisplayNameColumnIndex),
                    cursor.getStringOrDefault(mediaTypeColumnIndex),
                    cursor.getIntOrDefault(widthColumnIndex),
                    cursor.getIntOrDefault(heightColumnIndex),
                    cursor.getLongOrDefault(dateModifiedColumnIndex),
                    0,
                    false)
            )
        }
        loaderSuccess(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    private fun Cursor.getLongOrDefault(index: Int): Long {
        return if (index == -1) {
            0L
        } else {
            getLong(index)
        }
    }

    private fun Cursor.getIntOrDefault(index: Int): Int {
        return if (index == -1) {
            0
        } else {
            getInt(index)
        }
    }

    private fun Cursor.getStringOrDefault(index: Int): String {
        return if (index == -1) {
            ""
        } else {
            getString(index)
        }
    }
}