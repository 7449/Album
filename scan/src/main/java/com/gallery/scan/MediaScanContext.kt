package com.gallery.scan

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import com.gallery.scan.args.MediaCursorLoaderArgs
import com.gallery.scan.args.MediaScanEntityFactory

interface MediaScanContext {

    val loaderArgs: MediaCursorLoaderArgs

    val factory: MediaScanEntityFactory

    fun <T> context(): T where T : LifecycleOwner, T : ViewModelStoreOwner

}