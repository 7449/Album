package com.gallery.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.kotlin.expand.os.getIntExpand
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableExpand
import androidx.kotlin.expand.os.orEmptyExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGallery
import com.gallery.ui.activity.GalleryActivity

class Gallery(
        private val fragmentManager: FragmentManager,
        private val galleryBundle: GalleryBundle = GalleryBundle(),
        private val galleryUiBundle: GalleryUiBundle = GalleryUiBundle(),
        private val clz: Class<*> = GalleryActivity::class.java,
        private val galleryListener: GalleryListener
) {
    class GalleryFragment : Fragment()

    companion object {
        private val TAG = Gallery::class.java.simpleName
    }

    init {
        val galleryFragment = getGalleryFragment(fragmentManager)
        galleryFragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
            if (intent.resultCode == Activity.RESULT_OK) {
                val bundleExpand = intent.data?.extras.orEmptyExpand()
                when (bundleExpand.getIntExpand(UIResult.FRAGMENT_RESULT_TYPE)) {
                    UIResult.FRAGMENT_RESULT_CROP -> {
                        galleryListener.onGalleryCropResource(galleryFragment.requireActivity(), bundleExpand.getParcelableExpand(UIResult.FRAGMENT_RESULT_URI))
                    }
                    UIResult.FRAGMENT_RESULT_RESOURCE -> {
                        galleryListener.onGalleryResource(galleryFragment.requireActivity(), bundleExpand.getParcelableExpand(UIResult.FRAGMENT_RESULT_ENTITY))
                    }
                    UIResult.FRAGMENT_RESULT_RESOURCES -> {
                        galleryListener.onGalleryResources(galleryFragment.requireActivity(), bundleExpand.getParcelableArrayListExpand(UIResult.FRAGMENT_RESULT_ENTITIES))
                    }
                }
            } else if (intent.resultCode == Activity.RESULT_CANCELED) {
                galleryListener.onGalleryCancel(galleryFragment.requireActivity())
            }

        }.launch(Intent(galleryFragment.context, clz).apply {
            putExtras(Bundle().apply {
                putParcelable(IGallery.GALLERY_START_CONFIG, galleryBundle)
                putParcelable(UIResult.UI_CONFIG, galleryUiBundle)
            })
        })
    }

    private fun getGalleryFragment(fragmentManager: FragmentManager): GalleryFragment {
        var galleryFragment = fragmentManager.findFragmentByTag(TAG) as? GalleryFragment
        if (galleryFragment == null) {
            galleryFragment = GalleryFragment()
            fragmentManager
                    .beginTransaction()
                    .add(galleryFragment, TAG)
                    .commitNow()
        }
        return galleryFragment
    }
}