package com.gallery.scan

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.args.audio.ScanAudioEntity
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.scan.types.Result

class ScanError(val type: Result)

class ScanResult<ENTITY : ScanEntityFactory>(val bundle: Bundle, val entities: ArrayList<ENTITY>)

class ValueResult<ENTITY : ScanEntityFactory>(val bundle: Bundle, val entity: ENTITY?)

fun ViewModelProvider.getScanFileImpl(): ScanImpl<ScanFileEntity> {
    return getScanImpl()
}

fun ViewModelProvider.getScanAudioImpl(): ScanImpl<ScanAudioEntity> {
    return getScanImpl()
}

fun <ENTITY : ScanEntityFactory> ViewModelProvider.getScanImpl(): ScanImpl<ENTITY> {
    @Suppress("UNCHECKED_CAST")
    return get(ScanImpl::class.java) as ScanImpl<ENTITY>
}

fun <ENTITY : ScanEntityFactory> FragmentActivity.scanViewModel(scanEntityFactory: ScanEntityFactory, scanCursorLoaderArgs: CursorLoaderArgs): ScanImpl<ENTITY> {
    return ScanImpl(object : ScanView<ENTITY> {
        override val scanContext: FragmentActivity
            get() = this@scanViewModel

        override val scanCursorLoaderArgs: CursorLoaderArgs
            get() = scanCursorLoaderArgs

        override val scanEntityFactory: ScanEntityFactory
            get() = scanEntityFactory
    })
}

class ScanViewModelFactory(
        private val fragmentActivity: FragmentActivity,
        private val scanEntityFactory: ScanEntityFactory,
        private val scanCursorLoaderArgs: CursorLoaderArgs
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return fragmentActivity.scanViewModel<ScanEntityFactory>(scanEntityFactory, scanCursorLoaderArgs) as T
    }

}