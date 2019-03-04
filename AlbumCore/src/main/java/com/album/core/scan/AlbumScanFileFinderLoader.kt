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
class AlbumScanFileFinderLoader(
        private val activity: Context,
        private val allName: String,
        private val sdName: String,
        private val loaderSuccess: (ArrayList<FinderEntity>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    private val finderEntity = ArrayList<FinderEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(activity, ALBUM_FILE_URI,
                arrayOf("count(" + MediaStore.Files.FileColumns.PARENT + ")",
                        MediaStore.Files.FileColumns.DATA + " , " + MediaStore.Files.FileColumns._ID + " from (select *"),
                MediaStore.Files.FileColumns.MEDIA_TYPE + " =? or " + MediaStore.Files.FileColumns.MEDIA_TYPE + " =? )" + FILTER_SUFFIX
                        + " order by " + MediaStore.Files.FileColumns.DATE_MODIFIED + " )"
                        + " group by (" + MediaStore.Files.FileColumns.PARENT
                ,
                ALBUM_FILE_SELECTION_ARGS,
                ALBUM_SORT_ORDER)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return
        var maxCount = 0
        val pathColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
        while (cursor.moveToNext()) {
            val imageCount = cursor.getInt(0)
            val newImagePath = cursor.getString(pathColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val parentFile = newImagePath.parentFile() ?: return
            maxCount += imageCount
            finderEntity.add(FinderEntity(dirPath = parentFile.absolutePath, dirName = if (parentFile.name == "0") sdName else parentFile.name, thumbnailsPath = newImagePath, thumbnailsId = id, count = imageCount))
        }
        if (!finderEntity.isEmpty()) {
            finderEntity.add(0, FinderEntity(dirName = allName, thumbnailsPath = finderEntity[0].thumbnailsPath, thumbnailsId = finderEntity[0].thumbnailsId, count = maxCount))
        }
        loaderSuccess(finderEntity)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}