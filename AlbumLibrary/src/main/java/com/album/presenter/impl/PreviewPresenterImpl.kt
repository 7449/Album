package com.album.presenter.impl

import android.text.TextUtils
import com.album.AlbumBundle
import com.album.AlbumEntity
import com.album.FinderEntity
import com.album.presenter.PreviewPresenter
import com.album.ui.view.PrevView
import com.album.util.AlbumTask
import com.album.util.AlbumTaskCallBack
import com.album.util.scan.AlbumScanUtils
import com.album.util.scan.ScanCallBack
import com.album.util.scan.ScanView
import java.util.*

/**
 * by y on 17/08/2017.
 */

class PreviewPresenterImpl(private val prevView: PrevView, private val albumBundle: AlbumBundle) : PreviewPresenter, ScanCallBack {

    private val scanView: ScanView = AlbumScanUtils[prevView.getPreViewActivity().contentResolver]

    override fun startScan(bucketId: String, page: Int, count: Int) {
        prevView.getPreViewActivity().runOnUiThread { prevView.showProgress() }
        AlbumTask.instance.start(object : AlbumTaskCallBack.Call {
            override fun start() {
                scanView.startScan(this@PreviewPresenterImpl, bucketId, page, count, albumBundle.filterImg, albumBundle.sdName)
            }
        })
    }

    override fun mergeEntity(albumEntityList: List<AlbumEntity>, selectEntity: ArrayList<AlbumEntity>) {
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

    override fun scanCallBack(imageList: ArrayList<AlbumEntity>, finderList: ArrayList<FinderEntity>) {
        prevView.getPreViewActivity().runOnUiThread {
            prevView.hideProgress()
            prevView.scanSuccess(imageList)
        }
    }

    override fun resultCallBack(image: AlbumEntity?, finderList: ArrayList<FinderEntity>) {}

}
