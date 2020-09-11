package com.gallery.scan

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.args.IScanEntityFactory
import com.gallery.scan.args.ScanMinimumEntity
import com.gallery.scan.args.ScanParameter
import com.gallery.scan.types.Result

class ScanError(val type: Result)

class ScanResult<ENTITY : IScanEntityFactory>(val parentId: Long, val entities: ArrayList<ENTITY>)

class ValueResult<ENTITY : IScanEntityFactory>(val id: Long, val entity: ENTITY?)

fun ViewModelProvider.getScanMinimumImpl(): ScanImpl<ScanMinimumEntity> {
    return getScanImpl()
}

fun <ENTITY : IScanEntityFactory> ViewModelProvider.getScanImpl(): ScanImpl<ENTITY> {
    @Suppress("UNCHECKED_CAST")
    return get(ScanImpl::class.java) as ScanImpl<ENTITY>
}

fun <ENTITY : IScanEntityFactory> FragmentActivity.scanViewModel(
        scanFactoryCreate: IScanEntityFactory = IScanEntityFactory.api19Factory(),
        scanParameter: ScanParameter = ScanParameter()): ScanImpl<ENTITY> {
    return ScanImpl(object : ScanView<ENTITY> {
        override val scanContext: FragmentActivity
            get() = this@scanViewModel

        override val scanParameter: ScanParameter
            get() = scanParameter

        override val scanFactoryCreate: IScanEntityFactory
            get() = scanFactoryCreate
    })
}

class ScanViewModelFactory(
        private val fragmentActivity: FragmentActivity,
        private val scanFactoryCreate: IScanEntityFactory = IScanEntityFactory.api19Factory(),
        private val scanParameter: ScanParameter = ScanParameter()
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return fragmentActivity.scanViewModel<IScanEntityFactory>(scanFactoryCreate, scanParameter) as T
    }

}