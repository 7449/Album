package com.gallery.scan.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.gallery.scan.MediaScanContext
import com.gallery.scan.Types
import com.gallery.scan.args.MediaCursorLoaderArgs
import com.gallery.scan.args.MediaScanEntityFactory
import com.gallery.scan.impl.file.file

/** 是否是扫描全部的Id */
val Long.isScanAllMedia: Boolean get() = this == Types.Id.ALL

/** 是否禁止扫描,也可以自定义参数,不一定非要使用内置的参数 */
val Long.isScanNoNeMedia: Boolean get() = this == Types.Id.NONE

fun Fragment.fileScan(args: MediaCursorLoaderArgs): MediaScanContext {
    return object : MediaScanContext {
        override fun <T> context(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
            @Suppress("UNCHECKED_CAST")
            return this@fileScan as T
        }

        override val loaderArgs: MediaCursorLoaderArgs get() = args
        override val factory: MediaScanEntityFactory get() = MediaScanEntityFactory.file()
    }
}

fun FragmentActivity.fileScan(args: MediaCursorLoaderArgs): MediaScanContext {
    return object : MediaScanContext {
        override fun <T> context(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
            @Suppress("UNCHECKED_CAST")
            return this@fileScan as T
        }

        override val loaderArgs: MediaCursorLoaderArgs get() = args
        override val factory: MediaScanEntityFactory get() = MediaScanEntityFactory.file()
    }
}