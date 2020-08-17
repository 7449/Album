package com.gallery.scan

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.Columns

/**
 * @author y
 * @create 2019/2/27
 * 图库扫描工具类
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
 *  viewModel.scanLiveData.observe(fragmentActivity, Observer {
 *      Log.i("ViewModelProvider", "ViewModelProvider success:${it.entities}")
 *  })
 *
 *  viewModel.resultLiveData.observe(fragmentActivity, Observer {
 *      Log.i("ViewModelProvider", "ViewModelProvider success:${it.entity}")
 *  })
 *
 *  viewModel.scanParent(SCAN_ALL)
 *
 */
class ScanImpl(private val scanView: ScanView) : ViewModel(), Scan {

    val scanLiveData = MutableLiveData<ScanResult>()
    val resultLiveData = MutableLiveData<ValueResult>()
    private val loaderManager: LoaderManager = LoaderManager.getInstance(scanView.scanContext)
    private val context: Context = scanView.scanContext.applicationContext
    private val scanType: Int = scanView.scanType()
    private val scanField: String = scanView.scanSortField()
    private val scanSort: String = scanView.scanSort()

    override fun scanParent(parentId: Long) {
        if (loaderManager.hasRunningLoaders()) {
            return
        }
        loaderManager.restartLoader(SCAN_LOADER_ID, Bundle().apply {
            putLong(Columns.PARENT, parentId)
            putString(Columns.SORT, scanSort)
            putString(Columns.SORT_FIELD, scanField)
            putInt(Columns.SCAN_TYPE, scanType)
        }, ScanTask(context) {
            scanView.scanSuccess(it)
            scanLiveData.postValue(ScanResult(parentId, it))
            onCleared()
        })
    }

    override fun scanResult(id: Long) {
        loaderManager.restartLoader(SCAN_LOADER_ID, Bundle().apply {
            putLong(Columns.ID, id)
            putString(Columns.SORT, scanSort)
            putString(Columns.SORT_FIELD, scanField)
            putInt(Columns.SCAN_TYPE, scanType)
        }, ScanTask(context) {
            scanView.resultSuccess(if (it.isEmpty()) null else it[0])
            resultLiveData.postValue(ValueResult(id, if (it.isEmpty()) null else it[0]))
            onCleared()
        })
    }

    override fun onCleared() {
        loaderManager.destroyLoader(SCAN_LOADER_ID)
    }
}