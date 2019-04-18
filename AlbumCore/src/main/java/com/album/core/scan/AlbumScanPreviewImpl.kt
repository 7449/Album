package com.album.core.scan

import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.album.core.scan.task.AlbumScanFileTask
import com.album.core.view.AlbumPreViewView
import com.album.core.view.AlbumPreViewViewKt

/**
 * @author y
 * @create 2019/2/27
 * 预览扫描工具类
 */

class AlbumScanPreviewImpl private constructor(private val prevView: AlbumPreViewView) {

    companion object {
        @JvmStatic
        fun newInstance(prevView: AlbumPreViewView) = AlbumScanPreviewImpl(prevView)

        fun newInstance(albumPreViewViewKt: AlbumPreViewViewKt.() -> Unit) = newInstance(AlbumPreViewViewKt().also(albumPreViewViewKt).build())
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(prevView.getAlbumContext())

    private val allEntity = prevView.getAllEntity()

    private val selectEntity = prevView.getSelectEntity()

    private val parent = prevView.getParent()

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
                        putInt(AlbumColumns.COUNT, SCAN_ALL)
                        putInt(AlbumColumns.SCAN_TYPE, prevView.currentScanType())
                    },
                    AlbumScanFileTask.newInstance(prevView.getAlbumContext()) {
                        prevView.hideProgress()
                        prevView.scanSuccess(mergeEntity(it, selectEntity))
                        destroyLoaderManager()
                    })
        }
    }

    private fun mergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>): ArrayList<AlbumEntity> {
        selectEntity.forEach { select -> albumEntityList.find { it.path == select.path }?.isCheck = true }
        return albumEntityList
    }

    private fun destroyLoaderManager() {
        loaderManager.destroyLoader(AlbumScan.PREVIEW_LOADER_ID)
    }
}