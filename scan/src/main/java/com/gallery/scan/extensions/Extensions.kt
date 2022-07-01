package com.gallery.scan.extensions

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.gallery.scan.Types
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.CursorLoaderArgs.Companion.putCursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.callback.ScanCore

/** 是否是扫描全部的Id */
val Long.isScanAllExpand: Boolean get() = this == Types.Scan.ALL

/** 是否是空扫描 */
val Long.isScanNoNeExpand: Boolean get() = this == Types.Scan.NONE

/** 为[Fragment]创建一个[ScanCore] */
fun Fragment.scanCore(
        factory: ScanEntityFactory,
        args: CursorLoaderArgs
): ScanCore {
    return object : ScanCore {
        override val scanOwner: LifecycleOwner get() = this@scanCore
        override val loaderArgs: CursorLoaderArgs get() = args
        override val factory: ScanEntityFactory get() = factory
    }
}

/** 为[FragmentActivity]创建一个[ScanCore] */
fun FragmentActivity.scanCore(
        factory: ScanEntityFactory,
        args: CursorLoaderArgs
): ScanCore {
    return object : ScanCore {
        override val scanOwner: LifecycleOwner get() = this@scanCore
        override val loaderArgs: CursorLoaderArgs get() = args
        override val factory: ScanEntityFactory get() = factory
    }
}

/** 创建多个数据参数类 */
fun CursorLoaderArgs.createScanMultipleArgs(bundle: Bundle): Bundle {
    return Bundle().apply {
        putAll(bundle)
        putString(MediaStore.Files.FileColumns.MIME_TYPE, Types.Result.MULTIPLE)
        putCursorLoaderArgs(this@createScanMultipleArgs)
    }
}

/** 创建单个数据参数类 */
fun CursorLoaderArgs.createScanSingleArgs(bundle: Bundle): Bundle {
    return Bundle().apply {
        putAll(bundle)
        putString(MediaStore.Files.FileColumns.MIME_TYPE, Types.Result.SINGLE)
        putCursorLoaderArgs(this@createScanSingleArgs)
    }
}

/** 获取可使用的多个扫描Bundle */
fun Long.multipleScanExpand(): Bundle =
        Bundle().apply { putLong(MediaStore.Files.FileColumns.PARENT, this@multipleScanExpand) }

/** 获取可使用的单个扫描Bundle */
fun Long.singleScanExpand(): Bundle =
        Bundle().apply { putLong(MediaStore.Files.FileColumns._ID, this@singleScanExpand) }