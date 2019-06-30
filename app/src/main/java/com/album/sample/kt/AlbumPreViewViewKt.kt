package com.album.sample.kt

import androidx.fragment.app.FragmentActivity
import com.album.core.AlbumScan
import com.album.core.scan.AlbumEntity
import com.album.core.view.AlbumPreView


class AlbumPreViewViewKt {

    private var currentSelectEntity: (() -> ArrayList<AlbumEntity>)? = null
    private var currentParent: (() -> Long)? = null
    private var currentActivity: (() -> FragmentActivity)? = null
    private var currentScanType: (() -> Int)? = null
    private var scanSuccess: ((albumEntityList: ArrayList<AlbumEntity>) -> Unit)? = null

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

    internal fun build(): AlbumPreView {
        return object : AlbumPreView {

            override fun getSelectEntity(): ArrayList<AlbumEntity> = currentSelectEntity?.invoke()
                    ?: ArrayList()

            override fun getParentId(): Long = currentParent?.invoke()
                    ?: 0

            override fun getAlbumContext(): FragmentActivity = currentActivity?.invoke()
                    ?: throw KotlinNullPointerException("check currentActivity")

            override fun currentScanType(): Int = currentScanType?.invoke()
                    ?: AlbumScan.IMAGE

            override fun scanSuccess(arrayList: ArrayList<AlbumEntity>) {
                scanSuccess?.invoke(arrayList)
            }

        }
    }
}