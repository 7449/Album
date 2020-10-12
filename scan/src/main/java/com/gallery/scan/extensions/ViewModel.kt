@file:Suppress("UNCHECKED_CAST")

package com.gallery.scan.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.gallery.scan.ScanImpl
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.callback.ScanCore

/** [ScanFileEntity] [ScanViewModelFactory] */
fun ViewModelProvider.scanFileImpl(): ScanImpl<ScanFileEntity> = scanImpl()

/** [ScanAudioEntity] [ScanViewModelFactory] */
fun ViewModelProvider.scanAudioImpl(): ScanImpl<ScanAudioEntity> = scanImpl()

/** [ScanPictureEntity] [ScanViewModelFactory] */
fun ViewModelProvider.scanPictureImpl(): ScanImpl<ScanPictureEntity> = scanImpl()

/** [ScanImpl] [ScanViewModelFactory] */
fun <E> ViewModelProvider.scanImpl(): ScanImpl<E> = get(ScanImpl::class.java) as ScanImpl<E>

class ScanViewModelFactory(
        private val ownerActivity: FragmentActivity? = null,
        private val ownerFragment: Fragment? = null,
        private val factory: ScanEntityFactory,
        private val args: CursorLoaderArgs,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanImpl<Any>(object : ScanCore {
            override val scanCursorLoaderArgs: CursorLoaderArgs = args
            override val scanEntityFactory: ScanEntityFactory = factory
            override val scanOwner: ViewModelStoreOwner = ownerActivity ?: ownerFragment
            ?: throw KotlinNullPointerException("ownerActivity or ownerFragment == null,")
        }) as T
    }

}