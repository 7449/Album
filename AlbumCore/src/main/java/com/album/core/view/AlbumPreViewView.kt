package com.album.core.view

import androidx.fragment.app.FragmentActivity
import com.album.core.scan.AlbumEntity
import com.album.core.scan.AlbumScan
import com.album.core.scan.AlbumScanPreviewImpl

/**
 * @author y
 * @create 2019/2/27
 */
interface AlbumPreViewView : AlbumBaseView {

    /**
     * 选中的数据
     */
    fun getSelectEntity(): ArrayList<AlbumEntity>

    /**
     * 如果不为null,则为点击预览进入多选查看模式,不扫描数据库,直接返回[getSelectEntity]合并之后的数据
     */
    fun getAllEntity(): ArrayList<AlbumEntity>? = null

    /**
     * parent Id
     */
    fun getParent(): Long

}

class AlbumPreViewViewKt {

    private var currentSelectEntity: (() -> ArrayList<AlbumEntity>)? = null
    private var currentParent: (() -> Long)? = null
    private var currentActivity: (() -> FragmentActivity)? = null
    private var currentScanType: (() -> Int)? = null
    private var scanSuccess: ((albumEntityList: ArrayList<AlbumEntity>) -> Unit)? = null
    private var showProgress: (() -> Unit)? = null
    private var hideProgress: (() -> Unit)? = null

    fun currentSelectEntity(currentSelectEntity: () -> ArrayList<AlbumEntity>) {
        this.currentSelectEntity = currentSelectEntity
    }

    fun currentParent(currentParent: () -> Long) {
        this.currentParent = currentParent
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

    internal fun build(): AlbumPreViewView {
        AlbumScanPreviewImpl.newInstance {

        }
        return object : AlbumPreViewView {

            override fun getSelectEntity(): ArrayList<AlbumEntity> = currentSelectEntity?.invoke()
                    ?: ArrayList()

            override fun getParent(): Long = currentParent?.invoke()
                    ?: 0

            override fun getAlbumContext(): FragmentActivity = currentActivity?.invoke()
                    ?: throw KotlinNullPointerException("check currentActivity")

            override fun currentScanType(): Int = currentScanType?.invoke()
                    ?: AlbumScan.IMAGE

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