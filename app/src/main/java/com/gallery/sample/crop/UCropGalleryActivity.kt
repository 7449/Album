package com.gallery.sample.crop

import com.gallery.core.crop.ICrop
import com.gallery.ui.activity.GalleryActivity
import com.gallery.ui.finder.GalleryFinderAdapter

class UCropGalleryActivity : GalleryActivity() {

    override val cropImpl: ICrop?
        get() = UCropImpl(uiConfig)
    override val newFinderAdapter: GalleryFinderAdapter
        get() = super.newFinderAdapter
}