package com.gallery.compat.internal.simple

import android.content.Context
import android.os.Bundle
import com.gallery.compat.R
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryPrevCallback
import com.gallery.core.delegate.IPrevDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.safeToastExpand

interface SimplePrevCallback : IGalleryPrevCallback {

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onChangedCheckBox() {
    }

    override fun onClickItemFileNotExist(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        context.getString(R.string.gallery_compat_prev_check_file_deleted).safeToastExpand(context)
    }

    override fun onClickItemBoxMaxCount(
        context: Context,
        bundle: GalleryBundle,
        scanEntity: ScanEntity
    ) {
        context.getString(R.string.gallery_compat_check_max).safeToastExpand(context)
    }

    override fun onPrevCreated(
        delegate: IPrevDelegate,
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
    }

}