package com.album.core.scan

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.collection.ArrayMap
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.album.core.AlbumFile.parentFile
import com.album.core.view.AlbumView

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
 */
class AlbumScanImpl(private val albumView: AlbumView,
                    private val scanType: Int,
                    private val scanCount: Int,
                    private val allName: String,
                    private val sdName: String,
                    private val filterImg: Boolean) : LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        fun newInstance(
                albumView: AlbumView,
                scanType: Int,
                scanCount: Int,
                allName: String,
                sdName: String,
                filterImg: Boolean
        ) = AlbumScanImpl(albumView, scanType, scanCount, allName, sdName, filterImg)
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(albumView.getAlbumActivity())

    private val finderEntityMap = ArrayMap<String, FinderEntity>()
    private val albumList = ArrayList<AlbumEntity>()
    private val finderList = ArrayList<FinderEntity>()

    private val KEY_BUCKET = "bucket"
    private val KEY_PAGE = "page"

    fun scanAll(bucketId: String, page: Int) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        albumView.showProgress()
        loaderManager.restartLoader(AlbumScan.ALBUM_LOADER_ID, Bundle().apply {
            putString(KEY_BUCKET, bucketId)
            putInt(KEY_PAGE, page)
        }, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val page = args?.getInt(KEY_PAGE) ?: 0
        val bucketId = args?.getString(KEY_BUCKET) ?: ""
        return if (scanType == AlbumScan.VIDEO) {
            VideoCursorLoader(albumView.getAlbumActivity(), scanCount, page, bucketId, filterImg)
        } else {
            ImageCursorLoader(albumView.getAlbumActivity(), scanCount, page, bucketId, filterImg)
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
        albumList.clear()
        finderList.clear()
        destroyLoaderManager()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    fun mergeEntity(albumList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>) {
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

    fun resultScan(path: String) {
        loaderManager.restartLoader(AlbumScan.RESULT_LOADER_ID, null, object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return if (scanType == AlbumScan.VIDEO) {
                    VideoResultCursorLoader(albumView.getAlbumActivity(), path)
                } else {
                    ImageResultCursorLoader(albumView.getAlbumActivity(), path)
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
                refreshFinder(finderList)
                albumView.resultSuccess(albumEntity)
                albumView.scanFinderSuccess(finderList)
                finderList.clear()
                destroyLoaderManager()
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        })
    }

    private fun refreshFinder(finderList: ArrayList<FinderEntity>) {
        val cursor = (if (scanType == AlbumScan.VIDEO) VideoFinderScanCursor(albumView.getAlbumActivity(), filterImg)
        else ImageFinderScanCursor(albumView.getAlbumActivity(), filterImg))
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
            if (finderEntity == null && finderPath.parentFile() != null) {
                if (filterImg && size <= 0) {
                    continue
                }
                finderEntityMap[finderName] = FinderEntity(if (TextUtils.equals(finderName, "0")) sdName else finderName, finderPath, id, bucketId,
                        if (scanType == AlbumScan.VIDEO) VideoFinderCursorCount(albumView.getAlbumActivity(), bucketId, filterImg)
                        else ImageFinderCursorCount(albumView.getAlbumActivity(), bucketId, filterImg))
            }
        }
        cursor.close()
        if (finderEntityMap.isEmpty) {
            return
        }
        val finderEntity = FinderEntity(dirName = allName)
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

    private fun destroyLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.ALBUM_LOADER_ID)
        loaderManager.destroyLoader(AlbumScan.RESULT_LOADER_ID)
        loaderManager.destroyLoader(AlbumScan.FINDER_LOADER_ID)
    }
}