package com.album.core.scan

import android.os.Bundle
import androidx.loader.app.LoaderManager
import com.album.core.AlbumScan
import com.album.core.mergeEntity
import com.album.core.view.AlbumPreView

/**
 * @author y
 * @create 2019/2/27
 * 预览扫描工具类
 */
class AlbumScanPreviewImpl private constructor(private val prevView: AlbumPreView) {

    companion object {
        private const val PREVIEW_LOADER_ID = 110
        @JvmStatic
        fun newInstance(prevView: AlbumPreView) = AlbumScanPreviewImpl(prevView)
    }

    private val loaderManager: LoaderManager = LoaderManager.getInstance(prevView.getAlbumContext())

    private val allEntity = prevView.getAllEntity()

    private val selectEntity = prevView.getSelectEntity()

    private val parent = prevView.getParentId()

    init {
        if (allEntity != null) {
            val newEntity = ArrayList<AlbumEntity>()
            newEntity.addAll(allEntity)
            prevView.scanSuccess(newEntity.mergeEntity(selectEntity))
        } else {
            loaderManager.initLoader(PREVIEW_LOADER_ID, Bundle().apply {
                putLong(AlbumColumns.PARENT, parent)
                putInt(AlbumColumns.SCAN_TYPE, prevView.currentScanType())
            }, AlbumScanFileTask.newInstance(prevView.getAlbumContext()) {
                prevView.scanSuccess(it.mergeEntity(selectEntity))
                loaderManager.destroyLoader(PREVIEW_LOADER_ID)
            })
        }
    }
}