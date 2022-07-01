package com.gallery.scan.impl

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.loader.app.LoaderManager
import com.gallery.scan.callback.Scan
import com.gallery.scan.callback.ScanCore
import com.gallery.scan.extensions.createScanMultipleArgs
import com.gallery.scan.extensions.createScanSingleArgs
import com.gallery.scan.result.Result
import com.gallery.scan.task.ScanTask

/**
 * @author y
 * @create 2019/2/27
 * 文件扫描工具类
 */
class ScanImpl<E>(
        private val scanCore: ScanCore,
        private val action: Result<E>.() -> Unit
) : Scan<E>, LifecycleEventObserver {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    private val objects: Any = scanCore.scanOwnerGeneric()
    private val loaderManager = LoaderManager.getInstance(scanCore.scanOwnerGeneric())
    private val factory = scanCore.factory
    private val loaderArgs = scanCore.loaderArgs
    private val context by lazy {
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
            cleared()
        }
    }

    override fun scanMultiple(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(
                SCAN_LOADER_ID,
                loaderArgs.createScanMultipleArgs(args),
                ScanTask<E>(context, factory) {
                    action.invoke(Result.Multiple(it))
                    cleared()
                })
    }

    override fun scanSingle(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(
                SCAN_LOADER_ID,
                loaderArgs.createScanSingleArgs(args),
                ScanTask<E>(context, factory) {
                    action.invoke(Result.Single(it.firstOrNull()))
                    cleared()
                })
    }

    override fun cleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }

}