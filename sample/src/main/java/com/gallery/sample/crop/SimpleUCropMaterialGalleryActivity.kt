package com.gallery.sample.crop

import com.gallery.core.crop.ICrop
import com.gallery.ui.material.activity.MaterialGalleryActivity

class SimpleUCropMaterialGalleryActivity : MaterialGalleryActivity() {
    override val cropImpl: ICrop
        get() = SimpleUCropImpl()
}