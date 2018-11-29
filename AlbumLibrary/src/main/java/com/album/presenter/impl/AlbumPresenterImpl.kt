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
import com.album.util.scan.AlbumScanUtils
import com.album.util.scan.ScanCallBack
import com.album.util.scan.ScanView
import com.album.util.scan.VideoScanUtils
import java.util.*

/**
 * by y on 14/08/2017.
 */

class AlbumPresenterImpl(private val albumView: AlbumView, private val albumBundle: AlbumBundle) : AlbumPresenter, ScanCallBack {

    private var scanLoading = false
    private val scanView: ScanView = if (albumBundle.scanType == VIDEO) VideoScanUtils[albumView.getAlbumActivity().contentResolver] else AlbumScanUtils[albumView.getAlbumActivity().contentResolver]

    override fun startScan(bucketId: String, page: Int, count: Int) {
        albumView.getAlbumActivity().runOnUiThread {
            scanLoading = true
            if (page == 0) {
                albumView.showProgress()
            }
        }
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.startScan(this@AlbumPresenterImpl, bucketId, page, count, albumBundle.filterImg, albumBundle.sdName)
            }
        })
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

    override fun hasScanLoading(): Boolean = scanLoading

    override fun resultScan(path: String) {
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.resultScan(this@AlbumPresenterImpl, path, albumBundle.filterImg, albumBundle.sdName)
            }
        })
    }

    override fun scanCallBack(imageList: ArrayList<AlbumEntity>, finderList: ArrayList<FinderEntity>) {
        albumView.getAlbumActivity().runOnUiThread {
            scanLoading = false
            albumView.hideProgress()
            if (imageList.isEmpty()) {
                if (albumView.getPage() == 0) {
                    albumView.onAlbumEmpty()
                } else {
                    albumView.onAlbumNoMore()
                }
            } else {
                mergeEntity(imageList, albumView.getSelectEntity())
                albumView.scanSuccess(imageList)
                albumView.scanFinderSuccess(finderList)
            }
        }
    }

    override fun resultCallBack(image: AlbumEntity?, finderList: ArrayList<FinderEntity>) {
        albumView.getAlbumActivity().runOnUiThread {
            albumView.resultSuccess(image)
            albumView.scanFinderSuccess(finderList)
        }
    }
}
