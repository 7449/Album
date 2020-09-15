package com.gallery.scan

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.CursorLoaderArgs.Companion.putCursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.types.Result
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
class ScanImpl<E : Parcelable>(private val scanView: ScanView<E>) : ViewModel(), Scan {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    internal val multipleLiveData = MutableLiveData<ScanMultipleResult<E>>()
    internal val singleLiveData = MutableLiveData<ScanSingleResult<E>>()
    internal val errorLiveData = MutableLiveData<ScanError>()
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

    private fun createScanMultipleArgs(bundle: Bundle): Bundle {
        return Bundle().apply {
            putAll(bundle)
            putSerializable(MediaStore.Files.FileColumns.MIME_TYPE, Result.MULTIPLE)
            putCursorLoaderArgs(loaderArgs)
        }
    }

    private fun createScanSingleArgs(bundle: Bundle): Bundle {
        return Bundle().apply {
            putAll(bundle)
            putSerializable(MediaStore.Files.FileColumns.MIME_TYPE, Result.SINGLE)
            putCursorLoaderArgs(loaderArgs)
        }
    }

    override fun scanMultiple(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanMultipleArgs(args), ScanTask<E>(context, factory, {
            scanView.scanMultipleError()
            errorLiveData.postValueExpand(ScanError(Result.MULTIPLE))
        }) {
            scanView.scanMultipleSuccess(it)
            multipleLiveData.postValueExpand(ScanMultipleResult(Bundle(args), it))
            onCleared()
        })
    }

    override fun scanSingle(args: Bundle) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanSingleArgs(args), ScanTask<E>(context, factory, {
            scanView.scanSingleError()
            errorLiveData.postValueExpand(ScanError(Result.SINGLE))
        }) {
            scanView.scanSingleSuccess(if (it.isEmpty()) null else it[0])
            singleLiveData.postValueExpand(ScanSingleResult(Bundle(args), if (it.isEmpty()) null else it[0]))
            onCleared()
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }
}