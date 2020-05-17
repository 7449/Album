package com.gallery.sample.custom

import com.gallery.ui.page.GalleryActivity

class CustomCameraActivity : GalleryActivity() {

    override fun onCustomCamera(): Boolean {
        return true
    }

}