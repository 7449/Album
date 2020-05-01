package com.gallery.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.os.getIntExpand
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableExpand
import androidx.kotlin.expand.os.orEmptyExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGallery
import com.gallery.ui.activity.GalleryActivity

class Gallery(
        activity: FragmentActivity? = null,
        fragment: Fragment? = null,
        private val galleryBundle: GalleryBundle = GalleryBundle(),
        private val galleryUiBundle: GalleryUiBundle = GalleryUiBundle(),
        private val clz: Class<*> = GalleryActivity::class.java,
        private val galleryListener: GalleryListener
) {

    private val fragmentActivity: FragmentActivity

    init {
        when {
            fragment != null -> {
                fragmentActivity = fragment.requireActivity()
                startFragment(fragment)
            }
            activity != null -> {
                fragmentActivity = activity
                startActivity(activity)
            }
            else -> throw NullPointerException("fragment and activity == null")
        }
    }

    private val startActivityForResult: ActivityResultContracts.StartActivityForResult
        get() = ActivityResultContracts.StartActivityForResult()

    private val activityResult: ActivityResultCallback<ActivityResult>
        get() = ActivityResultCallback<ActivityResult> { intent ->
            if (intent.resultCode == Activity.RESULT_OK) {
                val bundleExpand = intent.data?.extras.orEmptyExpand()
                when (bundleExpand.getIntExpand(UIResult.FRAGMENT_RESULT_TYPE)) {
                    UIResult.FRAGMENT_RESULT_CROP -> {
                        galleryListener.onGalleryCropResource(fragmentActivity, bundleExpand.getParcelableExpand(UIResult.FRAGMENT_RESULT_URI))
                    }
                    UIResult.FRAGMENT_RESULT_RESOURCE -> {
                        galleryListener.onGalleryResource(fragmentActivity, bundleExpand.getParcelableExpand(UIResult.FRAGMENT_RESULT_ENTITY))
                    }
                    UIResult.FRAGMENT_RESULT_RESOURCES -> {
                        galleryListener.onGalleryResources(fragmentActivity, bundleExpand.getParcelableArrayListExpand(UIResult.FRAGMENT_RESULT_ENTITIES))
                    }
                }
            } else if (intent.resultCode == Activity.RESULT_CANCELED) {
                galleryListener.onGalleryCancel(fragmentActivity)
            }
        }

    private fun launchIntent(context: Context): Intent {
        return Intent(context, clz).apply {
            putExtras(Bundle().apply {
                putParcelable(IGallery.GALLERY_START_CONFIG, galleryBundle)
                putParcelable(UIResult.UI_CONFIG, galleryUiBundle)
            })
        }
    }

    private fun startActivity(activity: FragmentActivity) {
        activity
                .registerForActivityResult(startActivityForResult, activityResult)
                .launch(launchIntent(activity))
    }

    private fun startFragment(fragment: Fragment) {
        fragment
                .registerForActivityResult(startActivityForResult, activityResult)
                .launch(launchIntent(fragment.requireContext()))
    }
}