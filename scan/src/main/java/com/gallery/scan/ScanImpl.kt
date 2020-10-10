package com.gallery.scan

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.listener.ScanListener
import com.gallery.scan.types.ResultType
import com.gallery.scan.types.postValueExpand

/**
 * @author y
 * @create 2019/2/27
 * 文件扫描工具类
 *
 * ViewModelProvider(fragment,
 *    ScanViewModelFactory(
 *      ownerFragment = fragment,
 *      factory = ScanEntityFactory.fileExpand(),
 *      args = scanFileArgs
 *    )
 *  )
 *  .scanFileImpl()
 *  .registerMultipleLiveData(fragment) { _, result -> }
 *  .scanMultiple(parentId.multipleFileExpand())
 *
 */
class ScanImpl<E : Parcelable>(private val scanView: ScanView) : ViewModel(), Scan<E> {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    internal val multipleLiveData = MutableLiveData<ScanMultipleResult<E>>()
    internal val singleLiveData = MutableLiveData<ScanSingleResult<E>>()
    internal val errorLiveData = MutableLiveData<ScanError>()
    private var scanListener: ScanListener<E>? = null
    private val objects: Any = scanView.scanOwnerGeneric()
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.scanOwnerGeneric())
    private val factory: ScanEntityFactory = scanView.scanEntityFactory
    private val loaderArgs: CursorLoaderArgs = scanView.scanCursorLoaderArgs
    private val context: Context by lazy {
        when (objects) {
            is Fragment -> objects.requireContext().applicationContext
            is FragmentActivity -> objects.applicationContext
            else -> scanView.scanContext.applicationContext
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
            if (!errorLiveData.postValueExpand(ScanError(ResultType.MULTIPLE))) {
                scanListener?.scanMultipleError()
            }
        }) {
            if (!multipleLiveData.postValueExpand(ScanMultipleResult(Bundle(args), it))) {
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
            if (!errorLiveData.postValueExpand(ScanError(ResultType.SINGLE))) {
                scanListener?.scanSingleError()
            }
        }) {
            if (!singleLiveData.postValueExpand(ScanSingleResult(Bundle(args), if (it.isEmpty()) null else it[0]))) {
                scanListener?.scanSingleSuccess(if (it.isEmpty()) null else it[0])
            }
            onCleared()
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }
}