package com.album.presenter.impl

import android.text.TextUtils
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.presenter.AlbumPresenter
import com.album.ui.view.AlbumView
import com.album.ui.view.ScanView
import com.album.ui.widget.ScanCallBack
import com.album.util.scan.AlbumScanUtils
import com.album.util.scan.VideoScanUtils
import com.album.util.task.AlbumTask
import com.album.util.task.AlbumTaskCallBack
import java.util.*

/**
 * by y on 14/08/2017.
 */

class AlbumPresenterImpl(private val albumView: AlbumView, isVideo: Boolean) : AlbumPresenter, ScanCallBack {
    private var scanLoading = false
    private val scanView: ScanView

    init {
        val contentResolver = albumView.getAlbumActivity().contentResolver
        scanView = if (isVideo) VideoScanUtils[contentResolver] else AlbumScanUtils[contentResolver]
    }

    override fun scan(bucketId: String, page: Int, count: Int) {
        albumView.getAlbumActivity().runOnUiThread {
            scanLoading = true
            if (page == 0) {
                albumView.showProgress()
            }
        }
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.start(this@AlbumPresenterImpl, bucketId, page, count)
            }
        })
    }

    override fun mergeSelectEntity(albumList: ArrayList<AlbumEntity>, multiplePreviewList: ArrayList<AlbumEntity>) {
        for (albumEntity in albumList) {
            albumEntity.isCheck = false
        }
        for (albumEntity in multiplePreviewList) {
            val path = albumEntity.path
            for (allAlbumEntity in albumList) {
                val allEntityPath = allAlbumEntity.path
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.isCheck = albumEntity.isCheck
                }
            }
        }
    }

    override fun firstMergeEntity(albumEntityList: ArrayList<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>?) {
        if (selectEntity == null) return
        if (albumEntityList.isEmpty()) return
        for (albumEntity in albumEntityList) {
            albumEntity.isCheck = false
        }
        for (albumEntity in selectEntity) {
            albumEntity.isCheck = true
        }
        for (albumEntity in selectEntity) {
            val path = albumEntity.path
            for (allAlbumEntity in albumEntityList) {
                val allEntityPath = allAlbumEntity.path
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.isCheck = albumEntity.isCheck
                }
            }
        }

    }

    override fun getScanLoading(): Boolean {
        return scanLoading
    }

    override fun resultScan(path: String) {
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.resultScan(this@AlbumPresenterImpl, path)
            }
        })
    }

    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>, list: ArrayList<FinderEntity>) {
        albumView.getAlbumActivity().runOnUiThread {
            scanLoading = false
            albumView.hideProgress()
            if (albumEntityList.isEmpty()) {
                albumView.onAlbumNoMore()
            } else {
                albumView.scanSuccess(albumEntityList)
                if (!list.isEmpty()) {
                    albumView.scanFinder(list)
                }
            }
        }
    }

    override fun resultSuccess(albumEntity: AlbumEntity, finderEntityList: ArrayList<FinderEntity>) {
        albumView.getAlbumActivity().runOnUiThread {
            albumView.resultSuccess(albumEntity)
            if (!finderEntityList.isEmpty()) {
                albumView.scanFinder(finderEntityList)
            }
        }
    }
}
