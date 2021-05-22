package com.gallery.scan.extensions

import android.database.Cursor
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
val Long.isScanAllExpand: Boolean get() = this == Types.Scan.SCAN_ALL

/** 是否是空扫描 */
val Long.isScanNoNeExpand: Boolean get() = this == Types.Scan.SCAN_NONE

/** 为[Fragment]创建一个[ScanCore] */
fun Fragment.scanCore(
    factory: ScanEntityFactory,
    args: CursorLoaderArgs
): ScanCore {
    return object : ScanCore {
        override val scanOwner: LifecycleOwner get() = this@scanCore
        override val scanCursorLoaderArgs: CursorLoaderArgs get() = args
        override val scanEntityFactory: ScanEntityFactory get() = factory
    }
}

/** 为[FragmentActivity]创建一个[ScanCore] */
fun FragmentActivity.scanCore(
    factory: ScanEntityFactory,
    args: CursorLoaderArgs
): ScanCore {
    return object : ScanCore {
        override val scanOwner: LifecycleOwner get() = this@scanCore
        override val scanCursorLoaderArgs: CursorLoaderArgs get() = args
        override val scanEntityFactory: ScanEntityFactory get() = factory
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

fun Cursor?.getIntOrDefault(columnName: String, defaultValue: Int = 0): Int =
    getValueOrDefault(columnName, { defaultValue }) { it.getInt(it.getColumnIndex(columnName)) }

fun Cursor?.getLongOrDefault(columnName: String, defaultValue: Long = 0.toLong()): Long =
    getValueOrDefault(columnName, { defaultValue }) { it.getLong(it.getColumnIndex(columnName)) }

fun Cursor?.getStringOrDefault(columnName: String, defaultValue: String = ""): String =
    getValueOrDefault(columnName, { defaultValue }) { it.getString(it.getColumnIndex(columnName)) }

inline fun <T> Cursor?.getValueOrDefault(name: String, valueNull: () -> T, a: (c: Cursor) -> T): T =
    if (this?.isNull(getColumnIndex(name)) == false) a.invoke(this) else valueNull.invoke()