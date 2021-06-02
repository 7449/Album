package com.gallery.sample.crop

import com.gallery.core.crop.ICrop
import com.gallery.ui.material.activity.GalleryActivity

class SimpleUCropGalleryActivity : GalleryActivity() {
    override val cropImpl: ICrop
        get() = SimpleUCropImpl()
}