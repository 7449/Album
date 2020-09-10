package com.gallery.scan

import android.provider.MediaStore
import androidx.annotation.Size
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.annotation.SortDef
import com.gallery.scan.annotation.SortFieldDef
import com.gallery.scan.args.Columns
import com.gallery.scan.types.Result
import com.gallery.scan.types.Sort

class ScanError(val type: Result)

class ScanResult(val parentId: Long, val entities: ArrayList<ScanEntity>)

class ValueResult(val id: Long, val entity: ScanEntity?)

fun FragmentActivity.scanViewModel(
        @Size(min = 1, max = 3) scanType: IntArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
        @SortDef sort: String = Sort.DESC,
        @SortFieldDef fieldSort: String = Columns.DATE_MODIFIED): ScanImpl {
    return ScanImpl(object : ScanView {
        override val scanContext: FragmentActivity
            get() = this@scanViewModel

        override fun scanType(): IntArray = scanType

        override fun scanSort(): String = sort

        override fun scanSortField(): String = fieldSort
    })
}

class ScanViewModelFactory(
        private val fragmentActivity: FragmentActivity,
        @Size(min = 1, max = 3) private val scanType: IntArray = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
        @SortDef private val scanSort: String = Sort.DESC,
        @SortFieldDef private val scanSortField: String = Columns.DATE_MODIFIED
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return fragmentActivity.scanViewModel(scanType, scanSort, scanSortField) as T
    }

}