package com.gallery.scan.callback

import android.os.Bundle
import android.provider.MediaStore
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.CursorLoaderArgs.Companion.putCursorLoaderArgs
import com.gallery.scan.impl.ScanImpl
import com.gallery.scan.types.ResultType

/**
 * 实现:[ScanImpl]
 */
interface Scan<E> {

    /** 注册回调 */
    fun registerScanCall(scanCall: ScanCall<E>)

    /** 反注册回调 */
    fun unregisterScanCall()

    /** 扫描多个数据 */
    fun scanMultiple(args: Bundle)

    /** 扫描单个数据 */
    fun scanSingle(args: Bundle)

    /** 创建多个数据参数类 */
    fun createScanMultipleArgs(bundle: Bundle, args: CursorLoaderArgs): Bundle {
        return Bundle().apply {
            putAll(bundle)
            putString(MediaStore.Files.FileColumns.MIME_TYPE, ResultType.MULTIPLE)
            putCursorLoaderArgs(args)
        }
    }

    /** 创建单个数据参数类 */
    fun createScanSingleArgs(bundle: Bundle, args: CursorLoaderArgs): Bundle {
        return Bundle().apply {
            putAll(bundle)
            putString(MediaStore.Files.FileColumns.MIME_TYPE, ResultType.SINGLE)
            putCursorLoaderArgs(args)
        }
    }

}