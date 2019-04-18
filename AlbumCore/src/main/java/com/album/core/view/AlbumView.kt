package com.album.core.view

import androidx.fragment.app.FragmentActivity
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.scan.SCAN_ALL

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumView : AlbumBaseView {
    /**
     * 获取已经选择数据
     */
    fun getSelectEntity(): ArrayList<AlbumEntity>

    /**
     * 页码
     */
    fun getPage(): Int

    /**
     * 扫描个数
     */
    fun getScanCount(): Int

    /**
     * 刷新数据
     */
    fun refreshUI()

    /**
     * 扫描文件夹成功
     */
    fun scanFinderSuccess(list: ArrayList<AlbumEntity>)

    /**
     * type用来区分扫描结果的回调
     */
    fun onAlbumScanCallback(type: Int)

    /**
     * 拍照扫描成功
     */
    fun resultSuccess(albumEntity: AlbumEntity?)
}

class AlbumViewKt {

    private var currentActivity: (() -> FragmentActivity)? = null
    private var currentScanType: (() -> Int)? = null
    private var scanSuccess: ((albumEntityList: ArrayList<AlbumEntity>) -> Unit)? = null
    private var showProgress: (() -> Unit)? = null
    private var hideProgress: (() -> Unit)? = null
    private var resultSuccess: ((albumEntity: AlbumEntity?) -> Unit)? = null
    private var onAlbumScanCallback: ((type: Int) -> Unit)? = null
    private var scanFinderSuccess: ((list: ArrayList<AlbumEntity>) -> Unit)? = null
    private var refreshUI: (() -> Unit)? = null
    private var currentSelectEntity: (() -> ArrayList<AlbumEntity>)? = null
    private var currentPage: (() -> Int)? = null
    private var currentScanCount: (() -> Int)? = null

    fun currentSelectEntity(currentSelectEntity: () -> ArrayList<AlbumEntity>) {
        this.currentSelectEntity = currentSelectEntity
    }

    fun currentPage(currentPage: () -> Int) {
        this.currentPage = currentPage
    }

    fun currentScanCount(currentScanCount: () -> Int) {
        this.currentScanCount = currentScanCount
    }

    fun currentActivity(currentActivity: () -> FragmentActivity) {
        this.currentActivity = currentActivity
    }

    fun currentScanType(currentScanType: () -> Int) {
        this.currentScanType = currentScanType
    }

    fun scanSuccess(scanSuccess: (albumEntityList: ArrayList<AlbumEntity>) -> Unit) {
        this.scanSuccess = scanSuccess
    }

    fun showProgress(showProgress: () -> Unit) {
        this.showProgress = showProgress
    }

    fun hideProgress(hideProgress: () -> Unit) {
        this.hideProgress = hideProgress
    }

    internal fun build(): AlbumView {
        return object : AlbumView {

            override fun getSelectEntity(): ArrayList<AlbumEntity> = currentSelectEntity?.invoke()
                    ?: ArrayList()

            override fun getPage(): Int = currentPage?.invoke() ?: 0

            override fun getScanCount(): Int = currentScanCount?.invoke() ?: SCAN_ALL

            override fun refreshUI() {
                refreshUI?.invoke()
            }

            override fun scanFinderSuccess(list: ArrayList<AlbumEntity>) {
                scanFinderSuccess?.invoke(list)
            }

            override fun onAlbumScanCallback(type: Int) {
                onAlbumScanCallback?.invoke(type)
            }

            override fun resultSuccess(albumEntity: AlbumEntity?) {
                resultSuccess?.invoke(albumEntity)
            }

            override fun currentScanType(): Int = currentScanType?.invoke()
                    ?: AlbumScan.IMAGE

            override fun getAlbumContext(): FragmentActivity = currentActivity?.invoke()
                    ?: throw KotlinNullPointerException("check currentActivity")

            override fun scanSuccess(albumEntityList: ArrayList<AlbumEntity>) {
                scanSuccess?.invoke(albumEntityList)
            }

            override fun hideProgress() {
                hideProgress?.invoke()
            }

            override fun showProgress() {
                showProgress?.invoke()
            }
        }
    }
}