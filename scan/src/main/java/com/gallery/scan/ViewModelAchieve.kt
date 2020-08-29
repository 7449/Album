package com.gallery.scan

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.annotation.ScanTypeDef
import com.gallery.scan.annotation.SortDef
import com.gallery.scan.annotation.SortFieldDef
import com.gallery.scan.args.Columns

enum class Error {
    SCAN,
    RESULT
}

class ScanError(val type: Error)

class ScanResult(val parentId: Long, val entities: ArrayList<ScanEntity>)

class ValueResult(val id: Long, val entity: ScanEntity?)

fun FragmentActivity.scanViewModel(@ScanTypeDef scanType: Int = ScanType.IMAGE,
                                   @SortDef sort: String = Sort.DESC,
                                   @SortFieldDef fieldSort: String = Columns.DATE_MODIFIED): ScanImpl {
    return ScanImpl(object : ScanView {
        override val scanContext: FragmentActivity
            get() = this@scanViewModel

        override fun scanSort(): String = sort

        override fun scanSortField(): String = fieldSort

        override fun scanType(): Int = scanType
    })
}

class ScanViewModelFactory(
        private val fragmentActivity: FragmentActivity,
        @ScanTypeDef private val scanType: Int = ScanType.IMAGE,
        @SortDef private val scanSort: String = Sort.DESC,
        @SortFieldDef private val scanSortField: String = Columns.DATE_MODIFIED
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return fragmentActivity.scanViewModel(scanType, scanSort, scanSortField) as T
    }

}