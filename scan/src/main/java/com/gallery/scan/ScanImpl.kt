package com.gallery.scan

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.Columns
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
 *       override fun scanType(): Int {
 *          return ScanType.IMAGE
 *       }
 *   }
 *   ViewModelProvider(fragmentActivity, ScanViewModelFactory(scanView)).get(ScanImpl::class.java).scanParent(SCAN_ALL)
 *
 *   or
 *
 *  class ScanViewModelFactory(
 *      private val fragmentActivity: FragmentActivity,
 *      private val scanType: Int
 *  ) : ViewModelProvider.Factory {
 *
 *      override fun <T : ViewModel> create(modelClass: Class<T>): T {
 *          return fragmentActivity.scanViewModel(scanType) as T
 *      }
 *
 *  }
 *
 *  val viewModel = ViewModelProvider(fragmentActivity, ScanViewModelFactory(fragmentActivity, ScanType.IMAGE))
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
class ScanImpl(private val scanView: ScanView) : ViewModel(), Scan {

    companion object {
        private const val SCAN_LOADER_ID = 111
    }

    val scanLiveData = MutableLiveData<ScanResult>()
    val resultLiveData = MutableLiveData<ValueResult>()
    val errorLiveData = MutableLiveData<ScanError>()
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.scanContext)
    private val context: Context = scanView.scanContext.applicationContext
    private val scanType: Int = scanView.scanType()
    private val scanField: String = scanView.scanSortField()
    private val scanSort: String = scanView.scanSort()

    private fun createScanParentArgs(parentId: Long): Bundle {
        return Bundle().apply {
            putLong(Columns.PARENT, parentId)
            putSerializable(Columns.SCAN_RESULT, Result.MULTIPLE)
            putString(Columns.SORT, scanSort)
            putString(Columns.SORT_FIELD, scanField)
            putInt(Columns.SCAN_TYPE, scanType)
        }
    }

    private fun createScanResultArgs(id: Long): Bundle {
        return Bundle().apply {
            putLong(Columns.ID, id)
            putSerializable(Columns.SCAN_RESULT, Result.SINGLE)
            putString(Columns.SORT, scanSort)
            putString(Columns.SORT_FIELD, scanField)
            putInt(Columns.SCAN_TYPE, scanType)
        }
    }

    override fun scanParent(parentId: Long) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanParentArgs(parentId), ScanTask(context, {
            scanView.scanError()
            errorLiveData.postValueExpand(ScanError(Result.MULTIPLE))
        }) {
            scanView.scanSuccess(it)
            scanLiveData.postValueExpand(ScanResult(parentId, it))
            onCleared()
        })
    }

    override fun scanResult(id: Long) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, createScanResultArgs(id), ScanTask(context, {
            scanView.resultError()
            errorLiveData.postValueExpand(ScanError(Result.SINGLE))
        }) {
            scanView.resultSuccess(if (it.isEmpty()) null else it[0])
            resultLiveData.postValueExpand(ValueResult(id, if (it.isEmpty()) null else it[0]))
            onCleared()
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }
}