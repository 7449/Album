package com.album.core.scan

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.album.core.AlbumFile.parentFile
import com.album.core.view.AlbumPreViewView

/**
 * @author y
 * @create 2019/2/27
 * 预览扫描工具类
 */
class AlbumPreviewScanImpl(private val prevView: AlbumPreViewView,
                           private val filterImage: Boolean,
                           private val selectEntity: ArrayList<AlbumEntity>,
                           private val bucketId: String) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        fun newInstance(
                prevView: AlbumPreViewView,
                filterImage: Boolean,
                selectEntity: ArrayList<AlbumEntity>,
                bucketId: String
        ) = AlbumPreviewScanImpl(prevView, filterImage, selectEntity, bucketId)
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(prevView.getPrevContext())

    init {
        prevView.showProgress()
        loaderManager.initLoader(AlbumScan.PREVIEW_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val selection = if (TextUtils.isEmpty(bucketId)) {
            String.format(ALBUM_SELECTION, if (filterImage) FILTER_SUFFIX else "")
        } else {
            String.format(ALBUM_BUCKET_SELECTION, if (filterImage) FILTER_SUFFIX else "")
        }
        return CursorLoader(prevView.getPrevContext(), ALBUM_URL, ALBUM_PROJECTION, selection, selectionArgs(AlbumScan.IMAGE, bucketId), ALBUM_SORT_ORDER)
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
            if (filterImage && size <= 0 || path.parentFile() == null) {
                continue
            }
            albumList.add(AlbumEntity(path = path, id = id))
        }
        prevView.hideProgress()
        prevView.scanSuccess(mergeEntity(albumList, selectEntity))
        destroyLoaderManager()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    private fun mergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>): ArrayList<AlbumEntity> {
        for (albumEntity in selectEntity) {
            val path = albumEntity.path
            for (allAlbumEntity in albumEntityList) {
                val allEntityPath = allAlbumEntity.path
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.isCheck = albumEntity.isCheck
                }
            }
        }
        return albumEntityList
    }

    private fun destroyLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.PREVIEW_LOADER_ID)
    }
}