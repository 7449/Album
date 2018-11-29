package com.album.presenter.impl

import android.text.TextUtils
import com.album.AlbumBundle
import com.album.AlbumEntity
import com.album.FinderEntity
import com.album.VIDEO
import com.album.presenter.AlbumPresenter
import com.album.ui.view.AlbumView
import com.album.util.AlbumTask
import com.album.util.AlbumTaskCallBack
import com.album.util.ScanCallBack
import com.album.util.ScanView
import com.album.util.scan.AlbumScanUtils
import com.album.util.scan.VideoScanUtils
import java.util.*

/**
 * by y on 14/08/2017.
 */

class AlbumPresenterImpl(private val albumView: AlbumView, private val albumBundle: AlbumBundle) : AlbumPresenter, ScanCallBack {
    private var scanLoading = false
    private val scanView: ScanView

    init {
        val contentResolver = albumView.getAlbumActivity().contentResolver
        scanView = if (albumBundle.scanType == VIDEO) VideoScanUtils[contentResolver] else AlbumScanUtils[contentResolver]
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
                scanView.scan(this@AlbumPresenterImpl, bucketId, page, count, albumBundle.filterImg, albumBundle.sdName)
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

    override fun getScanLoading(): Boolean = scanLoading

    override fun resultScan(path: String) {
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.resultScan(this@AlbumPresenterImpl, path, albumBundle.filterImg, albumBundle.sdName)
            }
        })
    }

    override fun scanCallBack(albumEntityList: ArrayList<AlbumEntity>, list: ArrayList<FinderEntity>) {
        albumView.getAlbumActivity().runOnUiThread {
            scanLoading = false
            albumView.hideProgress()
            if (albumEntityList.isEmpty()) {
                if (albumView.getPage() == 0) {
                    albumView.onAlbumEmpty()
                } else {
                    albumView.onAlbumNoMore()
                }
            } else {
                mergeSelectEntity(albumEntityList, albumView.getSelectEntity())
                albumView.scanSuccess(albumEntityList)
                albumView.scanFinder(list)
            }
        }
    }

    override fun resultCallBack(albumEntity: AlbumEntity?, finderEntityList: ArrayList<FinderEntity>) {
        albumView.getAlbumActivity().runOnUiThread {
            albumView.resultSuccess(albumEntity)
            albumView.scanFinder(finderEntityList)
        }
    }
}
