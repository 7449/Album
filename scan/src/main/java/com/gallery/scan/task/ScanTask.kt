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
        private val success: (ArrayList<E>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        args ?: throw KotlinNullPointerException("args == null")
        val loaderArgs = args.getCursorLoaderArgs()
        val bundle = Bundle(args)
        return CursorLoader(
                context,
                loaderArgs.uri,
                loaderArgs.projection,
                loaderArgs.createSelection(bundle),
                loaderArgs.createSelectionArgs(bundle),
                loaderArgs.sortOrder
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null) {
            success.invoke(arrayListOf())
            return
        }
        val arrayList = arrayListOf<E>()
        while (data.moveToNext()) {
            arrayList.add(factory.cursorMoveToNextGeneric(data))
        }
        success(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

}