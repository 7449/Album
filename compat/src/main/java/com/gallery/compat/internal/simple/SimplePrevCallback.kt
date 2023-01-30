package com.gallery.compat.internal.simple

import android.content.Context
import android.os.Bundle
import com.gallery.compat.R
import com.gallery.core.GalleryConfigs
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.toast

interface SimplePrevCallback : IGalleryPrevCallback {

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onCheckBoxChanged() {
    }

    override fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryConfigs,
        scanEntity: ScanEntity
    ) {
        context.getString(R.string.gallery_compat_prev_check_file_deleted).toast(context)
    }

    override fun onClickItemMaxCount(
        context: Context,
        bundle: GalleryConfigs,
        scanEntity: ScanEntity
    ) {
        context.getString(R.string.gallery_compat_check_max).toast(context)
    }

    override fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryConfigs,
        savedInstanceState: Bundle?
    ) {
    }

}