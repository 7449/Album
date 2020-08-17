package com.gallery.sample.custom

import com.gallery.core.crop.ICrop
import com.gallery.ui.crop.CropperImpl
import com.gallery.ui.page.GalleryActivity

class CustomCropActivity : GalleryActivity() {
    override val cropImpl: ICrop?
        get() = CropperImpl()
}