package com.gallery.sample.kt

import androidx.fragment.app.FragmentActivity
import com.gallery.scan.ScanEntity
import com.gallery.scan.ScanView
import com.gallery.scan.args.ScanConst


class GalleryViewKt {

    private var currentActivity: (() -> FragmentActivity)? = null
    private var currentScanTypes: (() -> Int)? = null
    private var scanSuccess: ((galleryEntityList: ArrayList<ScanEntity>, finderList: ArrayList<ScanEntity>) -> Unit)? = null
    private var resultSuccess: ((galleryEntity: ScanEntity?) -> Unit)? = null
    private var refreshUI: (() -> Unit)? = null
    private var currentSelectEntity: (() -> ArrayList<ScanEntity>)? = null

    fun currentSelectEntity(currentSelectEntity: () -> ArrayList<ScanEntity>) {
        this.currentSelectEntity = currentSelectEntity
    }

    fun currentActivity(currentActivity: () -> FragmentActivity) {
        this.currentActivity = currentActivity
    }

    fun currentScanType(currentScanType: () -> Int) {
        this.currentScanTypes = currentScanType
    }

    fun scanSuccess(scanSuccess: (galleryEntityList: ArrayList<ScanEntity>, finderList: ArrayList<ScanEntity>) -> Unit) {
        this.scanSuccess = scanSuccess
    }

    internal fun build(): ScanView {
        return object : ScanView {

            override val selectEntity: ArrayList<ScanEntity>
                get() = currentSelectEntity?.invoke()
                        ?: ArrayList()

            override fun refreshUI() {
                refreshUI?.invoke()
            }

            override fun resultSuccess(scanEntity: ScanEntity?) {
                resultSuccess?.invoke(scanEntity)
            }

            override val currentScanType: Int
                get() = currentScanTypes?.invoke() ?: ScanConst.IMAGE

            override val scanContext: FragmentActivity
                get() = currentActivity?.invoke()
                        ?: throw KotlinNullPointerException("check currentActivity")

            override fun scanSuccess(arrayList: ArrayList<ScanEntity>, finderList: ArrayList<ScanEntity>) {
                scanSuccess?.invoke(arrayList, finderList)
            }

        }
    }
}