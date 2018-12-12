package com.album.presenter.impl

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.collection.ArrayMap
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.album.*
import com.album.presenter.ALBUM_LOADER_ID
import com.album.presenter.AlbumPresenter
import com.album.presenter.FINDER_LOADER_ID
import com.album.presenter.RESULT_LOADER_ID
import com.album.ui.view.AlbumView
import com.album.util.*

/**
 * by y on 14/08/2017.
 */

@Suppress("PrivatePropertyName")
class AlbumPresenterImpl(private val albumView: AlbumView,
                         private val albumBundle: AlbumBundle)
    : AlbumPresenter, LoaderManager.LoaderCallbacks<Cursor> {
    private val loaderManager: LoaderManager = LoaderManager.getInstance(albumView.getAlbumActivity())

    private val KEY_BUCKET = "bucket"
    private val KEY_PAGE = "page"

    override fun startScan(bucketId: String, page: Int) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        albumView.showProgress()
        loaderManager.restartLoader(ALBUM_LOADER_ID, Bundle().apply {
            putString(KEY_BUCKET, bucketId)
            putInt(KEY_PAGE, page)
        }, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val page = args?.getInt(KEY_PAGE) ?: 0
        val bucketId = args?.getString(KEY_BUCKET) ?: ""
        return if (albumBundle.scanType == VIDEO) {
            VideoCursorLoader(albumView.getAlbumActivity(), albumBundle.scanCount, page, bucketId, albumBundle.filterImg)
        } else {
            ImageCursorLoader(albumView.getAlbumActivity(), albumBundle.scanCount, page, bucketId, albumBundle.filterImg)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        val cursor = data ?: return
        val albumList = ArrayList<AlbumEntity>()
        val finderList = ArrayList<FinderEntity>()
        val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
        val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        while (cursor.moveToNext()) {
            val path = cursor.getString(dataColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            val bucketId = cursor.getString(bucketIdColumnIndex)
            if (albumBundle.filterImg && size <= 0 || getParentFile(path) == null) {
                continue
            }
            albumList.add(AlbumEntity(path = path, id = id, bucketId = bucketId))
        }
        albumView.hideProgress()
        if (albumList.isEmpty()) {
            if (albumView.getPage() == 0) {
                albumView.onAlbumEmpty()
            } else {
                albumView.onAlbumNoMore()
            }
        } else {
            refreshFinder(finderList)
            mergeEntity(albumList, albumView.getSelectEntity())
            albumView.scanSuccess(albumList)
            albumView.scanFinderSuccess(finderList)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun mergeEntity(albumList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>) {
        for (albumEntity in albumList) {
            albumEntity.isCheck = false
        }
        for (albumEntity in selectEntity) {
            val path = albumEntity.path
            for (allAlbumEntity in albumList) {
                val allEntityPath = allAlbumEntity.path
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.isCheck = albumEntity.isCheck
                }
            }
        }
    }

    override fun resultScan(path: String) {
        loaderManager.restartLoader(RESULT_LOADER_ID, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return if (albumBundle.scanType == VIDEO) {
                    VideoResultCursorLoader(albumView.getAlbumActivity(), path)
                } else {
                    ImageResultCursorLoader(albumView.getAlbumActivity(), path)
                }
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
                val cursor = data ?: return
                var albumEntity: AlbumEntity? = null
                val finderList = ArrayList<FinderEntity>()
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
                while (cursor.moveToNext()) {
                    val resultPath = cursor.getString(dataColumnIndex)
                    val id = cursor.getLong(idColumnIndex)
                    val bucketId = cursor.getString(bucketIdColumnIndex)
                    if (getParentFile(resultPath) == null) {
                        continue
                    }
                    albumEntity = AlbumEntity(path = resultPath, id = id, bucketId = bucketId)
                }
                refreshFinder(finderList)
                albumView.resultSuccess(albumEntity)
                albumView.scanFinderSuccess(finderList)
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        })
    }

    private fun refreshFinder(finderList: ArrayList<FinderEntity>) {
        val finderEntityMap = ArrayMap<String, FinderEntity>()
        val cursor = (if (albumBundle.scanType == VIDEO) VideoFinderScanCursor(albumView.getAlbumActivity(), albumBundle.filterImg)
        else ImageFinderScanCursor(albumView.getAlbumActivity(), albumBundle.filterImg))
                ?: return
        val bucketIdColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)
        val finderNameColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        val finderPathColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
        val idColumnIndex = cursor.getColumnIndex(MediaStore.Video.Media._ID)
        val sizeColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        while (cursor.moveToNext()) {
            val bucketId = cursor.getString(bucketIdColumnIndex)
            val finderName = cursor.getString(finderNameColumnIndex)
            val finderPath = cursor.getString(finderPathColumnIndex)
            val size = cursor.getLong(sizeColumnIndex)
            val id = cursor.getLong(idColumnIndex)
            val finderEntity = finderEntityMap[finderName]
            if (finderEntity == null && getParentFile(finderPath) != null) {
                if (albumBundle.filterImg && size <= 0) {
                    continue
                }
                finderEntityMap[finderName] = FinderEntity(finderName, finderPath, id, bucketId,
                        if (albumBundle.scanType == VIDEO) VideoFinderCursorCount(albumView.getAlbumActivity(), bucketId, albumBundle.filterImg)
                        else ImageFinderCursorCount(albumView.getAlbumActivity(), bucketId, albumBundle.filterImg))
            }
        }
        cursor.close()
        if (finderEntityMap.isEmpty) {
            return
        }
        val finderEntity = FinderEntity(dirName = FINDER_ALL_DIR_NAME)
        var count = 0
        for ((_, value) in finderEntityMap) {
            finderList.add(value)
            count += value.count
        }
        finderEntity.count = count
        if (!finderList.isEmpty()) {
            finderEntity.thumbnailsPath = finderList[0].thumbnailsPath
            finderEntity.thumbnailsId = finderList[0].thumbnailsId
        }
        finderList.add(0, finderEntity)
        finderEntityMap.clear()
    }

    override fun destroyLoaderManager() {
        loaderManager.destroyLoader(ALBUM_LOADER_ID)
        loaderManager.destroyLoader(RESULT_LOADER_ID)
        loaderManager.destroyLoader(FINDER_LOADER_ID)
    }
}
