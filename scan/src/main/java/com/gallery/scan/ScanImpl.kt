package com.gallery.scan

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.callback.Scan
import com.gallery.scan.callback.ScanCore
import com.gallery.scan.callback.ScanListener
import com.gallery.scan.extensions.postValueExpand
import com.gallery.scan.result.Result
import com.gallery.scan.task.ScanTask
import com.gallery.scan.types.ResultType

/**
 * @author y
 * @create 2019/2/27
 * 文件扫描工具类
 */
class ScanImpl<E>(private val scanCore: ScanCore) : ViewModel(), Scan<E> {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    internal val resultLiveData = MutableLiveData<Result<*>>()
    private var scanListener: ScanListener<E>? = null
    private val objects: Any = scanCore.scanOwnerGeneric()
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanCore.scanOwnerGeneric())
    private val factory: ScanEntityFactory = scanCore.scanEntityFactory
    private val loaderArgs: CursorLoaderArgs = scanCore.scanCursorLoaderArgs
    private val context: Context by lazy {
        when (objects) {
            is Fragment -> objects.requireContext().applicationContext
            is FragmentActivity -> objects.applicationContext
            else -> scanCore.scanContext.applicationContext
        }
    }

    override fun registerScanListener(scanListener: ScanListener<E>) {
        this.scanListener = scanListener
    }

    override fun unregisterScanListener() {
        this.scanListener = null
    }

    override fun scanMultiple(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanMultipleArgs(args, loaderArgs), ScanTask<E>(context, factory, {
            if (!resultLiveData.postValueExpand(Result.Error(ResultType.MULTIPLE))) {
                scanListener?.scanMultipleError()
            }
        }) {
            if (!resultLiveData.postValueExpand(Result.Multiple(it))) {
                scanListener?.scanMultipleSuccess(it)
            }
            onCleared()
        })
    }

    override fun scanSingle(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanSingleArgs(args, loaderArgs), ScanTask<E>(context, factory, {
            if (!resultLiveData.postValueExpand(Result.Error(ResultType.SINGLE))) {
                scanListener?.scanSingleError()
            }
        }) {
            if (!resultLiveData.postValueExpand(Result.Single(if (it.isEmpty()) null else it[0]))) {
                scanListener?.scanSingleSuccess(if (it.isEmpty()) null else it[0])
            }
            onCleared()
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }
}