package com.album.sample.kt

import androidx.fragment.app.FragmentActivity
import com.album.scan.AlbumScanConst
import com.album.scan.scan.AlbumEntity
import com.album.scan.view.AlbumView


class AlbumViewKt {

    private var currentActivity: (() -> FragmentActivity)? = null
    private var currentScanType: (() -> Int)? = null
    private var scanSuccess: ((albumEntityList: ArrayList<AlbumEntity>) -> Unit)? = null
    private var resultSuccess: ((albumEntity: AlbumEntity?) -> Unit)? = null
    private var scanFinderSuccess: ((list: ArrayList<AlbumEntity>) -> Unit)? = null
    private var refreshUI: (() -> Unit)? = null
    private var currentSelectEntity: (() -> ArrayList<AlbumEntity>)? = null

    fun currentSelectEntity(currentSelectEntity: () -> ArrayList<AlbumEntity>) {
        this.currentSelectEntity = currentSelectEntity
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

    internal fun build(): AlbumView {
        return object : AlbumView {

            override fun getSelectEntity(): ArrayList<AlbumEntity> = currentSelectEntity?.invoke()
                    ?: ArrayList()

            override fun refreshUI() {
                refreshUI?.invoke()
            }

            override fun scanFinderSuccess(finderList: ArrayList<AlbumEntity>) {
                scanFinderSuccess?.invoke(finderList)
            }

            override fun resultSuccess(albumEntity: AlbumEntity?) {
                resultSuccess?.invoke(albumEntity)
            }

            override fun currentScanType(): Int = currentScanType?.invoke()
                    ?: AlbumScanConst.IMAGE

            override fun getAlbumContext(): FragmentActivity = currentActivity?.invoke()
                    ?: throw KotlinNullPointerException("check currentActivity")

            override fun scanSuccess(arrayList: ArrayList<AlbumEntity>) {
                scanSuccess?.invoke(arrayList)
            }

        }
    }
}