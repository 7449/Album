package com.album.core.scan

import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.album.core.scan.task.AlbumScanFileTask
import com.album.core.view.AlbumPreViewView

/**
 * @author y
 * @create 2019/2/27
 * 预览扫描工具类
 */
class AlbumScanPreviewImpl(private val prevView: AlbumPreViewView,
                           private val selectEntity: ArrayList<AlbumEntity>,
                           allEntity: ArrayList<AlbumEntity>?,
                           parent: Long,
                           scanType: Int) {

    companion object {
        fun newInstance(
                prevView: AlbumPreViewView,
                selectEntity: ArrayList<AlbumEntity>,
                allEntity: ArrayList<AlbumEntity>?,
                parent: Long,
                scanType: Int
        ) = AlbumScanPreviewImpl(prevView, selectEntity, allEntity, parent, scanType)
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(prevView.getPrevContext())

    init {
        if (allEntity != null) {
            val newEntity = ArrayList<AlbumEntity>()
            newEntity.addAll(allEntity)
            prevView.scanSuccess(mergeEntity(newEntity, selectEntity))
        } else {
            prevView.showProgress()
            loaderManager.initLoader(AlbumScan.PREVIEW_LOADER_ID,
                    Bundle().apply {
                        putLong(AlbumColumns.PARENT, parent)
                        putInt(AlbumColumns.SCAN_TYPE, scanType)
                    },
                    AlbumScanFileTask(prevView.getPrevContext(), SCAN_ALL) {
                        prevView.hideProgress()
                        prevView.scanSuccess(mergeEntity(it, selectEntity))
                        destroyLoaderManager()
                    })
        }
    }

    private fun mergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>): ArrayList<AlbumEntity> {
        selectEntity.forEach { select -> albumEntityList.filter { it.path == select.path }.forEach { it.isCheck = true } }
        return albumEntityList
    }

    private fun destroyLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.PREVIEW_LOADER_ID)
    }
}