@file:Suppress("UNCHECKED_CAST")

package com.gallery.scan.extensions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.gallery.scan.ScanImpl
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.callback.ScanCore

/** [ScanFileEntity] [ScanViewModelFactory2] */
fun ViewModelProvider.scanFileImpl(): ScanImpl<ScanFileEntity> = scanImpl()

/** [ScanAudioEntity] [ScanViewModelFactory2] */
fun ViewModelProvider.scanAudioImpl(): ScanImpl<ScanAudioEntity> = scanImpl()

/** [ScanPictureEntity] [ScanViewModelFactory2] */
fun ViewModelProvider.scanPictureImpl(): ScanImpl<ScanPictureEntity> = scanImpl()

/** [ScanImpl] [ScanViewModelFactory2] */
fun <E> ViewModelProvider.scanImpl(): ScanImpl<E> = get(ScanImpl::class.java) as ScanImpl<E>

open class ScanViewModelFactory2(
        private val context: Context,
        private val viewModelStoreOwner: ViewModelStoreOwner,
        private val factory: ScanEntityFactory,
        private val args: CursorLoaderArgs,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanImpl<Any>(object : ScanCore {
            override val scanContext: Context get() = context
            override val scanCursorLoaderArgs: CursorLoaderArgs = args
            override val scanEntityFactory: ScanEntityFactory = factory
            override val scanOwner: ViewModelStoreOwner = viewModelStoreOwner
        }) as T
    }

}

open class ScanViewModelFactory(
        ownerActivity: FragmentActivity? = null,
        ownerFragment: Fragment? = null,
        factory: ScanEntityFactory,
        args: CursorLoaderArgs,
) : ScanViewModelFactory2(
        ownerActivity ?: ownerFragment?.activity
        ?: throw KotlinNullPointerException("ownerActivity or ownerFragment == null,"),
        ownerActivity ?: ownerFragment
        ?: throw KotlinNullPointerException("ownerActivity or ownerFragment == null,"),
        factory, args
)

fun FragmentActivity.scanViewModelFactory(
        factory: ScanEntityFactory,
        args: CursorLoaderArgs,
): ScanViewModelFactory2 {
    return ScanViewModelFactory(
            ownerActivity = this,
            factory = factory,
            args = args
    )
}

fun Fragment.scanViewModelFactory(
        factory: ScanEntityFactory,
        args: CursorLoaderArgs,
): ScanViewModelFactory2 {
    return ScanViewModelFactory(
            ownerFragment = this,
            factory = factory,
            args = args
    )
}