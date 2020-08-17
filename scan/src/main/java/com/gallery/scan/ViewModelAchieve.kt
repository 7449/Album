package com.gallery.scan

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.annotation.ScanTypeDef

class ScanResult(val parentId: Long, val entities: ArrayList<ScanEntity>)

class ValueResult(val id: Long, val entity: ScanEntity?)

fun FragmentActivity.scanViewModel(@ScanTypeDef scanType: Int): ScanImpl {
    return ScanImpl(object : ScanView {
        override val scanContext: FragmentActivity
            get() = this@scanViewModel

        override fun scanType(): Int = scanType
    })
}

class ScanViewModelFactory(
        private val fragmentActivity: FragmentActivity,
        private val scanType: Int
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return fragmentActivity.scanViewModel(scanType) as T
    }

}