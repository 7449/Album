package com.album.core.scan

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.album.core.AlbumFile.fileExists

/**
 * @author y
 * @create 2019/3/4
 */
@Deprecated(message = "see AlbumScanPrevFileLoader")
class AlbumScanPreviewLoader(
        private val activity: Context,
        private val filterImage: Boolean,
        private val bucketId: String,
        private val loaderSuccess: (ArrayList<AlbumEntity>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
                activity,
                ALBUM_URL,
                ALBUM_PROJECTION,
                if (TextUtils.isEmpty(bucketId)) {
                    String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else "")
                } else {
                    String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
                },
                bucketId.albumSelectionArgs(AlbumScan.IMAGE),
                ALBUM_SORT_ORDER)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        data ?: return
        val albumList = ArrayList<AlbumEntity>()
        val dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA)
        val idColumnIndex = data.getColumnIndex(MediaStore.Images.Media._ID)
        val sizeColumnIndex = data.getColumnIndex(MediaStore.Images.Media.SIZE)
        while (data.moveToNext()) {
            val path = data.getString(dataColumnIndex)
            val id = data.getLong(idColumnIndex)
            val size = data.getLong(sizeColumnIndex)
            if (filterImage && size <= 0 || !path.fileExists()) {
                continue
            }
            albumList.add(AlbumEntity(path = path, id = id))
        }
        loaderSuccess(albumList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}