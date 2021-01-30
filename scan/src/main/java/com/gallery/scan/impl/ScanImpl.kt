package com.gallery.scan.impl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.callback.Scan
import com.gallery.scan.callback.ScanCore
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

        @Suppress("UNCHECKED_CAST")
        fun <E> ScanImpl<E>.registerLiveData(owner: LifecycleOwner, action: (type: Result<E>) -> Unit) = also {
            resultLiveData.observe(owner) { action.invoke(it as Result<E>) }
        }

    }

    private val resultLiveData = MutableLiveData<Result<*>>()
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

    private val multipleError = { resultLiveData.value = Result.Error(ResultType.MULTIPLE) }

    private val singleError = { resultLiveData.value = Result.Error(ResultType.SINGLE) }

    override fun scanMultiple(args: Bundle) {
        onCleared()
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanMultipleArgs(args, loaderArgs), ScanTask<E>(context, factory, multipleError) {
            resultLiveData.value = Result.Multiple(it)
        })
    }

    override fun scanSingle(args: Bundle) {
        onCleared()
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanSingleArgs(args, loaderArgs), ScanTask<E>(context, factory, singleError) {
            resultLiveData.value = Result.Single(if (it.isEmpty()) null else it[0])
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }

}