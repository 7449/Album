package com.gallery.scan

import android.content.Context
import android.os.Bundle
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
 *
 *   class ScanViewModelFactory(private val scanView: ScanView) : ViewModelProvider.Factory {
 *
 *       override fun <T : ViewModel> create(modelClass: Class<T>): T {
 *          return ScanImpl(scanView) as T
 *       }
 *
 *   }
 *
 *   val scanView = object : ScanView {
 *       override val scanContext: FragmentActivity
 *          get() = fragmentActivity
 *
 *       override fun scanSuccess(arrayList: ArrayList<ScanEntity>) {
 *          Log.i("ViewModelProvider", "ViewModelProvider success:${arrayList}")
 *       }
 *
 *       override fun scanType(): IntArray {
 *          return intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
 *       }
 *   }
 *   ViewModelProvider(fragmentActivity, ScanViewModelFactory(scanView)).get(ScanImpl::class.java).scanParent(SCAN_ALL)
 *
 *   or
 *
 *  class ScanViewModelFactory(
 *      private val fragmentActivity: FragmentActivity,
 *      private val scanType: IntArray
 *  ) : ViewModelProvider.Factory {
 *
 *      override fun <T : ViewModel> create(modelClass: Class<T>): T {
 *          return fragmentActivity.scanViewModel(scanType) as T
 *      }
 *
 *  }
 *
 *  val viewModel = ViewModelProvider(fragmentActivity, ScanViewModelFactory(fragmentActivity, intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)))
 *                  .get(ScanImpl::class.java)
 *
 *  viewModel.scanLiveData.observe(fragmentActivity, {
 *      Log.i("ViewModelProvider", "ViewModelProvider success:${it.entities}")
 *  })
 *
 *  viewModel.resultLiveData.observe(fragmentActivity, {
 *      Log.i("ViewModelProvider", "ViewModelProvider success:${it.entity}")
 *  })
 *
 *  viewModel.scanParent(SCAN_ALL)
 *
 */
class ScanImpl<ENTITY : ScanEntityFactory>(private val scanView: ScanView<ENTITY>) : ViewModel(), Scan {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    internal val multipleLiveData = MutableLiveData<ScanMultipleResult<ENTITY>>()
    internal val singleLiveData = MutableLiveData<ScanSingleResult<ENTITY>>()
    internal val errorLiveData = MutableLiveData<ScanError>()
    private val objects: Any = scanView.scanOwnerGeneric()
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.scanOwnerGeneric())
    private val factory: ScanEntityFactory = scanView.scanEntityFactory
    private val loaderArgs: CursorLoaderArgs = scanView.scanCursorLoaderArgs
    private val context: Context by lazy {
        when (objects) {
            is Fragment -> objects.requireContext()
            is FragmentActivity -> objects
            else -> scanView.scanContext
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
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanMultipleArgs(args), ScanTask<ENTITY>(context, factory, {
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
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanSingleArgs(args), ScanTask<ENTITY>(context, factory, {
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