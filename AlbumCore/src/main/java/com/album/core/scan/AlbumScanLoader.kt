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
@Deprecated(message = "see AlbumScanFileLoader")
class AlbumScanLoader(
        private val activity: Context,
        private val scanType: Int,
        private val scanCount: Int,
        private val filterImg: Boolean,
        private val loaderSuccess: (ArrayList<AlbumEntity>) -> Unit
) : LoaderManager.LoaderCallbacks<Cursor> {

    private val albumList = ArrayList<AlbumEntity>()

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val page = args?.getInt(AlbumScanImpl.KEY_PAGE) ?: 0
        val bucketId = args?.getString(AlbumScanImpl.KEY_BUCKET) ?: ""
        return when (scanType) {
            AlbumScan.IMAGE -> activity.albumImageCursorLoader(scanCount, page, bucketId, filterImg)
            AlbumScan.VIDEO -> activity.albumVideoCursorLoader(scanCount, page, bucketId, filterImg)
            else -> activity.albumVideoCursorLoader(scanCount, page, bucketId, filterImg)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return
        val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
        val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        while (cursor.moveToNext()) {
            val path = cursor.getString(dataColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            val bucketId = cursor.getString(bucketIdColumnIndex)
            if (filterImg && size <= 0 || path.parentFile() == null) {
                continue
            }
            albumList.add(AlbumEntity(path = path, id = id, bucketId = bucketId))
        }
        loaderSuccess(albumList)
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}