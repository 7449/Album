package com.gallery.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.gallery.scan.args.IScanEntityFactory
import com.gallery.scan.args.ScanParameter.Companion.getScanParameter
import com.gallery.scan.types.Result
import com.gallery.scan.types.SCAN_ALL

/**
 * @author y
 * @create 2019/3/4
 *
 * 扫描
 */
internal class ScanTask<ENTITY : IScanEntityFactory>(private val context: Context,
                                                     private val scanFactory: IScanEntityFactory,
                                                     private val loaderError: () -> Unit,
                                                     private val loaderSuccess: (ArrayList<ENTITY>) -> Unit) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        internal const val SCAN_RESULT = "scan_result"
        private const val APPEND = " ${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR"

        private fun getScanTypeSelection(intArray: IntArray): String {
            val defaultSelection = StringBuilder(" ${MediaStore.Files.FileColumns.SIZE} > 0 AND ")
            intArray.forEach { _ -> defaultSelection.append(APPEND) }
            return defaultSelection.toString().removeSuffix("OR")
        }

        private fun getParentSelection(parent: Long, intArray: IntArray) = "${MediaStore.Files.FileColumns.PARENT}=$parent AND (${getScanTypeSelection(intArray)})"

        private fun getResultSelection(id: Long, intArray: IntArray) = "${MediaStore.Files.FileColumns._ID}=$id AND (${getScanTypeSelection(intArray)})"
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        args ?: throw KotlinNullPointerException("args == null")
        val parent: Long = args.getLong(MediaStore.Files.FileColumns.PARENT)
        val scanParameter = args.getScanParameter()
        val typeArray: IntArray = scanParameter.scanType
        val selection: String = when (args.getSerializable(SCAN_RESULT) as Result) {
            Result.SINGLE -> getResultSelection(args.getLong(MediaStore.Files.FileColumns._ID), typeArray)
            Result.MULTIPLE -> if (parent == SCAN_ALL) getScanTypeSelection(typeArray)
            else getParentSelection(parent, typeArray)
        }
        return CursorLoader(context,
                scanParameter.scanUri,
                scanParameter.scanColumns,
                selection,
                typeArray.map { it.toString() }.toTypedArray(),
                "${scanParameter.scanSortField} ${scanParameter.scanSort}")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null) {
            loaderError.invoke()
            return
        }
        val cursor: Cursor = data
        val arrayList = ArrayList<ENTITY>()
        while (cursor.moveToNext()) {
            arrayList.add(scanFactory.onCreateCursorGeneric(cursor))
        }
        loaderSuccess(arrayList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}