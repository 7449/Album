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
import com.gallery.scan.types.Result
import com.gallery.scan.types.SCAN_ALL
import com.gallery.scan.types.isFileExists

/**
 * @author y
 * @create 2019/3/4
 *
 * 扫描
 */
internal class ScanTask(private val context: Context,
                        private val forceFilterFile: Boolean,
                        private val loaderError: () -> Unit,
                        private val loaderSuccess: (ArrayList<ScanEntity>) -> Unit) : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        args ?: throw KotlinNullPointerException("args == null")
        val parent: Long = args.getLong(Columns.PARENT)
        val selection: String = when (args.getSerializable(Columns.SCAN_RESULT) as Result) {
            Result.SINGLE -> CursorArgs.getResultSelection(args.getLong(Columns.ID))
            Result.MULTIPLE -> if (parent == SCAN_ALL) CursorArgs.ALL_SELECTION
            else CursorArgs.getParentSelection(parent)
        }
        return CursorLoader(context,
                CursorArgs.FILE_URI,
                CursorArgs.ALL_COLUMNS,
                selection,
                CursorArgs.getSelectionArgs(args.getInt(Columns.SCAN_TYPE)),
                "${args.getString(Columns.SORT_FIELD)} ${args.getString(Columns.SORT)}")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null) {
            loaderError.invoke()
            return
        }
        val cursor: Cursor = data
        val arrayList = ArrayList<ScanEntity>()
        while (cursor.moveToNext()) {
            val scanEntity = ScanEntity(
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
                    cursor.getLongOrDefault(Columns.DATE_ADDED),
                    cursor.getLongOrDefault(Columns.DATE_MODIFIED),
                    cursor.getLongOrDefault(Columns.PARENT),
                    0,
                    false)
            if (forceFilterFile) {
                if (scanEntity.isFileExists(context)) {
                    arrayList.add(scanEntity)
                }
            } else {
                arrayList.add(scanEntity)
            }
        }
        loaderSuccess(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}