package com.gallery.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.kotlin.expand.os.getIntExpand
import androidx.kotlin.expand.os.getParcelableArrayListExpand
import androidx.kotlin.expand.os.getParcelableExpand
import com.gallery.ui.GalleryListener
import com.gallery.ui.GalleryPlus
import com.gallery.ui.UIResult

class GalleryFragment : Fragment() {

    var galleryListener: GalleryListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GalleryPlus.REQUEST_CODE) {
            val bundleExpand = data?.extras
            when (bundleExpand.getIntExpand(UIResult.FRAGMENT_RESULT_TYPE)) {
                UIResult.FRAGMENT_RESULT_CROP -> {
                    galleryListener?.onGalleryCropResource(requireActivity(), bundleExpand.getParcelableExpand(UIResult.FRAGMENT_RESULT_URI))
                }
                UIResult.FRAGMENT_RESULT_RESOURCE -> {
                    galleryListener?.onGalleryResource(requireActivity(), bundleExpand.getParcelableExpand(UIResult.FRAGMENT_RESULT_ENTITY))
                }
                UIResult.FRAGMENT_RESULT_RESOURCES -> {
                    galleryListener?.onGalleryResources(requireActivity(), bundleExpand.getParcelableArrayListExpand(UIResult.FRAGMENT_RESULT_ENTITIES))
                }
            }
        }
    }
}