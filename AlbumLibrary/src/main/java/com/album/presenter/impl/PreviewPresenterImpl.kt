package com.album.presenter.impl

import android.text.TextUtils
import com.album.entity.AlbumEntity
import com.album.entity.FinderEntity
import com.album.presenter.PreviewPresenter
import com.album.ui.view.PrevView
import com.album.util.scan.ScanView
import com.album.util.scan.ScanCallBack
import com.album.util.scan.AlbumScanUtils
import com.album.util.task.AlbumTask
import com.album.util.task.AlbumTaskCallBack
import java.util.*

/**
 * by y on 17/08/2017.
 */

class PreviewPresenterImpl(private val prevView: PrevView) : PreviewPresenter, ScanCallBack {

    private val scanView: ScanView = AlbumScanUtils[prevView.getPreViewActivity().contentResolver]

    override fun scan(bucketId: String, page: Int, count: Int) {
        prevView.getPreViewActivity().runOnUiThread { prevView.showProgress() }
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.start(this@PreviewPresenterImpl, bucketId, page, count)
            }
        })
    }

    override fun mergeSelectEntity(albumEntityList: List<AlbumEntity>, selectAlbumEntityList: ArrayList<AlbumEntity>) {
        for (albumEntity in selectAlbumEntityList) {
            val path = albumEntity.path
            for (allAlbumEntity in albumEntityList) {
                val allEntityPath = allAlbumEntity.path
                if (TextUtils.equals(path, allEntityPath)) {
                    allAlbumEntity.isCheck = albumEntity.isCheck
                }
            }
        }
    }

    override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>, list: ArrayList<FinderEntity>) {
        prevView.getPreViewActivity().runOnUiThread {
            prevView.hideProgress()
            prevView.scanSuccess(albumEntityList)
        }
    }

    override fun resultSuccess(albumEntity: AlbumEntity, finderEntityList: ArrayList<FinderEntity>) {}

}
