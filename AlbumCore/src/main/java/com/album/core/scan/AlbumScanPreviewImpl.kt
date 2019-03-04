package com.album.core.scan

import androidx.loader.app.LoaderManager
import com.album.core.view.AlbumPreViewView

/**
 * @author y
 * @create 2019/2/27
 * 预览扫描工具类
 */
class AlbumScanPreviewImpl(private val prevView: AlbumPreViewView,
                           filterImage: Boolean,
                           private val selectEntity: ArrayList<AlbumEntity>,
                           bucketId: String) {

    companion object {
        fun newInstance(
                prevView: AlbumPreViewView,
                filterImage: Boolean,
                selectEntity: ArrayList<AlbumEntity>,
                bucketId: String
        ) = AlbumScanPreviewImpl(prevView, filterImage, selectEntity, bucketId)
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(prevView.getPrevContext())

    init {
        prevView.showProgress()
        loaderManager.initLoader(AlbumScan.PREVIEW_LOADER_ID, null, AlbumScanPreviewLoader(prevView.getPrevContext(), filterImage, bucketId) {
            prevView.hideProgress()
            prevView.scanSuccess(mergeEntity(it, selectEntity))
            destroyLoaderManager()
        })
    }

    private fun mergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>): ArrayList<AlbumEntity> {
        selectEntity.forEach { select -> albumEntityList.filter { it.path == select.path }.forEach { it.isCheck = true } }
        return albumEntityList
    }

    private fun destroyLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.PREVIEW_LOADER_ID)
    }
}