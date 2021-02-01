package com.gallery.scan.callback

import android.os.Bundle
import android.provider.MediaStore
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.CursorLoaderArgs.Companion.putCursorLoaderArgs
import com.gallery.scan.impl.ScanImpl
import com.gallery.scan.result.Result
import com.gallery.scan.types.ResultType

interface Scan<E> {

    /** 扫描多个数据 */
    fun scanMultiple(args: Bundle)

    /** 扫描单个数据 */
    fun scanSingle(args: Bundle)

    /** 注册回调 */
    fun registerScanResource(action: (result: Result<E>) -> Unit): ScanImpl<E>

    /** 释放资源 */
    fun scanCleared()

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