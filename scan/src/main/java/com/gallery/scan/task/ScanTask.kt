package com.gallery.scan.task

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.gallery.scan.args.CursorLoaderArgs.Companion.getCursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory

/**
 * @author y
 * @create 2019/3/4
 *
 * 扫描
 */
internal class ScanTask<E>(
        private val context: Context,
        private val factory: ScanEntityFactory,
        private val error: () -> Unit,
        private val success: (ArrayList<E>) -> Unit,
) : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        args ?: throw KotlinNullPointerException("args == null")
        val scanParameter = args.getCursorLoaderArgs()
        return CursorLoader(context,
                scanParameter.uri,
                scanParameter.projection,
                scanParameter.createSelection(args),
                scanParameter.createSelectionArgs(args),
                scanParameter.sortOrder)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null) {
            error.invoke()
            return
        }
        val arrayList = ArrayList<E>()
        while (data.moveToNext()) {
            arrayList.add(factory.cursorMoveToNextGeneric(data))
        }
        success(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}