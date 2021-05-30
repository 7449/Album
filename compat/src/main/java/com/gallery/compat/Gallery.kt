package com.gallery.compat

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.UIGalleryArgs.Companion.putArgs
import com.gallery.core.GalleryBundle

class Gallery(
    activity: FragmentActivity? = null,
    fragment: Fragment? = null,
    private val galleryLauncher: ActivityResultLauncher<Intent>,
    private val uiGalleryArgs: UIGalleryArgs,
    private val clz: Class<*>,
) {

    companion object {
        fun newInstance(
            activity: FragmentActivity? = null,
            fragment: Fragment? = null,
            launcher: ActivityResultLauncher<Intent>,
            bundle: GalleryBundle = GalleryBundle(),
            compatBundle: GalleryCompatBundle = GalleryCompatBundle(),
            option: Bundle = Bundle.EMPTY,
            prevOption: Bundle = Bundle.EMPTY,
            clz: Class<*>,
        ): Gallery {
            return Gallery(
                activity,
                fragment,
                launcher,
                UIGalleryArgs(bundle, compatBundle, option, prevOption),
                clz
            )
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