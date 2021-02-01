package com.gallery.scan.impl

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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
class ScanImpl<E>(private val scanCore: ScanCore) : Scan<E>, LifecycleEventObserver {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    private var scanAction: ((result: Result<*>) -> Unit)? = null
    private val objects: Any = scanCore.scanOwnerGeneric()
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanCore.scanOwnerGeneric())
    private val factory: ScanEntityFactory = scanCore.scanEntityFactory
    private val loaderArgs: CursorLoaderArgs = scanCore.scanCursorLoaderArgs
    private val context: Context by lazy {
        when (objects) {
            is Fragment -> objects.requireContext().applicationContext
            is FragmentActivity -> objects.applicationContext
            else -> throw KotlinNullPointerException("context == null")
        }
    }

    init {
        scanCore.scanOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            scanCore.scanOwner.lifecycle.removeObserver(this)
            scanAction = null
            scanCleared()
        }
    }

    override fun registerScanResource(action: (result: Result<E>) -> Unit): ScanImpl<E> {
        @Suppress("UNCHECKED_CAST")
        scanAction = action as (result: Result<*>) -> Unit
        return this
    }

    override fun scanMultiple(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanMultipleArgs(args, loaderArgs), ScanTask<E>(context, factory, {
            scanAction?.invoke(Result.Error(ResultType.MULTIPLE))
            scanCleared()
        }) {
            scanAction?.invoke(Result.Multiple(it))
            scanCleared()
        })
    }

    override fun scanSingle(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanSingleArgs(args, loaderArgs), ScanTask<E>(context, factory, {
            scanAction?.invoke(Result.Error(ResultType.SINGLE))
            scanCleared()
        }) {
            scanAction?.invoke(Result.Single(if (it.isEmpty()) null else it[0]))
            scanCleared()
        })
    }

    override fun scanCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }

}