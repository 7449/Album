package com.gallery.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gallery.core.GalleryBundle
import com.gallery.ui.UIGalleryArgs.Companion.putArgs
import com.gallery.ui.activity.GalleryActivity

class Gallery(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        private val galleryLauncher: ActivityResultLauncher<Intent>,
        private val uiGalleryArgs: UIGalleryArgs,
        private val clz: Class<*> = GalleryActivity::class.java,
) {

    companion object {
        fun newInstance(
                activity: FragmentActivity? = null,
                fragment: Fragment? = null,
                galleryLauncher: ActivityResultLauncher<Intent>,
                galleryBundle: GalleryBundle = GalleryBundle(),
                galleryUiBundle: GalleryUiBundle = GalleryUiBundle(),
                galleryOption: Bundle = Bundle.EMPTY,
                galleryPrevOption: Bundle = Bundle.EMPTY,
                clz: Class<*> = GalleryActivity::class.java,
        ): Gallery {
            return Gallery(activity, fragment, galleryLauncher, UIGalleryArgs(galleryBundle, galleryUiBundle, galleryOption, galleryPrevOption), clz)
        }
    }

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
        return Intent(fragmentActivity, clz).apply { putExtras(uiGalleryArgs.putArgs()) }
    }

    private fun startActivity() {
        galleryLauncher.launch(launchIntent())
    }

    private fun startFragment() {
        galleryLauncher.launch(launchIntent())
    }
}