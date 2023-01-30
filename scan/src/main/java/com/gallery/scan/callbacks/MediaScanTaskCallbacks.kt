package com.gallery.scan.callbacks

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.gallery.scan.args.MediaCursorLoaderArgs
import com.gallery.scan.args.MediaScanEntityFactory

internal class MediaScanTaskCallbacks<E>(
    private val context: Context,
    private val factory: MediaScanEntityFactory,
    private val loaderArgs: MediaCursorLoaderArgs,
    private val success: (ArrayList<E>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        args ?: throw KotlinNullPointerException("args == null")
        return CursorLoader(
            context,
            loaderArgs.uri,
            loaderArgs.projection,
            loaderArgs.createSelection(args),
            null,
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