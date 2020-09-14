@file:Suppress("UNCHECKED_CAST")

package com.gallery.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.args.audio.ScanAudioEntity
import com.gallery.scan.args.file.ScanFileEntity
import com.gallery.scan.args.picture.ScanPictureEntity
import com.gallery.scan.types.Result

class ScanError(val type: Result)

class ScanMultipleResult<ENTITY : ScanEntityFactory>(val bundle: Bundle, val entities: ArrayList<ENTITY>)

class ScanSingleResult<ENTITY : ScanEntityFactory>(val bundle: Bundle, val entity: ENTITY?)

fun ViewModelProvider.scanFileImpl(): ScanImpl<ScanFileEntity> = scanImpl()

fun ViewModelProvider.scanAudioImpl(): ScanImpl<ScanAudioEntity> = scanImpl()

fun ViewModelProvider.scanPictureImpl(): ScanImpl<ScanPictureEntity> = scanImpl()

fun <ENTITY : ScanEntityFactory> ViewModelProvider.scanImpl(): ScanImpl<ENTITY> = get(ScanImpl::class.java) as ScanImpl<ENTITY>

fun <ENTITY : ScanEntityFactory> FragmentActivity.scanViewModel(factory: ScanEntityFactory, args: CursorLoaderArgs): ScanImpl<ENTITY> {
    return ScanImpl(object : ScanView<ENTITY> {
        override val scanCursorLoaderArgs: CursorLoaderArgs
            get() = args
        override val scanEntityFactory: ScanEntityFactory
            get() = factory

        override fun scanOwner(): ViewModelStoreOwner {
            return this@scanViewModel
        }
    })
}

fun <ENTITY : ScanEntityFactory> Fragment.scanViewModel(factory: ScanEntityFactory, args: CursorLoaderArgs): ScanImpl<ENTITY> {
    return ScanImpl(object : ScanView<ENTITY> {
        override val scanCursorLoaderArgs: CursorLoaderArgs
            get() = args
        override val scanEntityFactory: ScanEntityFactory
            get() = factory

        override fun scanOwner(): ViewModelStoreOwner {
            return this@scanViewModel
        }
    })
}

class ScanViewModelFactory(
        private val ownerActivity: FragmentActivity? = null,
        private val ownerFragment: Fragment? = null,
        private val factory: ScanEntityFactory,
        private val args: CursorLoaderArgs
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ownerActivity?.scanViewModel<ScanEntityFactory>(factory, args) as? T
                ?: ownerFragment?.scanViewModel<ScanEntityFactory>(factory, args) as T
    }

}