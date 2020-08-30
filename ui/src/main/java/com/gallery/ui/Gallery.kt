package com.gallery.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gallery.core.GalleryBundle
import com.gallery.core.GalleryBundle.Companion.putGalleryBundle
import com.gallery.ui.page.GalleryActivity

class Gallery(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        private val galleryLauncher: ActivityResultLauncher<Intent>,
        private val galleryBundle: GalleryBundle = GalleryBundle(),
        private val galleryUiBundle: GalleryUiBundle = GalleryUiBundle(),
        private val galleryOption: Bundle = Bundle.EMPTY,
        private val galleryPrevOption: Bundle = Bundle.EMPTY,
        private val clz: Class<*> = GalleryActivity::class.java) {

    private val fragmentActivity: FragmentActivity

    init {
        when {
            fragment != null -> {
                fragmentActivity = fragment.requireActivity()
                startFragment()
            }
            activity != null -> {
                fragmentActivity = activity
                startActivity()
            }
            else -> throw KotlinNullPointerException("fragment and activity == null")
        }
    }

    private fun launchIntent(): Intent {
        return Intent(fragmentActivity, clz).apply {
            putExtras(Bundle().apply {
                putGalleryBundle(galleryBundle)
                putParcelable(UIResult.UI_CONFIG, galleryUiBundle)
                putBundle(UIResult.UI_GALLERY_CONFIG, galleryOption)
                putBundle(UIResult.UI_RESULT_CONFIG, galleryPrevOption)
            })
        }
    }

    private fun startActivity() {
        galleryLauncher.launch(launchIntent())
    }

    private fun startFragment() {
        galleryLauncher.launch(launchIntent())
    }
}