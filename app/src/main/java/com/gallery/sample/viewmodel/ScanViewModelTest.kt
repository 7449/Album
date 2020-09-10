package com.gallery.sample.viewmodel

import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.ScanImpl
import com.gallery.scan.ScanViewModelFactory
import com.gallery.scan.types.SCAN_ALL

object ScanViewModelTest {

    fun test(fragmentActivity: FragmentActivity) {
        runCatching { ViewModelProvider(fragmentActivity).get(ScanImpl::class.java) }
                .onSuccess { Log.i("ViewModelProvider", "ViewModelProvider success") }
                .onFailure { Log.e("ViewModelProvider", "ViewModelProvider failure:${it.message}") }
        val viewModel = ViewModelProvider(fragmentActivity, ScanViewModelFactory(fragmentActivity, scanType = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE))).get(ScanImpl::class.java)
        viewModel.scanLiveData.observe(fragmentActivity, {
            Log.i("ViewModelProvider", "ViewModelProvider success:${it.entities}")
        })
        viewModel.resultLiveData.observe(fragmentActivity, {
            Log.i("ViewModelProvider", "ViewModelProvider success:${it.entity}")
        })
        viewModel.errorLiveData.observe(fragmentActivity, {
            Log.i("ViewModelProvider", "ViewModelProvider error${it.type}")
        })
        viewModel.scanParent(SCAN_ALL)
    }
}