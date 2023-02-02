package com.gallery.compat.internal.simple

import android.os.Bundle
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity

interface SimplePrevCallback : IGalleryPrevCallback {

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onSelectMultipleMaxCount() {
    }

    override fun onSelectMultipleFileChanged(position: Int, entity: ScanEntity) {
    }

    override fun onSelectMultipleFileNotExist(entity: ScanEntity) {
    }

    override fun onPrevCreated(delegate: IPrevDelegate, saveState: Bundle?) {
    }

}