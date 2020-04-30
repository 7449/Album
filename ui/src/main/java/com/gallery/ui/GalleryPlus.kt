package com.gallery.ui

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.gallery.ui.GalleryPlus
import com.gallery.ui.activity.GalleryActivity
import com.gallery.ui.fragment.GalleryFragment

class GalleryPlus {

    companion object {
        private val TAG = GalleryPlus::class.java.simpleName
        const val REQUEST_CODE = 1000
    }

    private val galleryFragment: GalleryFragment
    private var galleryListener: GalleryListener? = null

    constructor(activity: FragmentActivity) {
        galleryFragment = getGalleryFragment(activity.supportFragmentManager)
    }

    constructor(fragment: Fragment) {
        galleryFragment = getGalleryFragment(fragment.childFragmentManager)
    }

    fun setCallback(galleryListener: GalleryListener): GalleryPlus {
        this.galleryListener = galleryListener
        return this
    }

    fun start() {
        galleryFragment.galleryListener = galleryListener
        galleryFragment.startActivityForResult(Intent(galleryFragment.context, GalleryActivity::class.java).apply {
        }, REQUEST_CODE)
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