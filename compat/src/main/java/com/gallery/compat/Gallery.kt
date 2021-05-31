package com.gallery.compat

import android.content.Intent
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gallery.compat.activity.args.GalleryCompatArgs
import com.gallery.compat.activity.args.GalleryCompatArgs.Companion.putArgs
import com.gallery.core.GalleryBundle

open class Gallery(
    activity: FragmentActivity? = null,
    fragment: Fragment? = null,
    private val launcher: ActivityResultLauncher<Intent>,
    private val args: GalleryCompatArgs,
    private val clz: Class<*>,
) {

    companion object {
        fun newInstance(
            activity: FragmentActivity? = null,
            fragment: Fragment? = null,
            launcher: ActivityResultLauncher<Intent>,
            bundle: GalleryBundle = GalleryBundle(),
            compatBundle: GalleryCompatBundle = GalleryCompatBundle(),
            customBundle: Parcelable? = null,
            clz: Class<*>,
        ): Gallery {
            return Gallery(
                activity,
                fragment,
                launcher,
                GalleryCompatArgs(bundle, compatBundle, customBundle),
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
        return Intent(fragmentActivity, clz).apply { putExtras(args.putArgs()) }
    }

    private fun startActivity() {
        launcher.launch(launchIntent())
    }

    private fun startFragment() {
        launcher.launch(launchIntent())
    }

}