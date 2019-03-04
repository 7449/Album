package com.album.core.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.album.core.AlbumFile.parentFile

/**
 * @author y
 * @create 2019/3/4
 */
class AlbumScanResultLoader(
        private val activity: Context,
        private val scanType: Int,
        private val path: String,
        private val loaderSuccess: (AlbumEntity?) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (scanType) {
            AlbumScan.IMAGE -> activity.albumImageResultCursorLoader(path)
            AlbumScan.VIDEO -> activity.albumVideoResultCursorLoader(path)
            else -> activity.albumImageResultCursorLoader(path)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return
        var albumEntity: AlbumEntity? = null
        val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
        while (cursor.moveToNext()) {
            val resultPath = cursor.getString(dataColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val bucketId = cursor.getString(bucketIdColumnIndex)
            if (resultPath.parentFile() == null) {
                continue
            }
            albumEntity = AlbumEntity(path = resultPath, id = id, bucketId = bucketId)
        }
        loaderSuccess(albumEntity)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}