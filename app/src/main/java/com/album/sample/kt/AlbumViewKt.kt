package com.album.sample.kt

import androidx.fragment.app.FragmentActivity
import com.album.scan.args.ScanConst
import com.album.scan.ScanEntity
import com.album.scan.ScanView


class AlbumViewKt {

    private var currentActivity: (() -> FragmentActivity)? = null
    private var currentScanType: (() -> Int)? = null
    private var scanSuccess: ((albumEntityList: ArrayList<ScanEntity>) -> Unit)? = null
    private var resultSuccess: ((albumEntity: ScanEntity?) -> Unit)? = null
    private var scanFinderSuccess: ((list: ArrayList<ScanEntity>) -> Unit)? = null
    private var refreshUI: (() -> Unit)? = null
    private var currentSelectEntity: (() -> ArrayList<ScanEntity>)? = null

    fun currentSelectEntity(currentSelectEntity: () -> ArrayList<ScanEntity>) {
        this.currentSelectEntity = currentSelectEntity
    }

    fun currentActivity(currentActivity: () -> FragmentActivity) {
        this.currentActivity = currentActivity
    }

    fun currentScanType(currentScanType: () -> Int) {
        this.currentScanType = currentScanType
    }

    fun scanSuccess(scanSuccess: (albumEntityList: ArrayList<ScanEntity>) -> Unit) {
        this.scanSuccess = scanSuccess
    }

    internal fun build(): ScanView {
        return object : ScanView {

            override fun getSelectEntity(): ArrayList<ScanEntity> = currentSelectEntity?.invoke()
                    ?: ArrayList()

            override fun refreshUI() {
                refreshUI?.invoke()
            }

            override fun scanFinderSuccess(finderList: ArrayList<ScanEntity>) {
                scanFinderSuccess?.invoke(finderList)
            }

            override fun resultSuccess(scanEntity: ScanEntity?) {
                resultSuccess?.invoke(scanEntity)
            }

            override fun currentScanType(): Int = currentScanType?.invoke()
                    ?: ScanConst.IMAGE

            override fun getScanContext(): FragmentActivity = currentActivity?.invoke()
                    ?: throw KotlinNullPointerException("check currentActivity")

            override fun scanSuccess(arrayList: ArrayList<ScanEntity>) {
                scanSuccess?.invoke(arrayList)
            }

        }
    }
}