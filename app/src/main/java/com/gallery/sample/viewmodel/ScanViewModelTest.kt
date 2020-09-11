package com.gallery.sample.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.gallery.scan.ScanViewModelFactory
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.args.audio.ScanAudioArgs
import com.gallery.scan.args.audio.scanAudioFactory
import com.gallery.scan.getScanAudioImpl

object ScanViewModelTest {

    fun testAudio(fragmentActivity: FragmentActivity) {
        val viewModel = ViewModelProvider(fragmentActivity, ScanViewModelFactory(fragmentActivity,
                ScanEntityFactory.scanAudioFactory(),
                ScanAudioArgs())).getScanAudioImpl()
        viewModel.scanLiveData.observe(fragmentActivity, {
            Log.i("ViewModelProviderAudio", "ViewModelProvider audio success:${it.entities}")
        })
        viewModel.resultLiveData.observe(fragmentActivity, {
            Log.i("ViewModelProviderAudio", "ViewModelProvider audio success:${it.entity}")
        })
        viewModel.errorLiveData.observe(fragmentActivity, {
            Log.i("ViewModelProviderAudio", "ViewModelProvider audio error${it.type}")
        })
        viewModel.scanMultiple(Bundle())
    }
}