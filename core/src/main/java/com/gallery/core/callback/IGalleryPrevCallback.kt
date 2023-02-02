package com.gallery.core.callback

import android.os.Bundle
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity

interface IGalleryPrevCallback {

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
    fun onPageSelected(position: Int)
    fun onPageScrollStateChanged(state: Int)

    fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?)
    fun onSelectMultipleFileNotExist(entity: ScanEntity)
    fun onSelectMultipleMaxCount()
    fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity)

}