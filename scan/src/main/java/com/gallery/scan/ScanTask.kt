package com.gallery.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.kotlin.expand.database.getIntOrDefault
import androidx.kotlin.expand.database.getLongOrDefault
import androidx.kotlin.expand.database.getStringOrDefault
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
        val parent: Long = args.getLong(Columns.PARENT)
        val fileId: Long = args.getLong(Columns.ID)
        val scanType: ScanType = args.getSerializable(Columns.SCAN_TYPE) as ScanType
        val selection: String = when {
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
        val cursor: Cursor = data ?: return
        val arrayList = ArrayList<ScanEntity>()
        while (cursor.moveToNext()) {
            arrayList.add(ScanEntity(
                    cursor.getLongOrDefault(Columns.ID),
                    cursor.getLongOrDefault(Columns.SIZE),
                    cursor.getLongOrDefault(Columns.DURATION),
                    cursor.getStringOrDefault(Columns.MIME_TYPE),
                    cursor.getStringOrDefault(Columns.DISPLAY_NAME),
                    cursor.getIntOrDefault(Columns.ORIENTATION),
                    cursor.getStringOrDefault(Columns.BUCKET_ID),
                    cursor.getStringOrDefault(Columns.BUCKET_DISPLAY_NAME),
                    cursor.getStringOrDefault(Columns.MEDIA_TYPE),
                    cursor.getIntOrDefault(Columns.WIDTH),
                    cursor.getIntOrDefault(Columns.HEIGHT),
                    cursor.getLongOrDefault(Columns.DATE_MODIFIED),
                    cursor.getLongOrDefault(Columns.PARENT),
                    0,
                    false)
            )
        }
        loaderSuccess(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}