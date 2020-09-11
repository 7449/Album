package com.gallery.sample.viewmodel

import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.database.getStringOrDefault
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.ScanImpl
import com.gallery.scan.ScanViewModelFactory
import com.gallery.scan.args.IScanEntityFactory
import com.gallery.scan.args.ScanParameter
import com.gallery.scan.getScanMinimumImpl
import com.gallery.scan.types.SCAN_ALL

object ScanViewModelTest {

    fun testAudio(fragmentActivity: FragmentActivity) {
//        val scanParameter = ScanParameter(scanUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, scanType = intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO))
//        val scanFactoryCreate: IScanEntityFactory = object : IScanEntityFactory {
//            override fun onCreateCursor(cursor: Cursor): IScanEntityFactory {
//                Log.i("ViewModelProviderAudio", cursor.getStringOrDefault(MediaStore.Audio.Media.TITLE))
//                return this
//            }
//        }
//        val viewModel = ViewModelProvider(fragmentActivity, ScanViewModelFactory(fragmentActivity, scanFactoryCreate, scanParameter)).getScanMinimumImpl()
//        viewModel.scanLiveData.observe(fragmentActivity, {
//            Log.i("ViewModelProvider", "ViewModelProvider audio success:${it.entities}")
//        })
//        viewModel.resultLiveData.observe(fragmentActivity, {
//            Log.i("ViewModelProvider", "ViewModelProvider audio success:${it.entity}")
//        })
//        viewModel.errorLiveData.observe(fragmentActivity, {
//            Log.i("ViewModelProvider", "ViewModelProvider audio error${it.type}")
//        })
//        viewModel.scanParent(SCAN_ALL)
    }

    fun test(fragmentActivity: FragmentActivity) {
        runCatching { ViewModelProvider(fragmentActivity).get(ScanImpl::class.java) }
                .onSuccess { Log.i("ViewModelProvider", "ViewModelProvider success") }
                .onFailure { Log.e("ViewModelProvider", "ViewModelProvider failure:${it.message}") }
        val viewModel = ViewModelProvider(fragmentActivity, ScanViewModelFactory(fragmentActivity)).getScanMinimumImpl()
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